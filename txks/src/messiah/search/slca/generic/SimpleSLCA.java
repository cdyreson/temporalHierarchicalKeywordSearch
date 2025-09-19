package messiah.search.slca.generic;

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
//import java.util.List;
import java.util.Map;
import java.util.Set;
//import java.util.TreeSet;
//import messiah.KeywordInfo;
import messiah.database.Database;
//import messiah.database.berkeleydb.SplitIterator;
//import messiah.database.berkeleydb.SplitMapKeywordNodeIndex;
import messiah.search.slca.generic.feed.SimpleCachingFeedGroup;
import messiah.search.slca.generic.feed.SimpleCachingFeedsController;
//import messiah.utils.Stopwatch;
import usu.NodeId;
import usu.PathId;
import usu.algebra.KeywordSearchExpression;

/**
 * This is the SLCA finder for nontemporal search.
 *
 * @author curtis
 */
public class SimpleSLCA extends SLCAFinder {

    private final Database db;
    private final SimpleCachingFeedsController controller;
    int shift[];
    Map<Integer, Integer> lcaLevelMap;
    int maxLevel = 0;
    boolean verbose = true;

    public SimpleSLCA(Database db) {
        this.db = db;
        controller = new SimpleCachingFeedsController();
        lcaLevelMap = new HashMap();
    }

    public void createLCAs(short current, short dimensions, PathId pathLCA, int hashKey, Set<PathId>[] paths) {
        // Reached the end
        if (current >= dimensions) {
            //System.out.println("level is " + pathLCA.getLevel());
            int level = pathLCA.getLevel() - 1;
            lcaLevelMap.put(hashKey, level);
            maxLevel = (level > maxLevel) ? level : maxLevel;
            return;
        }

        // Process this path
        int count = 0;
        for (PathId pathId : paths[current]) {

            //for (int i = 0; i < current; i++) {
            //    System.out.print("        ");
            //}
            //System.out.println("pathLCA " + pathLCA + " id " + pathId);
            int level = (pathLCA == null) ? pathId.getLevel() : pathId.computeNCALevel(pathLCA);
            PathId newPathLCA = pathId.getAncestor(level);
            int newHashKey = hashKey + (count++ << shift[current]);
            short nextCurrent = current;
            createLCAs(++nextCurrent, dimensions, newPathLCA, newHashKey, paths);
        }
    }
    /*
     Fetch the current SLCAs
     */

