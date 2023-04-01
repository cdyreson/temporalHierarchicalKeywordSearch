package messiah.search.slca.generic;

import java.util.Map;
import java.util.SortedMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import messiah.KeywordInfo;
import messiah.NodeInfo;
import messiah.utils.MergedSortedMemoryIterator;
import usu.NodeId;
import usu.PathId;
import usu.NodePathId;
import messiah.database.Database;
import usu.algebra.KeywordSearchExpression;

/**
 *
 * @author truongbaquan
 */
public class PartialFSLCAFinder extends SLCAFinder {
    private boolean verbose = false;

    private final Map<String,KeywordInfo> keywordMap;
    //private final SortedMap<NodeId,NodeInfo> nodeMap;
    private final Map<PathId,TreeSet> typeMap;
    private final BinarySLCAFinder slcaFinder;
    private Database db;
    
    public PartialFSLCAFinder(Database db, BinarySLCAFinder slcaF) {

        this.db = db;
        this.keywordMap = db.keywordIndex;
        //this.nodeMap = db.nodeIndex;
        this.typeMap = db.typeIndex;
        this.slcaFinder = slcaF;//new BinarySLCAFinder(/*db.getPathUtils()*/);
    }
    
    @Override
    public Set<NodeId> getSLCA(KeywordSearchExpression exp) {
        String[] query = exp.getKeywordsAsArray();
        System.out.println(this.getClass().getName());
        
        // results
        Set<NodeId> result = new LinkedHashSet<>();
        
        // get the matches
        KeywordInfo[] infos = new KeywordInfo[query.length];
        TreeSet[] nodeMatches = new TreeSet[query.length];
        TreeSet[] typeMatches = new TreeSet[query.length];
        boolean allHasPath = true;
        for (int i = 0; i < query.length; i++) {
            if (verbose) System.out.println("Curt: PartialFSLCAFinder doing keyword " + query[i]);
            infos[i] = keywordMap.get(query[i]);
            //System.out.println("Curt: keys " + keywordMap.keySet());
            if (infos[i] == null) return result;
            if (verbose) System.out.println("Curt: found " + infos[i].getPathIds());
            nodeMatches[i] = infos[i].getNodeIds();
            typeMatches[i] = infos[i].getPathIds();
            if (typeMatches[i].isEmpty()) allHasPath = false;
        }
        
        // compute the list of schema-level SLCA path and the list of nodes with those path
        MergedSortedMemoryIterator pathIter = null;
        NodeId curPathIter = null;
        TreeSet<PathId> pathLcas = null;
        if (allHasPath) {
            if (verbose) System.out.println("Curt: PartialFSCAFinder  getting pathLCas ");
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
            curPathIter = (NodeId)pathIter.next();
        }

//        sw.start();
        MergedSortedMemoryIterator<NodePathId> nodeIter = new MergedSortedMemoryIterator(nodeMatches);
        NodeId lastCand = null;
        while (nodeIter.hasNext()) {
            NodePathId curNode = nodeIter.next();
            //System.out.println("Curt: PartialFLSCAFinder nodePathId " + curNode.nodeId + " pathId " + curNode.pathId);
//            int minLcaLvl = db.getPath(curNode.pathId).getInfo().getLevel();
            int minLcaLvl = curNode.nodeId.getLevel();
                        //System.out.println("Curt: PartialFLSCAFinder minLcaLvl " + minLcaLvl + " length " + query.length);
            for (int i = 0; i < query.length; i++) {
                if (i != nodeIter.getLastIndex()) {
                    NodePathId node1 = nodeIter.peekNext(i);
                    //if (node1 != null) System.out.println("Curt: PartialFLSCAFinder node1 " + node1.nodeId);
                    int l1 = node1 == null ? l1 = 0 :
                            curNode.nodeId.computeNCALevel(node1.nodeId);
                    NodePathId node2 = nodeIter.peekLast(i);
                    //if (node2 != null) System.out.println("Curt: PartialFLSCAFinder node2 " + node2.nodeId);
                    int l2 = node2 == null ? l2 = 0 : 
                            curNode.nodeId.computeNCALevel(node2.nodeId);
                    int l3 = slcaFinder.getLcaLvl(curNode.pathId, typeMatches[i]);
                    //System.out.println("Curt: PartialFLSCAFinder l1 " + l1 + " l2 " + l2 + " l3 " + l3); // + " node1 " + node1.nodeId + " node2 " + node2.nodeId);
                    // find max l1, l2, l3
                    int l = l1 > l2 ? l1 : l2;
                    l = l > l3 ? l : l3;
                    if (l < minLcaLvl) minLcaLvl = l;
                }
            }
            if (minLcaLvl <= 0) continue;
            NodeId cand = curNode.nodeId.getAncestor(minLcaLvl); // candidate
            if (allHasPath) {
                PathId candPathId = curNode.pathId.getAncestor(minLcaLvl);
                if (slcaFinder.isTrueAncestor(candPathId, pathLcas)) {
                    continue; // discard the candidate
                }
            }
            
            if (lastCand == null || lastCand.lessThan(cand)) {
                if (lastCand != null && !lastCand.isAncestorOrDescendant(cand)
                        && (curPathIter == null || !lastCand.isAncestorOrDescendant(curPathIter))) {
                    result.add(lastCand);
//                    System.out.println("lastCand = " + lastCand);
                }
                while (allHasPath && curPathIter.lessThan(cand)) {
                    if (!curPathIter.isAncestorOrDescendant(cand))
                        result.add(curPathIter);
                    if (pathIter.hasNext())
                        curPathIter = (NodeId)pathIter.next();
                    else allHasPath = false;
                }
                lastCand = cand;
            }
        }
        if (lastCand != null && (!allHasPath || lastCand.isAncestorOrDescendant(curPathIter))) {
            result.add(lastCand);
//            System.out.println("lastCand = " + lastCand);
        }
        while (allHasPath) {
            result.add(curPathIter);
            if (pathIter.hasNext())
                curPathIter = (NodeId)pathIter.next();
            else allHasPath = false;
        }
//        sw.stop();
//        System.out.println("Iterating = " + sw.readTime() + "ms"); sw.reset();
        return result;
    }
}
