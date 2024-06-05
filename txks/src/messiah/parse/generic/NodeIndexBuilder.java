package messiah.parse.generic;

import messiah.parse.*;
import messiah.NodeInfo;
import usu.NodeId;
import messiah.database.Database;
import java.util.SortedMap;

/**
 * This object is in charge of building the node index from node id to node info
 *
 * @author John Truong Ba Quan and Curtis
 */
public class NodeIndexBuilder implements ParserListener {

    private Database db;
    private NodeIdBuilder nodeIdBuilder = null;
    private final PathIndexBuilder pathIndexBuilder;

    public NodeIndexBuilder(Database db, NodeIdBuilder nodeIdBuilder, PathIndexBuilder pathIndexBuilder) {
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
        if (isAttribute && !isValue) {
            str = '@' + str;
        }
        NodeInfo info = new NodeInfo(pathIndexBuilder.getCurrentPathId(), str, nodeIdBuilder.getLevel(), /* default to null for time */ null);
        //System.out.println("Curt: nodeId " + this.nodeIdBuilder.getNodeId() + " info " + info);
        db.nodeIndex.put(this.nodeIdBuilder.getNodeId(), info);
    }

    @Override
    public void end(boolean isAttribute, boolean isValue) {
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
