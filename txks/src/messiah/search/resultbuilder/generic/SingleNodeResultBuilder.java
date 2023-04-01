package messiah.search.resultbuilder.generic;

import messiah.search.resultbuilder.*;
import java.util.Set;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import messiah.NodeInfo;
import messiah.storage.generic.DbAccess;
import usu.NodeId;
import messiah.database.Database;
import java.util.Map;

/**
 *
 * @author truongbaquan
 */
public class SingleNodeResultBuilder extends ResultBuilder {
    
    private final DbAccess db;
    private final Database bdb;

    public SingleNodeResultBuilder(DbAccess db, Database bdb) {
        this.db = db;
        this.bdb = bdb;
    }

    @Override
    public JTree buildResultTree(Set<NodeId> resultSet, String[] keywords) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(resultSet.size() + " results");
        Map<NodeId,NodeInfo> dbMap = (Map)bdb.nodeIndex;
        for (NodeId nodeId : resultSet) {
            NodeInfo info = dbMap.get(nodeId);
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(db.getLabel(info.getPathId()));
            root.add(child);
        }
        return new JTree(root);
    }
    
}
