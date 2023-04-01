package messiah.parse.generic;

import java.util.ArrayList;
import java.util.List;
import messiah.parse.*;
import messiah.NodeInfo;
import usu.NodeId;
import messiah.database.Database;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import messiah.Config;
import usu.PathId;
import usu.level.Branch;
import usu.level.PrefixPair;

/**
 * This object is in charge of building the node index from node id to node info
 *
 * @author John Truong Ba Quan and Curtis
 */
public class PrefixIndexBuilder implements ParserListener {

    private Database db;
    private Integer[] levels;
    private StopwordFilter stopwordFilter = new StopwordFilter();
    private int preOrderCount;
    private int level = 0;

    public PrefixIndexBuilder(Database db) {
        this.db = db;
        this.levels = new Integer[Config.MAX_DEPTH];
        this.preOrderCount = 0;
    }

    /*
     * Copy the current array and add space to the end
     */
    private void increaseDepth() {
        Integer[] temp = new Integer[Config.MAX_DEPTH + levels.length];
        for (int i = 0; i < levels.length; i++) {
            temp[i] = levels[i];
        }
        levels = temp;
    }

    @Override
    public void startDocument() {
        this.level = 0;
    }

    @Override
    public void endDocument() {
    }

    @Override
    public void start(String str, boolean isAttribute, boolean isValue) {
        String[] tokens;
        preOrderCount++;
        level++;
        /*
        if (isAttribute) {
            if (isValue) {
                tokens = str.split(Config.TOKENS_DELIMITER);
                for (String token : tokens) {
                    if (token == null) {
                        continue;
                    }
                    token = token.trim().toLowerCase();
                    if (!token.isEmpty() && // not empty token
                            (!isValue || !stopwordFilter.isStopword(token))) { // label token or non-stopword value token
                        this.store(token, preOrderCount);
                    }
                }
            } else {
                tokens = new String[]{str}; 
            }
        }
        */
        if (isValue) // data value
        {
            tokens = str.split(Config.TOKENS_DELIMITER);
        } else // element/attribute label
        {
            tokens = new String[]{str}; // no need to split        
        }
        for (String token : tokens) {
            if (token == null) {
                continue;
            }
            token = token.trim().toLowerCase();
            if (!token.isEmpty() && // not empty token
                    (!isValue || !stopwordFilter.isStopword(token))) { // label token or non-stopword value token
                this.store(token, preOrderCount, level);
            }
        }
    }

    @Override
    public void end(boolean isAttribute, boolean isValue) {
        level--;
    }

    public void store(String term, int id, int level) {
        //SortedMap<NodeId, PathId> nodeIdList;
        List<PrefixPair> branchList;
        if (!db.prefixIndex.containsKey(term)) {
            branchList = new ArrayList(4);
        } else {
            branchList = db.prefixIndex.get(term);
        }
        branchList.add(new PrefixPair(id, level));
        db.prefixIndex.put(term, branchList);
    }

    public static void main(String[] args) {
        //Database bdb = new messiah.database.berkeleydb.Database(Config.DB_FOLDER_STRING + "foo.xml", true);
        Database bdb = new messiah.database.berkeleydb.Database(true, false);
        SortedMap<NodeId, NodeInfo> map = bdb.nodeIndex;
        for (NodeId nodeId : map.keySet()) {
            NodeInfo value = map.get(nodeId);
            System.out.println("key = " + nodeId + " --> " + value);
        }

    }
}
