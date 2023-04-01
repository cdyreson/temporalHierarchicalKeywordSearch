package messiah.search.slca.generic;

//import messiah.search.slca.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
//import messiah.encoding.BinaryDeweyUtils;
import usu.PathId;
import usu.PathIdFactory;

/**
 *
 * @author truongbaquan
 */
public class BinarySLCAFinder<T> {
    
    private final PathIdFactory pathIdFactory;
    //private BinaryDeweyUtils utils;

    public BinarySLCAFinder(T factory) {
        this.pathIdFactory = (PathIdFactory)factory;
    }

    public Set<PathId> getSLCA(TreeSet<PathId>... sets) {
        if (sets.length == 0) return null;
        Set<PathId> result = new LinkedHashSet<>();

        // get the SLCA of first set
        PathId lastNode = pathIdFactory.minId();
        //System.out.println("Curt: BinarySLCAFinder lastNode " + lastNode);
        for (PathId node : sets[0]) {
            //System.out.println("Curt: BinarySLCAFinder node " + node);
            if (!lastNode.isAncestorOrDescendant(node)) {
                //System.out.println("Curt: BinarySLCAFinder adding to result " + lastNode);
                result.add(lastNode);
            }
            lastNode = node;
        }
        //System.out.println("Curt: BinarySLCAFinder adding to result final " + lastNode);
        result.add(lastNode);

        // get the SLCA of all sets
        for (int i = 1; i < sets.length; i++) {
            result = getSLCA(result, (TreeSet<PathId>)sets[i]);
        }
        return result;
    }

    private Set<PathId> getSLCA(Set<PathId> set1, TreeSet<PathId> set2) {
        LinkedHashSet<PathId> result = new LinkedHashSet<>(set1.size());
        PathId lastLca = pathIdFactory.minId();
        for (PathId node : set1) {  
            //System.out.println("Curt: BinarySLCAFinder trying node " + node);
            int lcaLvl = getLcaLvl(node, set2);
            PathId lca = node.getAncestor(lcaLvl);
            //System.out.println("Curt: BinarySLCAFinder lcaLvl " + lcaLvl + " lca " + lca + " lastLca " + lastLca);
            if (lastLca.lessThanOrEqualTo(lca)) {
                //System.out.println("Curt: BinarySLCAFinder lastLCA " + lastLca + " <= " + lca);
                if (!lastLca.isAncestorOrDescendant(lca)) {
                    //System.out.println("Curt: BinarySLCAFinder not ancdecs " + lastLca + " and " + lca);
                    if (lastLca != pathIdFactory.minId()) result.add(lastLca);
                }
                lastLca = lca;
            }           
        }
        if (lastLca != pathIdFactory.minId()) result.add(lastLca);
        return result;
    }

    /**
     * Finds the LCA level between a node and a sorted list of nodes
     * @param node
     * @param list
     * @return 
     */
    public int getLcaLvl(PathId node, TreeSet<PathId> list) {
        PathId left = list.floor(node);
        //System.out.println("Curt: BinarySLCAFinder left " + left);
        PathId right = list.ceiling(node);
        //System.out.println("Curt: BinarySLCAFinder right " + right);
        int leftLcaLvl = left == null ? 0 : node.computeNCALevel(left);
        int rightLcaLvl = right == null ? 0 : node.computeNCALevel(right);
        //System.out.println("Curt: BinarySLCAFinder leftlcaLvl " + leftLcaLvl + " right " + rightLcaLvl);
        int lcaLvl = (leftLcaLvl >= rightLcaLvl) ? leftLcaLvl : rightLcaLvl; // return the larger/lower level
        return lcaLvl;  
    }
    
    public boolean isTrueAncestor(PathId anc, TreeSet<PathId> descs) {
        PathId next = descs.ceiling(anc);
        if (next == null) 
            return false;
        else 
            return anc != next && anc.isAncestorOrDescendant(next);
    }
}