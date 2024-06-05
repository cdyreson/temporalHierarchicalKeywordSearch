package messiah.parse.generic;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import messiah.Config;
import messiah.KeywordInfo;
import messiah.database.Database;
import messiah.parse.*;
import usu.NodeId;
import usu.PathId;

/**
 * This object is in charge of building the keyword index
 *
 * @author John Truong Ba Quan and Curtis
 */
public class KeywordIndexBuilder implements ParserListener {

    private final Database db;
    private NodeIdBuilder nodeIdBuilder = null;
    private PathIndexBuilder pathIndexBuilder = null;

    StopwordFilter stopwordFilter = new StopwordFilter();

    public KeywordIndexBuilder(Database db, NodeIdBuilder nodeIdBuilder, PathIndexBuilder pathIndexBuilder) {
        this.db = db;
        this.nodeIdBuilder = nodeIdBuilder;
        this.pathIndexBuilder = pathIndexBuilder;
    }

    @Override
    public void startDocument() {
    }

    @Override
    public void endDocument() {
    }

    @Override
    public void start(String str, boolean isAttribute, boolean isValue) {
        String[] tokens;
        str = str.trim().toLowerCase(); // trim and convert to lower case to avoid case-sensitivity
        if (isValue) {
            tokens = str.split(Config.TOKENS_DELIMITER);

        } else // element/attribute label
        {
            tokens = new String[]{str}; // no need to split        
        }
        for (String token : tokens) {
            if (token != null
                    && !token.isEmpty() //                        && !stopwordFilter.isStopword(token)
                    ) {
                //System.out.println("Curt: KeywordIndexBuilding " + token + " --> " + nodeIdBuilder.getNodeId());
                this.storeNode(token, nodeIdBuilder.getNodeId(), pathIndexBuilder.getCurrentPathId());

            }
        }
        tokens = null;
    }

    @Override
    public void end(boolean isAttribute, boolean isValue) {
    } // nothing to do

    public void storeNode(String keyword, NodeId nodeId, PathId pathId) {
        KeywordInfo info;
        Map<PathId, List<NodeId>> map;
        Set<PathId> set;

        //pathId = pathId.getParentId();
        if (!db.keywordIndex.containsKey(keyword)) {
            //System.out.println("Curt: not contains " + keyword);
            info = new KeywordInfo();
            //db.keywordIndex.put(keyword, info);
            map = new HashMap();
            set = new HashSet();
        } else {
            info = db.keywordIndex.get(keyword);
            map = db.keywordNodesIndex.get(keyword);
            set = db.keywordPathsIndex.get(keyword);
        }
        //System.out.println("Curt: adding " + keyword + " nodeId " + nodeId + " pathId " + pathId);
        //info.addNodeId(nodeId, pathId);
        db.keywordIndex.put(keyword, info);
        //if (set == null) {
        //    System.out.println("Error set shouldn't be null " + pathId + " " + keyword + " " + db.keywordIndex.size() + " " + db.keywordPathsIndex.size());
        //    return;
        //}
        if (!set.contains(pathId)) {
            set.add(pathId);
            map.put(pathId, new ArrayList());
        }
        List a = map.get(pathId);
        a.add(nodeId);
        map.put(pathId, a);

        db.keywordNodesIndex.put(keyword, map);
        //System.out.println("KeywordIndexBuilder " + keyword + " set is " + set);
        db.keywordPathsIndex.put(keyword, set);
        //info = null;
        //map = null;
        //set = null;
    }

    /*
    public void storePath(String keyword, PathId pathId) {
        //System.out.println("Curt: keyword path " + keyword + " " + pathId);
        KeywordInfo info;
        if (!db.keywordIndex.containsKey(keyword)) {
            info = new KeywordInfo();
        } else {
            info = db.keywordIndex.get(keyword);
        }
        info.addPathId(pathId);
        db.keywordIndex.put(keyword, info);
    }
    */

    public static void main(String[] args) {
        //Database bdb = new messiah.database.berkeleydb.Database(Config.DB_FOLDER_STRING + "foo.xml", true);
        Database bdb = new messiah.database.berkeleydb.Database(true, false);
        Map<String, KeywordInfo> keywordIndex = bdb.keywordIndex;
        Map<String, Set<PathId>> keywordPathsIndex = bdb.keywordPathsIndex;
        Map<String, Map<PathId, List<NodeId>>> keywordNodesIndex = bdb.keywordNodesIndex;
        for (String keyword : keywordIndex.keySet()) {
            KeywordInfo info = keywordIndex.get(keyword);
            System.out.println("key = " + keyword + " --> " + info);
            Map<PathId, List<NodeId>> map = keywordNodesIndex.get(keyword);
            for (PathId pathId : map.keySet()) {
                System.out.println("  path " + pathId + " --> " + map.get(pathId));
            }
            Set<PathId> set = keywordPathsIndex.get(keyword);
            for (PathId pathId : set) {
                System.out.println("  set " + pathId);
            }
        }

    }
}
