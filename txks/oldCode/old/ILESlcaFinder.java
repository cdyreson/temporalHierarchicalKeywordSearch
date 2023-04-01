package messiah.search.slca.old;

import messiah.search.slca.*;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import java.util.LinkedHashSet;
import java.util.Set;
import messiah.TermNodeKey;
//import messiah.encoding.BinaryDeweyUtils;
import messiah.storage.TermFreqManager;
import messiah.storage.TermNodeIndex;
import messiah.utils.Stopwatch;
import usu.NodeId;
import usu.NodeIdFactory;
import usu.dln.DLNFactory;

/**
 *
 * @author truongbaquan
 */
public class ILESlcaFinder extends SLCAFinder {
    //private final BinaryDeweyUtils nodeUtils;
    private final TermNodeIndex termNodeIndex;
    private static final NodeIdFactory nodeIdFactory = new DLNFactory();
    
    public ILESlcaFinder(/*BinaryDeweyUtils nodeUtils, */TermNodeIndex termNodeIndex) {
        //this.nodeUtils = nodeUtils;
        this.termNodeIndex = termNodeIndex;
    }
    
    @Override
    public Set<NodeId> getSLCA(String[] query) {
        System.out.println(this.getClass().getName());
        
        if (query.length == 0) return null;
        
        // retrieve the matches for each keyword and locate the keyword with minimum match size
        Stopwatch sw = new Stopwatch();
        Stopwatch slcaSw = new Stopwatch();
        Stopwatch maxSw = new Stopwatch();
        Stopwatch pruneSw = new Stopwatch();
        sw.start();
        int minSize = Integer.MAX_VALUE; int minIndex = 0;
        for (int i = 0; i < query.length; i++) {
            int size = TermFreqManager.getFreq(query[i]);
            if (minSize > size) {
                minIndex = i;
                minSize = size;
            }
        }
        sw.stop();
        System.out.println("Computing the match size = " + sw.readTime() + "ms");
        
        sw.start();
        Set<NodeId> result = new LinkedHashSet<>();
        // read the first match whose total size is minimum
        Cursor cursor = termNodeIndex.openCursor();
        String term = query[minIndex]; NodeId nodeId = nodeIdFactory.minId();
        TermNodeKey key = new TermNodeKey(term, nodeId);
        DatabaseEntry keyEntry = new DatabaseEntry();
        DatabaseEntry dataEntry = new DatabaseEntry();
        termNodeIndex.getKeyBinding().objectToEntry(key, keyEntry);
        OperationStatus op = cursor.getSearchKeyRange(keyEntry, dataEntry, LockMode.DEFAULT);
        // main algo
        NodeId lastLca = null;
        int count = 0;
        while (op == OperationStatus.SUCCESS 
                && query[minIndex].equals((key = termNodeIndex.getKeyBinding().entryToObject(keyEntry)).getTerm())) {
            nodeId = key.getNodeId();
            NodeId lca;
            if (query.length == 1) {
                lca = nodeId;
            } else {
                int[] slcaLvls = new int[query.length];
                for (int i = 0; i < query.length; i++) {
                    if (i == minIndex) continue;
                    slcaLvls[i] = 0;
                    count++;
                    slcaSw.start();
                    // get right node
                    Cursor localCursor = termNodeIndex.openCursor();
                    DatabaseEntry localKeyEntry = new DatabaseEntry();
                    DatabaseEntry localDataEntry = new DatabaseEntry();
                    TermNodeKey keyRight = new TermNodeKey(query[i], nodeId);
                    termNodeIndex.getKeyBinding().objectToEntry(keyRight, localKeyEntry);
                    OperationStatus localOp = localCursor.getSearchKeyRange(localKeyEntry, localDataEntry, LockMode.DEFAULT);
                    if (localOp == OperationStatus.SUCCESS
                            && (keyRight = termNodeIndex.getKeyBinding().entryToObject(localKeyEntry)).getTerm().equals(query[i])) {
                        NodeId nodeRight = keyRight.getNodeId();
                        int lcaLvlRight = nodeId.computeNCALevel(nodeRight);
                        if (lcaLvlRight >= slcaLvls[i]) slcaLvls[i] = lcaLvlRight;
                    }
                    // get left node
                    TermNodeKey keyLeft;
                    if (localOp == OperationStatus.SUCCESS) // right node is found (potentionally with unexpected term)
                        localOp = localCursor.getPrev(localKeyEntry, localDataEntry, LockMode.DEFAULT);
                    else // right node is not found, we are at the last position
                        localOp = localCursor.getLast(localKeyEntry, localDataEntry, LockMode.DEFAULT);
                    if (localOp == OperationStatus.SUCCESS
                            && (keyLeft = termNodeIndex.getKeyBinding().entryToObject(localKeyEntry)).getTerm().equals(query[i])) {
                        NodeId nodeLeft = keyLeft.getNodeId();
                        int lcaLvlLeft = nodeId.computeNCALevel(nodeLeft);
                        if (lcaLvlLeft >= slcaLvls[i]) slcaLvls[i] = lcaLvlLeft;
                    }
                    slcaSw.stop();
                    localCursor.close();
                }
                maxSw.start();
                int slcaLvl = Integer.MAX_VALUE;
                for (int i = 0; i < query.length; i++) {
                    if (i == minIndex) continue;
                    if (slcaLvl >= slcaLvls[i]) slcaLvl = slcaLvls[i];
                }
                lca = nodeId.getAncestor(slcaLvl);
                maxSw.stop();
            }
            
            pruneSw.start();
            if (lastLca.lessThanOrEqualTo(lca)) {
                if (lastLca != null && !lastLca.isAncestorOrDescendant(lca))
                    result.add(lastLca);
                lastLca = lca;
            }   
            op = cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT);
            pruneSw.stop();
        }
        cursor.close();
        if (lastLca != null)
            result.add(lastLca);
        
        sw.stop();
        System.out.println("Iterating..." + sw.readTime() + "ms");
        System.out.println("slcaSw.readAverageTime() = " + slcaSw.readAverageTime() + "ms");
        System.out.println("maxSw.readAverageTime() = " + maxSw.readAverageTime());
        System.out.println("pruneSw.readAverageTime() = " + pruneSw.readAverageTime());
        System.out.println("count = " + count);
        return result;
    }
}
