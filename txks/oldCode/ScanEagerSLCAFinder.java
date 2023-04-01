package messiah.search.slca;

import java.util.*;
//import messiah.encoding.BinaryDeweyUtils;
import messiah.storage.generic.TermFreqManager;
import messiah.search.slca.generic.SLCAFinder;
import messiah.utils.Stopwatch;
import usu.NodeId;
import usu.PathId;
import messiah.database.Database;

/**
 *
 * @author truongbaquan
 */
public class ScanEagerSLCAFinder extends SLCAFinder {
    
    public static final int BUFFER_SIZE = 1000;
    
    Database db;
    private final Map<String,TreeMap<NodeId,PathId>> termMap;

    public ScanEagerSLCAFinder(/*BinaryDeweyUtils nodeUtils, */Database db) {
        //this.nodeUtils = nodeUtils;
        this.db = db;
        this.termMap = db.termIndex;
    }
    
    public Set<NodeId> getSLCA(String[] query) {
        //System.out.println(this.getClass()));
        System.out.println("ScanEagerSLCAFinder");
        
        if (query.length == 0) return null;
        
        // retrieve the matches for each keyword and locate the keyword with minimum match size
        Stopwatch sw = new Stopwatch();
        sw.start();
        int minSize = Integer.MAX_VALUE; int minIndex = 0;
        NavigableSet<NodeId>[] sets = new NavigableSet[query.length];
        for (int i = 0; i < query.length; i++) {
            int size = TermFreqManager.getFreq(query[i]);
            if (minSize > size) {
                minIndex = i;
                minSize = size;
            }
            sets[i] = termMap.get(query[i]).navigableKeySet();
        }
        sw.stop();
        System.out.println("Retrieves match sets and cmputing match sizes = " + sw.readTime() + "ms");
        
        sw.start();
        Set<NodeId> result = new LinkedHashSet<>();
        
        NodeId lastLca = null;
        ArrayList<NodeId> buffer = new ArrayList<>(BUFFER_SIZE);
        int count = 0;
        for (NodeId nodeId : sets[minIndex]) {
            buffer.add(nodeId);
            count++;
            if (buffer.size() == BUFFER_SIZE || count == minSize) {
                for (int i = 0; i < query.length; i++) {
                    if (i != minIndex)
                        buffer = getSLCA(buffer, sets[i]);
                }
                int firstIndex = 0;
                if (lastLca != null && buffer.size() != firstIndex 
                            && buffer.get(firstIndex).isAncestorOrDescendant(lastLca))
                        firstIndex++; // ignore first candidate
                    if (lastLca != null && 
                            (buffer.size() == firstIndex || !lastLca.isAncestorOrDescendant(buffer.get(firstIndex))))
                        result.add(lastLca);
                    lastLca = buffer.get(buffer.size() - 1);
                for (int i = firstIndex; i < buffer.size() - 1; i++) {
                    result.add(buffer.get(i));
                }
                buffer.clear();
            }
        }
        if (lastLca != null)
                result.add(lastLca);
        
        sw.stop();
        System.out.println("Iterating..." + sw.readTime() + "ms");
        return result;
    }
    
    private ArrayList<NodeId> getSLCA(ArrayList<NodeId> list1, NavigableSet<NodeId> set2) {
        ArrayList<NodeId> result = new ArrayList<>(list1.size());
        if (set2.isEmpty()) return result;
        NodeId lastLca = null;
        for (NodeId node : list1) {
            int lcaLvl = 0;
            // get right node
            NodeId nodeRight = set2.ceiling(node);
            if (nodeRight != null) {
                int lcaLvlRight = node.computeNCALevel(nodeRight);
                if (lcaLvlRight >= lcaLvl) lcaLvl = lcaLvlRight;
            }
            
            // get left node
            NodeId nodeLeft = set2.floor(node);
            if (nodeLeft != null) {
                int lcaLvlLeft = node.computeNCALevel(nodeLeft);
                if (lcaLvlLeft >= lcaLvl) lcaLvl = lcaLvlLeft;
            }
            
            NodeId lca = node.getAncestor(lcaLvl);
            
            if (lastLca.lessThanOrEqualTo(lca)) {
                if (lastLca != null && !lastLca.isAncestorOrDescendant(lca))
                    result.add(lastLca);
                lastLca = lca;
            }           
        }
        if (lastLca != null)
            result.add(lastLca);
        return result;
    }
    
}
