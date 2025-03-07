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
import messiah.search.SearchAlgoEnum;
import static messiah.search.SearchAlgoEnum.NonSequencedFSLCA;
import static messiah.search.SearchAlgoEnum.NontemporalSearch;
import static messiah.search.SearchAlgoEnum.SequencedSearch;
import usu.algebra.KeywordSearchExpression;
import usu.dln.HistoryDLN;
import usu.dln.TimeElementHistoryDLN;
import usu.temporal.Time;
import usu.temporal.TimeElement;

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

    DbAccess db;
    Database bdb;
    KeywordSearchExpression exp;
    private HashMap<String, Counter> pathCounterMap;

    public SubtreeResultBuilder(DbAccess db, Database bdb, KeywordSearchExpression exp) {
        this.db = db;
        this.bdb = bdb;
        this.exp = exp;
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
        // TimedMsg.printMsg("Start building subtree for " + ancestorId);
        
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
        Stack<TimeElement> timeStack = new Stack();
        // Temporal search needs a time on the time stack
        switch (this.exp.getSearchType()) {
            // Sequenced
            case SequencedSearch:
            case SequencedPartialFSLCA:
            case SequencedFSLCA:
            case EarliestSearch:
            case LatestSearch:
                timeStack.push(((TimeElementHistoryDLN) ancestorId).getTime(/*curLvl*/ancLvl));
                break;
            default:
        }

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
                        //if (this.exp.getSearchType() == SearchAlgoEnum.SequencedSearch) {
                        //    HistoryDLN hdln = node.
                        //} else {
                        //     
                        //}
                    }
                    stack.push(node);
                    switch (this.exp.getSearchType()) {
                        // Sequenced
                        case SequencedSearch:
                        case SequencedPartialFSLCA:
                        case SequencedFSLCA:
                        case EarliestSearch:
                        case LatestSearch:
                            timeStack.push(((TimeElementHistoryDLN) key).getTime(/*lvl*/ancLvl));
                            break;
                        default:
                    }               
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
                switch (this.exp.getSearchType()) {
                    // Sequenced
                    case SequencedSearch:
                    case SequencedPartialFSLCA:
                    case SequencedFSLCA:
                    case EarliestSearch:
                    case LatestSearch:
                        timeStack.pop();
                        break;
                    default:
                }
                curLvl--;
            }

            // System.out.println("class is " + key.getClass().getName());
            TimeElement time;
            String nodeInformationString = info.getData();
            switch (this.exp.getSearchType()) {
                // No timestamps
                case NontemporalSearch:
                case PartialFSLCA:
                case CompleteFSLCA:
                case NonTemporalFSLCA:
                    time = null;
                    break;

                // Sequenced
                case SequencedSearch:
                case SequencedPartialFSLCA:
                case SequencedFSLCA:
                case EarliestSearch:
                case LatestSearch:
                    // Figure out time

                    if (key.getClass() == TimeElementHistoryDLN.class) {
                        // @earliest title year
                        //System.out.println("here " + curLvl + " " + timeStack.size() + " " + ((TimeElementHistoryDLN) key).getTime(/*curLvl*/ancLvl));

                        time = ((TimeElementHistoryDLN) key).getTime(/*curLvl*/ancLvl);
                        if (time.isEmpty()) {
                            time = null;
                        } else {
                            time = timeStack.peek().intersection(((TimeElementHistoryDLN) key).getTime(/*curLvl*/ancLvl));
                        }
                    } else if (key.getClass() == HistoryDLN.class) {
                        Time t = ((HistoryDLN) key).getTime(/*curLvl*/ancLvl);
                        if (t == null) {
                            time = null;
                        } else {         
                            time = timeStack.peek().intersection(((HistoryDLN) key).getTime(/*curLvl*/ancLvl));
                        }
                    } else {
                        time = null;//new TimeElement();
                    }
                    
                    if (time == null || time.isEmpty()) {
                        nodeInformationString = null;
                    } else {
                        nodeInformationString += " " + time.toString();
                    }

                    if (nodeInformationString != null) {
                        // Modify time stack
                        timeStack.push(time);
                        /*
                        if (key.getClass() == TimeElementHistoryDLN.class) {
                            timeStack.push(((TimeElementHistoryDLN) key).getTime(ancLvl));
                        } else if (key.getClass() == HistoryDLN.class) {
                            timeStack.push(new TimeElement(((HistoryDLN) key).getTime(ancLvl)));
                        } else {
                            timeStack.push(new TimeElement());
                        }
                        */
                    }


                    break;

                // Nonsequenced
                case TemporalSearch:
                case NonSequencedFSLCA:
                case NonsequencedPartialFSLCA:
                case DurationalSearch:
                    if (key.getClass() == TimeElementHistoryDLN.class) {
                        time = ((TimeElementHistoryDLN) key).getTime(/*curLvl*/ancLvl);
                        if (time.isEmpty()) {
                            time = null;
                        }
                    } else if (key.getClass() == HistoryDLN.class) {
                        Time t = ((HistoryDLN) key).getTime(/*curLvl*/ancLvl);
                        if (t == null) {
                            time = null;
                        } else { 
                            time = new TimeElement(((HistoryDLN) key).getTime(/*curLvl*/ancLvl));
                        }  
                    } else {
                        time = new TimeElement();
                    }
                    if (time == null) {
                        nodeInformationString = null;
                    } else {
                        nodeInformationString += " " + time.toString();
                    }
                    
                    break;
                default:
                    time = null;
            }
            
            // Subtree result builder skips some nodes if no nodeInformaitonString 
            // (when the time at a given level is not existent)
            if (nodeInformationString != null) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeInformationString);
                if (stack.isEmpty()) {
                    root = node;
                } else {
                    stack.peek().add(node);
                }
                stack.push(node);
                curLvl++;
                lastKey = key;
            }


//            System.out.println("key = " + key);
        }

//        TimedMsg.printMsg("Finish node traversal");
        return root;
    }
}
