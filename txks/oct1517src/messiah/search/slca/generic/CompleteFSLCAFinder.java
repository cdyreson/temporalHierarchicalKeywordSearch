package messiah.search.slca.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import messiah.KeywordInfo;
import usu.NodePathId;
import messiah.Path;
import messiah.storage.generic.DbAccess;
import messiah.storage.generic.TermFreqManager;
import messiah.storage.TypeSLCAFinder;
import messiah.utils.MergedSortedMemoryIterator;
import messiah.utils.Stopwatch;
import usu.NodeId;
import usu.PathId;
import java.util.Map;
import java.util.SortedMap;
import messiah.database.Database;
import messiah.TermNodeKey;
import usu.algebra.KeywordSearchExpression;

/**
 *
 * @author truongbaquan
 */
public class CompleteFSLCAFinder extends SLCAFinder {
    
    private final DbAccess db;
    private final BinarySLCAFinder pathSLCAFinder;
    private final SortedMap<TermNodeKey,PathId> termNodeIndex;
    private final Map<String,SortedMap<NodeId,PathId>> termMap;
    private final Map<String,KeywordInfo> keywordMap;
    private final TypeSLCAFinder typeSLCAFinder;
    private final boolean indexed;
    
    public CompleteFSLCAFinder(DbAccess db, Database bdb, 
            TypeSLCAFinder typeSLCAFinder, BinarySLCAFinder pathF, boolean indexed) {
        this.db = db;
        this.pathSLCAFinder = pathF;
        this.termNodeIndex = bdb.termNodeIndex;
        this.termMap = bdb.termIndex;
        this.keywordMap = bdb.keywordIndex;
        this.typeSLCAFinder = typeSLCAFinder;
        this.indexed = indexed;
    }
    
    @Override
    public Set<NodeId> getSLCA(KeywordSearchExpression exp) {
        // results
        String[] query = exp.getKeywordsAsArray();
        System.out.println(this.getClass().getName());
        
        if (query.length == 0) return null;
        
        // retrieve the matches for each keyword and locate the keyword with minimum match size
        Stopwatch sw = new Stopwatch();
        Stopwatch slcaSw = new Stopwatch();
        Stopwatch typeLcaSw = new Stopwatch();
        Stopwatch pruneSw = new Stopwatch();
        sw.start();
        // find the query keyword with fewest matches with preference to keywords without type matches
        int minSize = Integer.MAX_VALUE; int minIndex = 0; boolean allKeywordsAreType = false;
        for (int i = 0; i < query.length; i++) {
            if (typeSLCAFinder.isTypeLabel(query[i])) continue; // search for keywords without type matches first
            int size = TermFreqManager.getFreq(query[i]);
            if (minSize > size) {
                minIndex = i;
                minSize = size;
            }
        }
        // are keywords without type matches found?
        if (minSize == Integer.MAX_VALUE) {
            // No. Search again
            allKeywordsAreType = true;
            for (int i = 0; i < query.length; i++) {
                int size = TermFreqManager.getFreq(query[i]); // omit the type match condition
                if (minSize > size) {
                    minIndex = i;
                    minSize = size;
                }
            }
        }
        sw.stop();
        System.out.println("Computing the match size = " + sw.readTime() + "ms");
        System.out.println("Anchor keyword: " + query[minIndex]);
        
        FSLCADescFilter fslcaDescFilter = new FSLCADescFilter();
        if (allKeywordsAreType) {
            fslcaDescFilter.initialize(query, minIndex);
        }
        
        sw.start();
        Set<NodeId> result = new LinkedHashSet<>();
        try (MatchAccessor matchAccessor = createMatchAccessor(indexed, query)) {
            // main algo
            NodeId nodeId;
            NodeId lastSlca = null;
            int lastSlcaLvl = -1;
            boolean lastIsFSLCA = false;
            MatchAccessor.MatchIterator matchIterator = matchAccessor.getMatchIterator(minIndex);
            while (matchIterator.hasNext()) {
                nodeId = matchIterator.next();
                NodeId slca;
                int slcaLvl;
                boolean isFSLCA = true;
                if (query.length == 1) {
                    slca = nodeId;
                    slcaLvl = nodeId.getLevel();
                } else {
                    // read path ID of the current node
                    PathId pathId = matchIterator.getPathId();
                    
                    slcaSw.start();
                    // compute the SLCA level of the current nodes with matches of other keywords
                    int[] slcaLvls = new int[query.length];
                    for (int i = 0; i < query.length; i++) {
                        if (i == minIndex) continue;
                        slcaLvls[i] = 0;
                        NodeId[] prevNextId = matchAccessor.getPrevNext(nodeId, i);
                        if (prevNextId[1] != null) {
                            int lcaLvlRight = nodeId.computeNCALevel(prevNextId[1]);
                            if (lcaLvlRight >= slcaLvls[i]) slcaLvls[i] = lcaLvlRight;
                        }
                        if (prevNextId[0] != null) {
                            int lcaLvlLeft = nodeId.computeNCALevel(prevNextId[0]);
                            if (lcaLvlLeft >= slcaLvls[i]) slcaLvls[i] = lcaLvlLeft;
                        }
                    }
                    slcaLvl = Integer.MAX_VALUE;
                    for (int i = 0; i < query.length; i++) {
                        if (i == minIndex) continue;
                        if (slcaLvl >= slcaLvls[i]) slcaLvl = slcaLvls[i];
                    }
                    slcaSw.stop();
                    
                    // check whether the path ID of the SLCA is an ancestor of a path-SLCA; if yes, it is not an FSLCA
                    PathId slcaPathId = pathId.getAncestor(slcaLvl);
                    
                    isFSLCA = !fslcaDescFilter.checkByPath(slcaPathId); // is not an FSLCA if having a descedant FSLCA
                    
                    if (isFSLCA) {
                        typeLcaSw.start();
                        int[] fslcaLvls = new int[query.length];
                        for (int i = 0; i < query.length; i++) {
                            // get type node
                            int typeLcaLvl = typeSLCAFinder.getSLCALvl(pathId, query[i]);
                            fslcaLvls[i] = Math.max(slcaLvls[i], typeLcaLvl);
                        }
                        typeLcaSw.stop();
                        
                        int fslcaLvl = Integer.MAX_VALUE;
                        for (int i = 0; i < query.length; i++) {
                            if (i == minIndex) continue;
                            if (fslcaLvl >= fslcaLvls[i]) fslcaLvl = fslcaLvls[i];
                        }
                        isFSLCA = (fslcaLvl == slcaLvl);
                    }
                    slca = nodeId.getAncestor(slcaLvl);
                }
                
                pruneSw.start();
                if (lastSlca.lessThanOrEqualTo(slca)) {
                    if (lastIsFSLCA && !lastSlca.isAncestorOrDescendant(slca)) {
                        if (!fslcaDescFilter.checkByNode(lastSlca, lastSlcaLvl))
                            result.add(lastSlca);
                    }
                    lastSlca = slca;
                    lastSlcaLvl = slcaLvl;
                    lastIsFSLCA = isFSLCA;
                }   
                pruneSw.stop();
            }
            if (lastIsFSLCA && !fslcaDescFilter.checkByNode(lastSlca, lastSlcaLvl))
                result.add(lastSlca);
        }
        sw.stop();
        System.out.println("Iterating..." + sw.readTime() + "ms");
        System.out.println("slcaSw.readAverageTime() = " + slcaSw.readAverageTime() + "ms");
        System.out.println("typeLcaSw.readAverageTime() = " + typeLcaSw.readAverageTime() + "ms");
        System.out.println("pruneSw.readAverageTime() = " + pruneSw.readAverageTime());
        return result;
    }
    