    @Override
    public Set<NodeId> getSLCA(KeywordSearchExpression exp) {
        // results
        String[] query = exp.getKeywordsAsArray();
        //Stopwatch stopwatch = new Stopwatch();
        //stopwatch.start();
        Set<NodeId> result = new LinkedHashSet<>();
        short dimensions = (short) query.length;
        Set<PathId>[] paths = new HashSet[dimensions];

        if (verbose) System.out.println("SimpleSLCA here " + dimensions);
        // Look up node lists for each path
        for (short i = 0; i < dimensions; i++) {
            paths[i] = db.keywordPathsIndex.get(query[i]);
            if (verbose) System.out.println("SimpleSLCA " + query[i] + " path DLNs " + paths[i]); // + " keys " + db.keywordPathsIndex.keySet());
            // If no list for the path, return empty result
            if (paths[i] == null) {
                return result;
            }
        }
        // Allocate space to hold the shift numbers
        shift = new int[dimensions];
        for (int i = 0; i < dimensions; i++) {
            int j = 2;
            int k = 1;
            while (j < paths[i].size()) {
                j = j << 1;
                k++;
            }
            // K is power of 2 above number of paths
            shift[i] = (i == 0) ? k : k + shift[i - 1];
        }

        // Adjust shift to be total shift for level below
        for (int i = dimensions - 1; i > 0; i--) {
            shift[i] = shift[i - 1];
        }
        shift[0] = 0;

        controller.initialize(dimensions, shift, lcaLevelMap);

        //SplitMapKeywordNodeIndex index = (SplitMapKeywordNodeIndex)db.keywordNodesIndex;
        for (short i = 0; i < dimensions; i++) {
            //System.out.println("CurtFixedSLCA: keyword " + query[i]);
            // Create the iterators
            SimpleCachingFeedGroup group = controller.addFeedGroup();
            short count = 0;

            //index.addIters(group, i, query[i]);
            for (PathId pathId : paths[i]) {
                if (verbose) System.out.println("SimpleSLCA: doing path " + query[i] + " : " + pathId + " " + db.keywordNodesIndex.get(query[i]).get(pathId).size());
                Iterator<NodeId> nIter = (db.keywordNodesIndex.get(query[i]).get(pathId)).iterator();

                //Iterator<NodeId> nIter = (Iterator<NodeId>)index.getIters(query[i],pathId);
                group.addFeed(i, count++, nIter, pathId, query[i]);
            }

        }

        short temp = 0;
        createLCAs(temp, dimensions, null, 0, paths);

        controller.startFeeds();
        // Now do all of the searching through each merged iterator
        // slca holds the current candidate
        NodeId slca = null;

        //System.out.println("CurtSLCA controller done");
        // Now do all of the searching through each merged iterator
        // slca holds the current candidate
        NodeId nodeId = null;
        while (controller.hasNext()) {
            nodeId = controller.getCandidateSLCA();
            // Advance controller
            controller.next();
            if (verbose) System.out.println("SimpleSequencedSLCA next nodeId " + nodeId);
            //NodeId preNodeId = nodeId;
            if (nodeId != null) {
                int level = nodeId.getLevel();
                if (slca == null) {
                    slca = nodeId;
                    if (verbose) System.out.println("SimpleSequencedSLCA first slca at level " + level);
                    //excludes[level] = new TimeElement();
                } else if (slca.isEqualTo(nodeId)) {
                    // Equal so slca is already in result set, so ignore
                } else {
                    if (slca.isDescendantOf(nodeId)/*slca.isDescendantOrSelfOf(nodeId)*/) {
                        if (verbose) System.out.println("SimpleSequencedSLCA slca " + slca + " is descendantSelf of " + nodeId);
                        continue;
                    } else {
                        if (nodeId.isDescendantOf(slca)) {
                            if (verbose) System.out.println("SimpleSequencedSLCA nodeId " + nodeId + " is descendant of " + slca);
                            slca = nodeId;
                            continue;
                        } else {
                            // We have a new SLCA at this level
                            // Node lifetime is lifetimes[i] - excludes[i]
                            // Add liftetime + excludes to level above
                            // Build up excludes from all lower levels
                            if (verbose) System.out.println("SimpleSequencedSLCA :  new " + slca);
                            result.add(slca);
                            slca = nodeId;
                        
                        }
                    }

                }
            }
        }
         if (nodeId != null) {
            if (slca != null) {
                if (slca.isDescendantOrSelfOf(nodeId)) {
                    if (verbose) System.out.println("SimpleSLCA final adding slca to result " + slca + " " + nodeId);
                    result.add(slca);
                } else {
                    if (nodeId.isDescendantOf(slca)) {
                        if (verbose) System.out.println("CurtSLCA final adding nodeId to result " + nodeId);
                        result.add(nodeId);
                    } else {
                        // add both
                        result.add(nodeId);
                        result.add(slca);
                        if (verbose) System.out.println("CurtSLCA final adding both to result " + nodeId);
                        if (verbose) System.out.println("CurtSLCA final adding both to result " + slca);
                    }
                }
            } else {
                result.add(nodeId);
                if (verbose) System.out.println("CurtSLCA final adding to result " + nodeId);
            }
        } else {
            if (slca != null) {
                result.add(slca);
                if (verbose) System.out.println("CurtSLCA final adding to result " + slca);
            }
        }
        //stopwatch.stop();
        //System.out.println("Get result time = " + stopwatch.readTime() + "ms");
        if (verbose) for (NodeId id : result) {
            System.out.println("Result " + id);
        }
        return result;
    }

}
