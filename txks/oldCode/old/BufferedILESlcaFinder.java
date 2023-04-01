package messiah.search.slca.old;

import messiah.search.slca.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import messiah.TermNodeKey;
//import messiah.encoding.BinaryDeweyUtils;
import messiah.storage.generic.TermFreqManager;
import messiah.utils.Stopwatch;
import usu.NodeId;
import usu.NodeIdFactory;
import usu.dln.DLNFactory;

/**
 *
 * @author truongbaquan
 */
public class BufferedILESlcaFinder extends SLCAFinder {
    
    public static final int BUFFER_SIZE = 1000;
    
    //private final BinaryDeweyUtils nodeUtils;
    private final TermNodeIndex termNodeIndex;
    private static final NodeIdFactory nodeIdFactory = new DLNFactory();

    public BufferedILESlcaFinder(/*BinaryDeweyUtils nodeUtils, */TermNodeIndex termNodeIndex) {
        //this.nodeUtils = nodeUtils;
        this.termNodeIndex = termNodeIndex;
    }
    
    @Override
    public Set<NodeId> getSLCA(String[] query) {
        System.out.println(this.getClass().getName());
        
        if (query.length == 0) return null;
        
        // retrieve the matches for each keyword and locate the keyword with minimum match size
        Stopwatch sw = new Stopwatch();
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
        try (Cursor cursor = termNodeIndex.openCursor()) {
            String term = query[minIndex]; NodeId nodeId = nodeIdFactory.minId();
            TermNodeKey key = new TermNodeKey(term, nodeId);
            DatabaseEntry keyEntry = new DatabaseEntry();
            DatabaseEntry dataEntry = new DatabaseEntry();
            termNodeIndex.getKeyBinding().objectToEntry(key, keyEntry);
            OperationStatus op = cursor.getSearchKeyRange(keyEntry, dataEntry, LockMode.DEFAULT);
            // main algo
            NodeId lastLca = null;
            ArrayList<NodeId> buffer = new ArrayList<>(BUFFER_SIZE);
            int count = 0;
            while (op == OperationStatus.SUCCESS 
                    && query[minIndex].equals((key = termNodeIndex.getKeyBinding().entryToObject(keyEntry)).getTerm())) {
                nodeId = key.getNodeId();
                buffer.add(nodeId);
                count++;
                if (buffer.size() == BUFFER_SIZE || count == minSize) {
                    for (int i = 0; i < query.length; i++) {
                        if (i != minIndex)
                            buffer = getSLCA(buffer, query[i]);
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
                op = cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT);
            }
            result.add(lastLca);
            
            sw.stop();
            System.out.println("Iterating..." + sw.readTime() + "ms");
        }
        return result;
    }
    
    private ArrayList<NodeId> getSLCA(ArrayList<NodeId> list, String term) {
        ArrayList<NodeId> result = new ArrayList<>(list.size());
        if (TermFreqManager.getFreq(term) == 0) return result;
        NodeId lastLca = nodeIdFactory.minId();
        try (Cursor cursor = termNodeIndex.openCursor()) {
            DatabaseEntry keyEntry = new DatabaseEntry();
            DatabaseEntry dataEntry = new DatabaseEntry();
            for (NodeId node : list) {
                int lcaLvl = 0;
                // get right node
                TermNodeKey keyRight = new TermNodeKey(term, node);
                termNodeIndex.getKeyBinding().objectToEntry(keyRight, keyEntry);
                OperationStatus op = cursor.getSearchKeyRange(keyEntry, dataEntry, LockMode.DEFAULT);
                if (op == OperationStatus.SUCCESS
                        && (keyRight = termNodeIndex.getKeyBinding().entryToObject(keyEntry)).getTerm().equals(term)) {
                    NodeId nodeRight = keyRight.getNodeId();
                    int lcaLvlRight = node.computeNCALevel(nodeRight);
                    if (lcaLvlRight >= lcaLvl) lcaLvl = lcaLvlRight;
                }
                
                // get left node
                TermNodeKey keyLeft;
                if (op == OperationStatus.SUCCESS) // right node is found (potentionally with unexpected term)
                    op = cursor.getPrev(keyEntry, dataEntry, LockMode.DEFAULT);
                else // right node is not found, we are at the last position
                    op = cursor.getLast(keyEntry, dataEntry, LockMode.DEFAULT);
                if (op == OperationStatus.SUCCESS
                        && (keyLeft = termNodeIndex.getKeyBinding().entryToObject(keyEntry)).getTerm().equals(term)) {
                    NodeId nodeLeft = keyLeft.getNodeId();
                    int lcaLvlRight = node.computeNCALevel(nodeLeft);
                    if (lcaLvlRight >= lcaLvl) lcaLvl = lcaLvlRight;
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
        }
        return result;
    }
    
}
