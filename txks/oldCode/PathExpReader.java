package messiah.parse;

import java.util.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import messiah.Path;
import messiah.PathInfo;
import usu.PathId;
import usu.PathIdFactory;
//import usu.dln.DLNFactory;

/**
 * This parser parses a lexicographically sorted list of path expressions and
 * allocate unique index to it Note: Can't work for Treebank because Treebank
 * has a tag with .. (fail the scheme using .# to enclose tag)
 *
 * @author John Truong Ba Quan, Curtis
 * @param <F> - Factory of the kind of node to use
 */
public class PathExpReader<F extends PathIdFactory> {

    //<editor-fold defaultstate="collapsed" desc="Max Depth">
    private int maxDepth = 0;

    public int getMaxDepth() {
        return maxDepth;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Fanout">
    private int fanout = 0;

    public int getFanout() {
        return fanout;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Level Order">
    private int levelOrder = 0;

    public int getLevelOrder() {
        return levelOrder;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Path Tree">
    private JTree tree = null;

    public JTree getTree() {
        return tree;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Path List">
    private final SortedMap<String, Path> pathMapByPathExpr = new TreeMap<>();
    private final SortedMap<PathId, Path> pathMapById = new TreeMap<>();
    private final Map<DefaultMutableTreeNode, Path> pathMapByTreeNode = new HashMap<>();

    public Path getPathByPathExpr(String pathExpr) {
        return pathMapByPathExpr.get(pathExpr);
    }

    public Path getPathById(PathId pathId) {
        return pathMapById.get(pathId);
    }

    public Path getPathByTreeNode(DefaultMutableTreeNode treeNode) {
        return pathMapByTreeNode.get(treeNode);
    }

    public Collection<Path> getPathsSortedByPathExpr() {
        return pathMapByPathExpr.values();
    }

    public Collection<Path> getPathsSortedById() {
        return pathMapById.values();
    }
    //</editor-fold>

    private F pathIdFactory;
    
    void parse(SortedMap<String, LabelAppearanceInfo> pathExprs, Set<String> repeatablePathExpr, F factory) {
        HashMap<String, Integer> posMap = new HashMap<>();
        int size = pathExprs.size();
        this.pathIdFactory = factory;
        String[] pathExprArray = new String[size];
        int[] numChildren = new int[size];
        int[] level = new int[size];
        boolean[] isRepeatable = new boolean[size];
        int[] numApps = new int[size];
        double[] parentAppRates = new double[size];
        DefaultMutableTreeNode[] nodes = new DefaultMutableTreeNode[size];
        DefaultMutableTreeNode rootNode = null;


        try {
            int i = 0;
            for (String pathExpr : pathExprs.keySet()) {
                // parent pathExpr
                String parentPathExpr = PathExprUtilities.getParentPathExpr(pathExpr);
                // mapping between pathExpr and its position
                posMap.put(pathExpr, i);
                pathExprArray[i] = pathExpr;

                // repeatable?
                isRepeatable[i] = repeatablePathExpr.contains(pathExpr) ? true : false;
                // level?
                level[i] = PathExprUtilities.getLevel(pathExpr);

                numApps[i] = pathExprs.get(pathExpr).getNumApp();

                // number of children
                numChildren[i] = 0;
                System.out.println("i = " + i + ":" + pathExpr + " - " + parentPathExpr);
                if (!parentPathExpr.equals("")) {
                    int parentIndex = posMap.get(parentPathExpr);
                    numChildren[parentIndex]++;
                    parentAppRates[i] = ((double) pathExprs.get(pathExpr).getNumAppInParent()) / numApps[parentIndex];
                } else {
                    parentAppRates[i] = 1;
                }
                // construct the tree node
                PathInfo obj = new PathInfo(pathExpr, level[i], isRepeatable[i], numApps[i], parentAppRates[i]);
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(obj);
                if (!parentPathExpr.equals("")) { // not root
                    DefaultMutableTreeNode parentNode = nodes[posMap.get(parentPathExpr)];
                    parentNode.add(node);
                } else // root
                {
                    rootNode = node;
                }
                nodes[i] = node;

                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }

        // build tree
        this.tree = new JTree(rootNode);

        // compute paths' maxDepth and fanout        
        for (int j = 0; j < size; j++) {
            if (this.maxDepth < level[j]) {
                this.maxDepth = level[j];
            }
            if (this.fanout < numChildren[j]) {
                this.fanout = numChildren[j];
            }
        }
        PathId[] index = new PathId[size];
        index[0] = pathIdFactory.rootId();
        // build paths
        Map<String,PathId> pathIdMap = new HashMap();
        Map<String,PathId> parentPathIdMap = new HashMap();
        //pathIdMap.put("",PathId.getRootId());
        for (int j = 0; j < size; j++) {
            String parentPathExpr = PathExprUtilities.getParentPathExpr(pathExprArray[j]);
            if (parentPathIdMap.containsKey(parentPathExpr)) {
                PathId next = parentPathIdMap.get(parentPathExpr).nextSibling();
                //System.out.println("Curt: next " + next);
                pathIdMap.put(pathExprArray[j], next);
                parentPathIdMap.put(parentPathExpr,next);
                index[j] = next;
            } else {
                if (pathIdMap.containsKey(parentPathExpr)) {
                  PathId child = pathIdMap.get(parentPathExpr).newChild();
                  //System.out.println("Curt: child " + child);
                  pathIdMap.put(pathExprArray[j], child);
                  parentPathIdMap.put(parentPathExpr,child);
                  index[j] = child;
                } else {
                    pathIdMap.put(pathExprArray[j],pathIdFactory.rootId());
                    index[j] = pathIdFactory.rootId();
                }
            }
            Path path = new Path(index[j], pathExprArray[j], level[j], isRepeatable[j], numApps[j], parentAppRates[j]);
            this.pathMapByPathExpr.put(pathExprArray[j], path);
            this.pathMapByTreeNode.put(nodes[j], path);
            this.pathMapById.put(index[j], path);
            //System.out.println("Curt: path index value " + j + " " + index[j]);
            //System.out.println("Curt: info " + path.getInfo().getPathExpr() + ":" + path.getInfo().getNumApp() + ":" + path.getInfo().getParentAppRate());
        }
    }
}
