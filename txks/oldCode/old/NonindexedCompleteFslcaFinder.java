package messiah.search.slca.old;

import messiah.search.slca.*;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import java.io.Closeable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
//import messiah.encoding.BinaryDeweyUtils;
import messiah.storage.Index;
import messiah.storage.TermFreqManager;
import messiah.storage.TermIndex;
import messiah.storage.TypeSLCAFinder;
import messiah.utils.Stopwatch;
import usu.NodeId;
import usu.PathId;
import usu.NodeIdFactory;
import usu.dln.DLNFactory;

/**
 *
 * @author truongbaquan
 */
public class NonindexedCompleteFslcaFinder extends SLCAFinder {
    
    //private final BinaryDeweyUtils nodeUtils;
    private final StoredMap<String,TreeMap> termMap;
    private final TypeSLCAFinder typeSLCAFinder;
    private static final NodeIdFactory nodeIdFactory = new DLNFactory();
    
    public NonindexedCompleteFslcaFinder(/*BinaryDeweyUtils nodeUtils, */TermIndex termIndex, TypeSLCAFinder typeSLCAFinder) {
        //this.nodeUtils = nodeUtils;
        this.termMap = termIndex.wrapUnsortedMap(false);
        this.typeSLCAFinder = typeSLCAFinder;
    }
    
    @Override
    public Set<NodeId> getSLCA(String[] query) {
        System.out.println(this.getClass().getName());
        
        if (query.length == 0) return null;
        
        // retrieve the matches for each keyword and locate the keyword with minimum match size
        Stopwatch sw = new Stopwatch();
        Stopwatch slcaSw = new Stopwatch();
        Stopwatch typeLcaSw = new Stopwatch();
        Stopwatch maxSw = new Stopwatch();
        Stopwatch pruneSw = new Stopwatch();
        sw.start();
        int minSize = Integer.MAX_VALUE; int minIndex = 0;
        for (int i = 0; i < query.length; i++) {
            if (typeSLCAFinder.isTypeLabel(query[i])) continue;
            int size = TermFreqManager.getFreq(query[i]);
            if (minSize > size) {
                minIndex = i;
                minSize = size;
            }
        }
        if (minSize == Integer.MAX_VALUE) {
            for (int i = 0; i < query.length; i++) {
                int size = TermFreqManager.getFreq(query[i]); // omit the type label condition
                if (minSize > size) {
                    minIndex = i;
                    minSize = size;
                }
            }
        }
        TreeMap<NodeId,PathId>[] maps = new TreeMap[query.length];
        for (int i = 0; i < query.length; i++) {
            maps[i] = termMap.get(query[i]);
        }
        sw.stop();
        System.out.println("Computing the match size = " + sw.readTime() + "ms");
        sw.reset();
        
        sw.start();
        Set<NodeId> result = new LinkedHashSet<>();
        int count = 0;
        String term = query[minIndex]; NodeId nodeId = nodeIdFactory.minId();
        // main algo
        NodeId lastLca = null;
        boolean lastIsSLCA = false;
        for (Map.Entry<NodeId,PathId> entry : maps[minIndex].entrySet()) {
            nodeId = entry.getKey();
            NodeId lca;
            boolean isSLCA;
            if (query.length == 1) {
                lca = nodeId;
                isSLCA = true;
            } else {
                PathId pathId = entry.getValue();
                int[] slcaLvls = new int[query.length];
                int[] fslcaLvls = new int[query.length];
                for (int i = 0; i < query.length; i++) {
                    if (i == minIndex) continue;
                    count++;
                    slcaLvls[i] = 0;
                    TreeMap<NodeId,PathId> localMap = maps[i];
                    slcaSw.start();
                    NodeId nodeRight = localMap.ceilingKey(nodeId);
                    if (nodeRight != null) {
                        int lcaLvlRight = nodeId.computeNCALevel(nodeRight);
                        if (lcaLvlRight >= slcaLvls[i]) slcaLvls[i] = lcaLvlRight;
                    }
                    
                    NodeId nodeLeft = localMap.floorKey(nodeId);
                    if (nodeLeft != null) {
                        int lcaLvlLeft = nodeId.computeNCALevel(nodeLeft);
                        if (lcaLvlLeft >= slcaLvls[i]) slcaLvls[i] = lcaLvlLeft;
                    }
                    slcaSw.stop();
                    // get type node
                    typeLcaSw.start();
                    int typeLcaLvl = typeSLCAFinder.getSLCALvl(pathId, query[i]);
                    fslcaLvls[i] = Math.max(slcaLvls[i], typeLcaLvl);
                    typeLcaSw.stop();
                }
                maxSw.start();
                int slcaLvl = Integer.MAX_VALUE;
                int fslcaLvl = Integer.MAX_VALUE;
                for (int i = 0; i < query.length; i++) {
                    if (i == minIndex) continue;
                    if (slcaLvl >= slcaLvls[i]) slcaLvl = slcaLvls[i];
                    if (fslcaLvl >= fslcaLvls[i]) fslcaLvl = fslcaLvls[i];
                }
                lca = nodeId.getAncestor(fslcaLvl);
                isSLCA = (fslcaLvl == slcaLvl);
                maxSw.stop();
            }
            pruneSw.start();
            if (lastLca.lessThanOrEqualTo(lca)) {
                if (!lastLca.isAncestorOrDescendant(lca) && lastIsSLCA)
                    result.add(lastLca);
                lastLca = lca;
                lastIsSLCA = isSLCA;
            }   
            pruneSw.stop();
        }
        if (lastIsSLCA)
            result.add(lastLca);
        sw.stop();
        System.out.println("Iterating..." + sw.readTime() + "ms");
        System.out.println("slcaSw.readAverageTime() = " + slcaSw.readAverageTime() + "ms");
        System.out.println("typeLcaSw.readAverageTime() = " + typeLcaSw.readAverageTime() + "ms");
        System.out.println("maxSw.readAverageTime() = " + maxSw.readAverageTime());
        System.out.println("pruneSw.readAverageTime() = " + pruneSw.readAverageTime());
        System.out.println("count = " + count);
        return result;
    }
    
    private class CursorManager implements Closeable {
        private Cursor[] cursors;
        private DatabaseEntry[] keyEntries;
        private DatabaseEntry[] valueEntries;
        
        public CursorManager(Index index, int n) {
            this.cursors = new Cursor[n];
            this.keyEntries = new DatabaseEntry[n];
            this.valueEntries = new DatabaseEntry[n];
            open(index);
            for (int i = 0; i < n; i++) {
                keyEntries[i] = new DatabaseEntry();
                valueEntries[i] = new DatabaseEntry();
            }
        }
        
        private void open(Index index) {
            for (int i = 0; i < cursors.length;i++) {
                cursors[i] = index.openCursor();
            }
        }
        
        @Override
        public void close() {
            for (int i = 0; i < cursors.length;i++) {
                cursors[i].close();
            }
        }
        
        public Cursor getCursor(int i) {
            return cursors[i];
        }
        
        public DatabaseEntry getKeyEntry(int i) {
            return keyEntries[i];
        }
        
        public DatabaseEntry getValueEntry(int i) {
            return valueEntries[i];
        }
    }
    
}
