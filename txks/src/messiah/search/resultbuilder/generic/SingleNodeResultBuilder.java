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
import usu.algebra.KeywordSearchExpression;

/**
 *
 * @author truongbaquan
 */
public class SingleNodeResultBuilder extends ResultBuilder {
    
    DbAccess db;
    Database bdb;
    KeywordSearchExpression exp;

    public SingleNodeResultBuilder(DbAccess db, Database bdb, KeywordSearchExpression exp) {
        this.db = db;
        this.bdb = bdb;
        this.exp = exp;
    }

    @Override
    public JTree buildResultTree(Set<NodeId> resultSet, String[] keywords) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(resultSet.size() + " results");
        Map<NodeId,NodeInfo> dbMap = (Map)bdb.nodeIndex;
        for (NodeId nodeId : resultSet) {
            NodeInfo info = dbMap.get(nodeId);
            //DefaultMutableTreeNode child = new DefaultMutableTreeNode(db.getLabel(info.getPathId()));
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(info.getData());
            root.add(child);
        }
        return new JTree(root);
    }
    
}
