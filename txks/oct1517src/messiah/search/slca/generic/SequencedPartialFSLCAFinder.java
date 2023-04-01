package messiah.search.slca.generic;

import java.util.Map;
//import java.util.SortedMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import messiah.KeywordInfo;
//import messiah.NodeInfo;
import messiah.utils.MergedSortedMemoryIterator;
import usu.NodeId;
import usu.PathId;
import usu.NodePathId;
import messiah.database.Database;
import usu.algebra.KeywordSearchExpression;

/**
 *
 * @author curtis
 */
public class SequencedPartialFSLCAFinder extends SLCAFinder {
    private boolean verbose = false;

    private final Map<String,KeywordInfo> keywordMap;
    private final Map<PathId,TreeSet> typeMap;
    private final BinarySLCAFinder slcaFinder;
    private final Database db;
    
    public SequencedPartialFSLCAFinder(Database db, BinarySLCAFinder slcaF) {

        this.db = db;
        this.keywordMap = db.keywordIndex;
        this.typeMap = db.typeIndex;
        this.slcaFinder = slcaF;//new BinarySLCAFinder(/*db.getPathUtils()*/);
    }
    
    @Override
    public Set<NodeId> getSLCA(KeywordSearchExpression exp) {
        // results
        String[] query = exp.getKeywordsAsArray();
        Set<NodeId> result = new LinkedHashSet<>();
        
        // get the matches
        KeywordInfo[] infos = new KeywordInfo[query.length];
        TreeSet[] nodeMatches = new TreeSet[query.length];
        TreeSet[] typeMatches = new TreeSet[query.length];
        boolean allHavePath = true;
        for (int i = 0; i < query.length; i++) {
            if (verbose) System.out.println("Curt: SequencedPartialFSLCAFinder doing keyword " + query[i]);
            infos[i] = keywordMap.get(query[i]);
            //System.out.println("Curt: found " + infos[i].getPathIds());
            //System.out.println("Curt: keys " + keywordMap.keySet());
            if (infos[i] == null) return result;
            nodeMatches[i] = infos[i].getNodeIds();
            if (verbose) System.out.println("Curt: SequencedPartialFSLCAFinder nodematches size " + nodeMatches[i].size());
            typeMatches[i] = infos[i].getPathIds();
            if (typeMatches[i].isEmpty()) allHavePath = false;
        }
        
        // compute the list of schema-level SLCA path and the list of nodes with those path
        MergedSortedMemoryIterator pathIter = null;
        NodeId currentNodeInPathIterator = null;
        TreeSet<PathId> pathLcas = null;
        if (allHavePath) {
            if (verbose) System.out.println("Curt: SequencedPartialFSCAFinder  getting pathLCas ");
            pathLcas = new TreeSet<>(this.slcaFinder.getSLCA(typeMatches));
            TreeSet<PathId>[] typeNodesArray = new TreeSet[pathLcas.size()];
            int i = 0;
            for (PathId pathLca : pathLcas) {
                if (verbose) System.out.println("Curt: pathLca " + pathLca + " " + pathLca.debug() + " typeMap " + db.typeIndex.get(pathLca) + " size " + pathLcas.size());
                //for (PathId p: typeMap.keySet()) {
                //     System.out.println("Curt: typeMap " + p + " --> " + typeMap.get(p) + " " + p.debug());
                //}
                //if (!db.typeIndex.containsKey(pathLca)) System.out.println("Does not have key " + pathLca.getClass());
                typeNodesArray[i] = typeMap.get(pathLca);
                i++;
            }
            pathIter = new MergedSortedMemoryIterator(typeNodesArray);
            currentNodeInPathIterator = (NodeId)pathIter.next();
        }

//        sw.start();
        MergedSortedMemoryIterator<NodePathId> nodeIter = new MergedSortedMemoryIterator(nodeMatches);
        NodeId previousCandidate = null;
        while (nodeIter.hasNext()) {
            NodePathId currentNode = nodeIter.next();
            if (verbose) System.out.println("Curt: SequencedPartialFLSCAFinder nodePathId " + currentNode.nodeId + " pathId " + currentNode.pathId);
            int minLcaLvl = currentNode.nodeId.getLevel();
                        //System.out.println("Curt: PartialFLSCAFinder minLcaLvl " + minLcaLvl + " length " + query.length);
            for (int i = 0; i < query.length; i++) {
                if (i != nodeIter.getLastIndex()) {
                    if (verbose) System.out.println("Curt: SeqPar " + i + " " + nodeIter.getLastIndex());
                    NodePathId node1 = nodeIter.peekNext(i);
                    if (node1 != null) if (verbose) System.out.println("Curt: SequencedPartialFLSCAFinder node1 " + node1.nodeId + " " + node1.nodeId.getClass() + " " + node1.pathId);
                    int l1 = node1 == null ? 0 : currentNode.nodeId.computeNCALevel(node1.nodeId);
                    NodePathId node2 = nodeIter.peekLast(i);
                    if (node2 != null) if (verbose) System.out.println("Curt: SequencedPartialFLSCAFinder node2 " + node2.nodeId + " " + node2.nodeId.getClass() + " " + node2.pathId);
                    int l2 = node2 == null ? 0 : currentNode.nodeId.computeNCALevel(node2.nodeId);
                    int l3 = slcaFinder.getLcaLvl(currentNode.pathId, typeMatches[i]);
                    if (verbose) System.out.println("Curt: SequencedPartialFLSCAFinder l1 " + l1 + " l2 " + l2 + " l3 " + l3); // + " node1 " + node1.nodeId + " node2 " + node2.nodeId);
                    // find max l1, l2, l3
                    int l = l1 > l2 ? l1 : l2;
                    l = l > l3 ? l : l3;
                    if (l < minLcaLvl) minLcaLvl = l;
                }
            }
            if (verbose) System.out.println("Curt: Seq " + minLcaLvl);
            if (minLcaLvl <= 0) continue;
            NodeId candidate = currentNode.nodeId.getAncestor(minLcaLvl); // candidate
            /*
            if (allHavePath) {
                PathId candidatePathId = currentNode.pathId.getAncestor(minLcaLvl);
                if (slcaFinder.isTrueAncestor(candidatePathId, pathLcas)) {
                    continue; // discard the candidate
                }
            }
            */
            if (previousCandidate == null || previousCandidate.lessThan(candidate)) {
                if (previousCandidate != null && !previousCandidate.isAncestorOrDescendant(candidate)
                        && (currentNodeInPathIterator == null || !previousCandidate.isAncestorOrDescendant(currentNodeInPathIterator))) {
                    result.add(previousCandidate);
                    if (verbose) System.out.println("previousCandidate = " + previousCandidate);
                }
                /*
                while (allHavePath && currentNodeInPathIterator.lessThan(candidate)) {
                    if (!currentNodeInPathIterator.isAncestorOrDescendant(candidate))
                        result.add(currentNodeInPathIterator);
                    if (pathIter.hasNext())
                        currentNodeInPathIterator = (NodeId)pathIter.next();
                    else allHavePath = false;
                }
                        */
                previousCandidate = candidate;
            }
        }
        
        if (previousCandidate != null && (!allHavePath || previousCandidate.isAncestorOrDescendant(currentNodeInPathIterator))) {
            result.add(previousCandidate);
            if (verbose) System.out.println("previousCandidate2 = " + previousCandidate);
        }
        
        while (allHavePath) {
            if (verbose) System.out.println("Curt: seqIter " + currentNodeInPathIterator);
            result.add(currentNodeInPathIterator);
            if (pathIter.hasNext())
                currentNodeInPathIterator = (NodeId)pathIter.next();
            else allHavePath = false;
        }
//        sw.stop();
//        System.out.println("Iterating = " + sw.readTime() + "ms"); sw.reset();
        return result;
    }
}
