package messiah.search.resultbuilder.generic;

import messiah.search.resultbuilder.*;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import messiah.NodeInfo;
import messiah.Path;
import messiah.storage.generic.DbAccess;
import messiah.utils.Counter;
import usu.NodeId;
import messiah.database.Database;
import java.util.SortedMap;
import usu.dln.DLN;

/**
 *
 * @author truongbaquan
 */
public class SubtreeResultBuilder extends ResultBuilder {
    private boolean verbose = false;

    /**
     * Indicating whether the root path should be included in the results
     */
    private final boolean displayRootPath = false;

    private final DbAccess db;
    private final Database bdb;
    private HashMap<String, Counter> pathCounterMap;

    public SubtreeResultBuilder(DbAccess db, Database bdb) {
        this.db = db;
        this.bdb = bdb;
    }

    @Override
    public JTree buildResultTree(Set<NodeId> resultSet, String[] keywords) {
        pathCounterMap = new HashMap<>();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(resultSet.size() + " results");
        for (NodeId nodeId : resultSet) {
            if (verbose) System.out.println("Curt: Building with " + nodeId + " " + root);
            DefaultMutableTreeNode newNode = this.buildSubtree(nodeId);
            if (newNode != null) {
              root.add(newNode);
            }
        }

        int total = 0;
        for (Entry<String, Counter> entry : pathCounterMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().getValue());
            total += entry.getValue().getValue();
        }
        System.out.println("total =  " + total + " " + resultSet.size());

        return new JTree(root);
    }

    private DefaultMutableTreeNode buildSubtree(NodeId ancestorId) {
//        TimedMsg.printMsg("Start building subtree for " + ancestorId);

        int ancLvl = ancestorId.getLevel();
        NodeId upperBound = ancestorId.getNextFirstDescendant(ancLvl);
        if (verbose) System.out.println("Curt: SubtreeResultBuilder ancestorId " + ancestorId + " upperBound " + upperBound);

        // This one works for in memory only!
        NodeInfo rootNodeInfo = bdb.nodeIndex.get(ancestorId);
        SortedMap<NodeId, NodeInfo> map = bdb.nodeIndex.subMap(ancestorId, upperBound);
        SortedMap<NodeId, NodeInfo> myMap = new TreeMap();
        myMap.put(ancestorId,rootNodeInfo);
        
        // For now put into a temporary map, need to populate with on disk stuff
        for (Entry<NodeId,NodeInfo> entry : map.entrySet()) {
            myMap.put((NodeId)entry.getKey(), (NodeInfo)entry.getValue());
        }
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("dummy");

        int curLvl = ancLvl - 1;
        Stack<DefaultMutableTreeNode> stack = new Stack<>();

//        TimedMsg.printMsg("Start node traversal from " + ancestorId + " to " + upperBound);
        NodeId lastKey = null;

//        System.out.println("key = " + key);
        for (Entry<NodeId,NodeInfo> entry : myMap.entrySet()) {
            // System.out.println("Curt: SubtreeResultBuilder key " + entry.toString());
            NodeId key = entry.getKey();
            NodeInfo info = entry.getValue();
            //if (!key.lessThan(upperBound)) break;
            if (verbose) System.out.println("Curt: SubtreeResultBuilder key " + key + " nodeInfo " + info);
            if (lastKey == null && displayRootPath) {
                Path path = db.getPath(info.getPathId());
                String[] labelTokens = path.getInfo().getPathExpr().split("\\.");
                int nodeLvl = info.getLevel();
                int pathLvl = path.getInfo().getLevel();
                int parentLvl = nodeLvl == pathLvl ? pathLvl - 1 : pathLvl;
                //System.out.println("Curt: SubtreeResultBuilder nodeLvl " + pathLvl);
                for (int lvl = 1; lvl <= parentLvl; lvl++) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(labelTokens[lvl].substring(0, labelTokens[lvl].length() - 1));
                    if (stack.isEmpty()) {
                        //System.out.println("Curt: SubtreeResultBuilder adding to root");
                        root = node;
                    } else {
                        //System.out.println("Curt: SubtreeResultBuilder push on stack");
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
            //System.out.println("Curt: SubtreeResultBuilder lastKey " + lastKey + " lcaLvl " + lcaLvl + " ancLvl " + ancLvl + " curLvl " + curLvl);
            while (curLvl > lcaLvl) {
                stack.pop();
                curLvl--;
            }

            DefaultMutableTreeNode node = new DefaultMutableTreeNode(info.getData());
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
