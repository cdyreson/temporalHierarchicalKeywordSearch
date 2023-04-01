package messiah.search.resultbuilder.generic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import messiah.NodeInfo;
import messiah.Path;
import messiah.database.Database;
import messiah.search.resultbuilder.*;
import messiah.storage.generic.DbAccess;
import messiah.utils.Counter;
import usu.NodeId;
import usu.PathId;
import java.util.SortedMap;

/**
 *
 * @author truongbaquan
 */
public class XSeekResultBuilder extends ResultBuilder {

    /**
     * Indicating whether the root path should be included in the results
     */
    private boolean displayRootPath = true;

    private final DbAccess db;
    private final Database bdb;
    private final SortedMap<NodeId, NodeInfo> nodeMap;
    private HashMap<String, Counter> pathCounterMap;

    public XSeekResultBuilder(DbAccess db, Database bdb) {
        this.db = db;
        this.bdb = bdb;
        this.nodeMap = bdb.nodeIndex;
    }

    @Override
    public JTree buildResultTree(Set<NodeId> resultSet, String[] keywords) {
        pathCounterMap = new HashMap<>();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(resultSet.size() + " results");
        for (NodeId nodeId : resultSet) {
            PathId pathId = nodeMap.get(nodeId).getPathId();
            int entityLevel = db.getPathEntityLevel(pathId);
            NodeId entityNodeId = nodeId.getAncestor(entityLevel);
            root.add(this.buildSubtree(entityNodeId, keywords));
        }

        int total = 0;
        for (Map.Entry<String, Counter> entry : pathCounterMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().getValue());
            total += entry.getValue().getValue();
        }
        System.out.println("total = " + total);

        return new JTree(root);
    }

    private DefaultMutableTreeNode buildSubtree(NodeId ancestorId, String[] keywords) {
//        TimedMsg.printMsg("Start building subtree for " + ancestorId);
        int ancLvl = ancestorId.getLevel();
        NodeId upperBound = ancestorId.getNextFirstDescendant(ancLvl);

        SortedMap<NodeId, NodeInfo> map = bdb.nodeIndex.subMap(ancestorId, upperBound);
        DefaultMutableTreeNode root = null;
        int curLvl = ancLvl - 1;
        Stack<DefaultMutableTreeNode> stack = new Stack<>();

//        TimedMsg.printMsg("Start node traversal from " + ancestorId + " to " + upperBound);
        NodeId lastKey = null;
//        System.out.println("key = " + key);
        for (Map.Entry<NodeId, NodeInfo> entry : map.entrySet()) {
            NodeId key = entry.getKey();
            NodeInfo info = entry.getValue();
            if (lastKey == null && displayRootPath) {
                Path path = db.getPath(info.getPathId());
                String[] labelTokens = path.getInfo().getPathExpr().split("\\.");
                int nodeLvl = info.getLevel();
                int pathLvl = path.getInfo().getLevel();
                int parentLvl = nodeLvl == pathLvl ? pathLvl - 1 : pathLvl;
                for (int lvl = 1; lvl <= parentLvl; lvl++) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(labelTokens[lvl].substring(0, labelTokens[lvl].length() - 1));
                    if (stack.isEmpty()) {
                        root = node;
                    } else {
                        stack.peek().add(node);
                    }
                    stack.push(node);
                }

                String pathExpr = path.getInfo().getPathExpr();
                Counter counter = pathCounterMap.get(pathExpr);
                if (counter == null) {
                    counter = new Counter();
                    pathCounterMap.put(pathExpr, counter);
                }
                counter.increment();
            }
//            System.out.println("info = " + db.getPath(info.getPathId()).getInfo().getPathExpr());
            int lcaLvl = (lastKey == null) ? ancLvl : lastKey.computeNCALevel(key);
            while (curLvl > lcaLvl) {
                stack.pop();
                curLvl--;
            }

            DefaultMutableTreeNode node = new DefaultMutableTreeNode(info.getData() + ":" + key);
            if (stack.isEmpty()) {
                root = node;
            } else {
                stack.peek().add(node);
            }
            stack.push(node);
            curLvl++;
            lastKey = key;

//            System.out.println("key = " + key);
        }
//        TimedMsg.printMsg("Finish node traversal");
        return root;
    }
}