    private MatchAccessor createMatchAccessor(boolean indexed, String[] query) {
        if (indexed) {
            return new IndexedMatchAccessor(query, termNodeIndex);
        } else {
            return new NonIndexedMatchAccessor(query, termMap);
        }
    }
    
    /**
     * This private class is used to check whether a node has descendant FSLCA node
     */
    private class FSLCADescFilter {
        private boolean initialized = false;
        
        private long allBitMap;
        private HashMap<PathId,Long> pathDescTermBitMap;
        TreeSet<PathId> pathLcas = null;
        MergedSortedMemoryIterator<NodePathId> nodeIter = null;
        NodePathId nodeIterCursor = null;
        
        public void initialize(String[] query, int minIndex) {
            TreeSet[] typeMatches = new TreeSet[query.length];
            TreeSet[] nodeMatches = new TreeSet[query.length];
            for (int i = 0; i < query.length; i++) {
                ArrayList<Path> paths = typeSLCAFinder.getPaths(query[i]);
                typeMatches[i] = new TreeSet<>();
                for (Path path : paths) {
                    typeMatches[i].add(path.getPathId());
                }
                if (i == minIndex) {
                    nodeMatches[i] = new TreeSet<>(); // empty
                } else {
                    nodeMatches[i] = keywordMap.get(query[i]).getNodeIds();
                }
            }
            pathLcas = new TreeSet<>(pathSLCAFinder.getSLCA(typeMatches));
            nodeIter = new MergedSortedMemoryIterator(nodeMatches);
            nodeIterCursor = nodeIter.next();
            
            //allBitMap = PathId.POWER_OF_2[query.length] - 1;
            pathDescTermBitMap = new HashMap<>(db.getPaths().size());
            for (Path path : db.getPaths()) {
                long bitMap = 0;
                Set<String> descTerms = typeSLCAFinder.getDescTermSet(path.getPathId());
                for (int i = 0; i < query.length; i++) {
                    if (descTerms.contains(query[i])) {
                        bitMap |= (1 << i);
                    }
                }
                pathDescTermBitMap.put(path.getPathId(), bitMap);
            }
            initialized = true;
        }
        
        public boolean checkByPath(PathId pathId) {
            if (!initialized) return false;
            return pathSLCAFinder.isTrueAncestor(pathId, pathLcas);
        }
        
        public boolean checkByNode(NodeId nodeId, int nodeLvl) {
            if (!initialized) return false;
            while (nodeIterCursor != null && nodeIterCursor.nodeId.lessThanOrEqualTo(nodeId)) {
                nodeIterCursor = nodeIter.next(); // advance the cursor
            }
            NodeId upperBound = nodeId.getNextFirstDescendant(nodeLvl);
            long curBit = 0;
            NodeId lastChild = null;
            while (nodeIterCursor != null && nodeIterCursor.nodeId.lessThan(upperBound)) {
                assert nodeId.isAncestorOrDescendant(nodeIterCursor.nodeId);
                NodeId curChild = nodeIterCursor.nodeId.getAncestor(nodeLvl+1);
                assert lastChild.lessThan(curChild);
                if (curChild != lastChild) {
                    PathId childPathId = nodeIterCursor.pathId.getAncestor(nodeLvl+1);
                    curBit = pathDescTermBitMap.get(childPathId);
                    lastChild = curChild;
                }
                curBit |= 1 << nodeIter.getLastIndex();
                if (curBit == allBitMap) {
                    return true;
                }
                nodeIterCursor = nodeIter.next();
            }
            return false;
        }
    }
}