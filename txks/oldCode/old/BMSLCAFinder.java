/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messiah.search.slca.old;

import messiah.search.slca.generic.*;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import java.util.LinkedHashSet;
import java.util.Set;
import messiah.TermNodeKey;
//import messiah.encoding.BinaryDeweyUtils;
import messiah.storage.generic.TermFreqManager;
import messiah.storage.TypeSLCAFinder;
import messiah.utils.Stopwatch;
import usu.NodeId;
import usu.PathId;
import usu.NodeIdFactory;
import messiah.database.Database;
//import usu.dln.DLNFactory;

/**
 *
 * @author truongbaquan
 */
public class BMSLCAFinder<T> extends SLCAFinder {
    //private final BinaryDeweyUtils nodeUtils;
    private final Database db;
    private final TypeSLCAFinder typeSLCAFinder;
    private final NodeIdFactory nodeIdFactory;
    
    public BMSLCAFinder(/*BinaryDeweyUtils nodeUtils, */Database db, TypeSLCAFinder typeSLCAFinder, T nodeIdFactory) {
        //this.nodeUtils = nodeUtils;
        this.db = db;
        this.typeSLCAFinder = typeSLCAFinder;
        this.nodeIdFactory = (NodeIdFactory)nodeIdFactory;
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
        // read the first match whose total size is minimum
        String term = query[minIndex]; 
        NodeId nodeId = nodeIdFactory.minId();
        TermNodeKey key = new TermNodeKey(term, nodeId);
        
        // main algo
        NodeId lastLca = null;
        boolean lastIsSLCA = false;
        //int count = 0;
        while (op == OperationStatus.SUCCESS 
                && query[minIndex].equals((key = termNodeIndex.getKeyBinding().entryToObject(keyEntry)).getTerm())) {
            nodeId = key.getNodeId();
            NodeId lca;
            boolean isSLCA;
            if (query.length == 1) {
                lca = nodeId;
                isSLCA = true;
            } else {
                PathId pathId = termNodeIndex.getValueBinding().entryToObject(dataEntry);
                //count++;
                int[] slcaLvls = new int[query.length];
                int[] fslcaLvls = new int[query.length];
                for (int i = 0; i < query.length; i++) {
                    if (i == minIndex) continue;
                    slcaLvls[i] = 0;
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
                    // get type node
                    int typeLcaLvl = typeSLCAFinder.getSLCALvl(pathId, query[i]);

                    fslcaLvls[i] = Math.max(slcaLvls[i], typeLcaLvl);
                }
                int slcaLvl = Integer.MAX_VALUE;
                int fslcaLvl = Integer.MAX_VALUE;
                for (int i = 0; i < query.length; i++) {
                    if (i == minIndex) continue;
                    if (slcaLvl >= slcaLvls[i]) slcaLvl = slcaLvls[i];
                    if (fslcaLvl >= fslcaLvls[i]) fslcaLvl = fslcaLvls[i];
                }
                lca = nodeId.getAncestor(fslcaLvl);
                isSLCA = (fslcaLvl == slcaLvl);
            }

            if (lastLca.lessThanOrEqualTo(lca)) {
                if (lastLca != null && !lastLca.isAncestorOrDescendant(lca) && lastIsSLCA)
                    result.add(lastLca);
                lastLca = lca;
                lastIsSLCA = isSLCA;
            }
        }
        if (lastLca != null && lastIsSLCA)
            result.add(lastLca);
        
        sw.stop();
        System.out.println("Iterating..." + sw.readTime() + "ms");
        return result;
    }
}
