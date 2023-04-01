package messiah.search.slca.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import messiah.KeywordInfo;
import messiah.database.Database;
//import messiah.database.berkeleydb.SplitIterator;
//import messiah.database.berkeleydb.SplitMapKeywordNodeIndex;
import messiah.search.slca.generic.feed.SimpleCachingFeedGroup;
import messiah.search.slca.generic.feed.SimpleCachingFeedsController;
import messiah.utils.Stopwatch;
import usu.NodeId;
import usu.PathId;
import usu.algebra.KeywordSearchExpression;
import usu.temporal.TimeElement;

/**
 * This is the SLCA finder for sequenced search.  It goes through nodes in order,
 * placing fragments (lifetimes of nodes that do not match) in a priority queue 
 * to be served.
 *
 * @author curtis
 */
public class FragmentedSimpleSequencedSLCA extends SLCAFinder {

    private final Database db;
    private final SimpleCachingFeedsController controller;
    int shift[];
    Map<Integer, Integer> lcaLevelMap;
    int maxLevel = 0;

    public FragmentedSimpleSequencedSLCA(Database db) {
        this.db = db;
        controller = new SimpleCachingFeedsController();
        lcaLevelMap = new HashMap();
    }

    /*
    * Create the initial LCAs for each keyword combination.  We figure out the LCA from the PathId's.
    * This basically is the FSLCA approach.  
    */
    private void createLCAs(short current, short dimensions, PathId pathLCA, int hashKey, Set<PathId>[] paths) {
        // Reached the end
        if (current >= dimensions) {
            //System.out.println("level is " + pathLCA.getLevel());
            int level = pathLCA.getLevel() - 1;
            lcaLevelMap.put(hashKey, level);
            maxLevel = (level > maxLevel) ? level : maxLevel;
            return;
        }

        // Process this set of paths
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
     Fetch the SLCAs
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

        //System.out.println("CurtSLCA here");
        // Look up node lists for each path
        for (short i = 0; i < dimensions; i++) {
            //System.out.println("CurtFixedSLCA " + db.keywordPathsIndex);
            paths[i] = db.keywordPathsIndex.get(query[i]);
            //System.out.println("CurtFixedSLCA " + query[i] + " " + paths[i]); // + " keys " + db.keywordPathsIndex.keySet());
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
                //System.out.println("CurtFixedCachedSequencedSLCA: doing path " + query[i] + " : " + pathId + " " + db.keywordNodesIndex.get(query[i]).get(pathId).size());
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
        NodeId[] slcas = new NodeId[maxLevel + 1];
        TimeElement[] lifetimes = new TimeElement[maxLevel + 1];
        TimeElement[] excludes = new TimeElement[maxLevel + 1];
        // Initialize the excludes at each level
        for (int i = 0; i <= maxLevel; i++) {
            excludes[i] = new TimeElement();
            lifetimes[i] = new TimeElement();
        }

        //System.out.println("CurtSLCA controller done");
        // Now do all of the searching through each merged iterator
        // slca holds the current candidate
        NodeId nodeId = null;
        while (controller.hasNext()) {
            nodeId = controller.getCandidateSLCA();
            TimeElement lifetime = controller.gather();
            // Advance controller
            controller.next();
            System.out.println("SimpleSequencedSLCA next nodeId " + nodeId);
            //NodeId preNodeId = nodeId;
            if (nodeId != null) {
                int level = nodeId.getLevel();
                NodeId slca = slcas[level];
                if (slca == null) {
                    slcas[level] = nodeId;
                    lifetimes[level] = lifetime;
                    System.out.println("SimpleSequencedSLCA first slca at level " + level);
                    //excludes[level] = new TimeElement();
                } else {
                    if (slca.isDescendantOrSelfOf(nodeId)) {
                        System.out.println("SimpleSequencedSLCA slca " + slca + " is descendantSelf of " + nodeId);
                        lifetimes[level] = lifetimes[level].union(lifetime);
                        continue;
                    } else {
                        if (nodeId.isDescendantOf(slca)) {
                            System.out.println("SimpleSequencedSLCA nodeId " + nodeId + " is descendant of " + slca);
                            lifetimes[level] = lifetimes[level].union(lifetime);
                            slca = nodeId;
                            continue;
                        } else {
                            // We have a new SLCA at this level
                            // Existing node's lifetime is lifetimes[i] - excludes[i]
                            // Add liftetime + excludes to level above
                            // Build up excludes from all lower levels
                            System.out.println("SimpleSequencedSLCA :  new " + slca);
                            for (int i = maxLevel; i > level; i--) {
                                if (slcas[i] != null) {
                                    System.out.println("SimpleSequencedSLCA checking " + slcas[i]);
                                    if (slcas[i].isDescendantOf(slca)) {
                                        lifetimes[i] = lifetimes[i].difference(excludes[i]);
                                        System.out.println("SimpleSequencedSLCA is descendant " + slcas[i]);
                                        // If lifetime is empty don't add to result
                                        if (!lifetimes[i].isEmpty()) {
                                            //System.out.println("SimpleSequencedSLCA adding to result " + slcas[i]);
                                            System.out.println("SimpleSequencedSLCA adding A to result " + slcas[i] + " lifetime " + lifetimes[i] + " excludes " + excludes[i]);
                                            result.add(slcas[i]);
                                            for (int j = i - 1; j > 0; j--) {
                                                //if (slca.isDescendantOf(slcas[j])) {
                                                 System.out.println("Fixing excludes at level " + j + " " + level);
                                                    excludes[j] = excludes[j].union(lifetimes[i]);
                                                //}
                                            }
                                        }
                                        excludes[i] = new TimeElement();
                                        slcas[i] = null;
                                        lifetimes[i] = new TimeElement();
                                    }
                                }
                            }
                            for (int i = level + 1; i <= maxLevel; i++) {
                                if (slcas[i] != null) {

                                    if (slcas[i].isDescendantOf(slca)) {
                                                            System.out.println("Modifying excludes at level " + i + " " + level);
                                        excludes[level] = excludes[level].union(lifetimes[i].union(excludes[i]));
                                        System.out.println("CurtFixedCached: excluded " + excludes[level]);
                                    }
                                }
                            }
                            lifetimes[level] = lifetimes[level].difference(excludes[level]);

                            // If lifetime is empty don't add to result
                            if (!lifetimes[level].isEmpty()) {
                                //System.out.println("SimpleSequencedSLCA adding to result " + slca);
                                System.out.println("SimpleSequencedSLCA adding to result " + slca + " lifetime " + lifetimes[level] + " excludes " + excludes[level]);
                                result.add(slca);
                            }
                            // Add excludes to at least one good level above
                            for (int i = level - 1; i >= 1; i--) {
                                if (slcas[i] != null) {
                                    if (slca.isDescendantOf(slcas[i])) {
                                        System.out.println("Modifying excludes " + i + " " + level);
                                        excludes[i] = excludes[i].union(excludes[level]);
                                        i = 0;
                                    }
                                }
                            }
                            excludes[level] = new TimeElement();
                            slcas[level] = nodeId;
                            lifetimes[level] = lifetimes[level].union(lifetime);
                        }
                    }

                }
            }
        }
        // Generate all of the SLCAs in the array
        for (int i = maxLevel; i >= 1; i--) {
            if (slcas[i] != null) {
                System.out.println("SimpleSLCASequenced :  final checking " + slcas[i] + " lifetimes " + lifetimes[i] + " excludes " + excludes[i]);
                TimeElement lifetime = lifetimes[i].difference(excludes[i]);
                if (!lifetime.isEmpty()) {
                    //System.out.println("CurtSLCASequenced :  adding to result final " + slcas[i]);
                    System.out.println("SimpleSequencedSLCA adding to result final " + slcas[i] + " lifetime " + lifetime + " excludes " + excludes[i]);
                    result.add(slcas[i]);
                }
                // Exclude from everyone above me who is an ancestor.
                lifetime = lifetimes[i].union(excludes[i]);
                for (int j = i - 1; j >= 1; j--) {
                    if (slcas[j] != null) {
                        if (slcas[i].isDescendantOf(slcas[j])) {
                            TimeElement before = lifetimes[j];
                            lifetimes[j] = lifetimes[j].difference(lifetime);
                            //System.out.println("excluding " + before + " : " + lifetime + " : " + lifetimes[j]);
                        }
                    }
                }

            }
            excludes[i - 1] = excludes[i - 1].union(excludes[i]);
            excludes[i - 1] = excludes[i - 1].union(lifetimes[i]);
        }
        //stopwatch.stop();
        //System.out.println("Get result time = " + stopwatch.readTime() + "ms");
        for (NodeId id : result) {
            System.out.println("Result " + id);
        }
        return result;
    }

}
