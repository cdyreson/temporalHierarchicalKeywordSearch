package messiah.search.slca.generic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import messiah.database.Database;
import messiah.search.slca.generic.feed.SimpleCachingFeedGroup;
import messiah.search.slca.generic.feed.SimpleCachingFeedsController;
import usu.NodeId;
import usu.PathId;
import usu.algebra.KeywordSearchExpression;
import usu.temporal.TimeElement;

/**
 * This is the SLCA finder for sequenced search.
 *
 * @author curtis
 */
public class SimpleSequencedSLCA extends SLCAFinder {

    private final Database db;
    private final SimpleCachingFeedsController controller;
    int shift[];
    Map<Integer, Integer> lcaLevelMap;
    int maxLevel = 0;
    boolean verbose = false;

    public SimpleSequencedSLCA(Database db) {
        this.db = db;
        controller = new SimpleCachingFeedsController();
        lcaLevelMap = new HashMap();
    }

    private void createLCAs(short current, short dimensions, PathId pathLCA, int hashKey, Set<PathId>[] paths) {
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
        //Stopwatch stopwatch = new Stopwatch();
        //stopwatch.start();
        Set<NodeId> result = new LinkedHashSet<>();
        String[] query = exp.getKeywordsAsArray();
        short dimensions = (short) query.length;
        Set<PathId>[] paths = new HashSet[dimensions];

        // Look up node lists for each path
        for (short i = 0; i < dimensions; i++) {
            paths[i] = db.keywordPathsIndex.get(query[i]);
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
            // Create the iterators
            SimpleCachingFeedGroup group = controller.addFeedGroup();
            short count = 0;

            for (PathId pathId : paths[i]) {
                Iterator<NodeId> nIter = (db.keywordNodesIndex.get(query[i]).get(pathId)).iterator();
                group.addFeed(i, count++, nIter, pathId, query[i]);
            }

        }

        short temp = 0;
        createLCAs(temp, dimensions, null, 0, paths);

        controller.startFeeds();
        // Initialize all of the timestamp info
        NodeId[] slcas = new NodeId[maxLevel + 1];
        TimeElement[] lifetimes = new TimeElement[maxLevel + 1];
        TimeElement[] excludes = new TimeElement[maxLevel + 1];
        // Initialize the excludes at each level
        for (int i = 0; i <= maxLevel; i++) {
            excludes[i] = new TimeElement();
            lifetimes[i] = new TimeElement();
        }

        // Now do all of the searching through each merged iterator
        // slca holds the current candidate
        NodeId nodeId = null;
        while (controller.hasNext()) {
            nodeId = controller.getCandidateSLCA();

            // Check to see if we are done, out of nodeIds
            if (nodeId == null) {
                break;
            }

            // Advance controller
            controller.next();
            if (verbose) {
                System.out.println("SimpleSequencedSLCA next nodeId " + nodeId);
            }

            int level = nodeId.getLevel();
            NodeId slca = slcas[level];
            TimeElement lifetime = controller.gather();
            //System.out.println("SimpleSequencedSLCA lifetime " + lifetime);
            if (slca == null) {
                slcas[level] = nodeId;
                lifetimes[level] = lifetime;
                if (verbose) {
                    System.out.println("SimpleSequencedSLCA first slca at level " + level);
                }
                //excludes[level] = new TimeElement();
            } else {
                // Check to see if this is the slca and the node are the same
                // Shouldn't we just check self?
                if (slca.isDescendantOrSelfOf(nodeId)) {
                    if (verbose) {
                        System.out.println("SimpleSequencedSLCA slca " + slca + " is descendantSelf of " + nodeId);
                    }
                    lifetimes[level] = lifetimes[level].union(lifetime);
                    continue;
                } else {
                    if (nodeId.isDescendantOf(slca)) {
                        if (verbose) {
                            System.out.println("SimpleSequencedSLCA nodeId " + nodeId + " is descendant of " + slca);
                        }
                        lifetimes[level] = lifetimes[level].union(lifetime);
                        slca = nodeId;
                        continue;
                    } else {
                        // We have a new SLCA at this level
                        // Existing node's lifetime is lifetimes[i] - excludes[i]
                        // Add liftetime + excludes to level above
                        // Build up excludes from all lower levels
                        if (verbose) {
                            System.out.println("SimpleSequencedSLCA :  new " + slca);
                        }
                        for (int i = maxLevel; i > level; i--) {
                            if (slcas[i] != null) {
                                if (verbose) {
                                    System.out.println("SimpleSequencedSLCA checking " + slcas[i]);
                                }
                                if (slcas[i].isDescendantOf(slca)) {
                                    lifetimes[i] = lifetimes[i].difference(excludes[i]);
                                    if (verbose) {
                                        System.out.println("SimpleSequencedSLCA is descendant " + slcas[i]);
                                    }
                                    // If lifetime is empty don't add to result
                                    if (!lifetimes[i].isEmpty()) {
                                        //System.out.println("SimpleSequencedSLCA adding to result " + slcas[i]);
                                        if (verbose) {
                                            System.out.println("SimpleSequencedSLCA adding A to result " + slcas[i] + " lifetime " + lifetimes[i] + " excludes " + excludes[i]);
                                        }
                                        result.add(slcas[i]);
                                        for (int j = i - 1; j > 0; j--) {
                                            //if (slca == null) continue;
                                            if (slcas[j] == null) {
                                                continue;
                                            }
                                            if (slca.isDescendantOf(slcas[j])) {
                                                if (verbose) {
                                                    System.out.println("Fixing excludes at level " + j + " " + level);
                                                }
                                                excludes[j] = excludes[j].union(lifetimes[i]);
                                            }
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
                                    if (verbose) {
                                        System.out.println("Modifying excludes at level " + i + " " + level);
                                    }
                                    excludes[level] = excludes[level].union(lifetimes[i].union(excludes[i]));
                                    if (verbose) {
                                        System.out.println("CurtFixedCached: excluded " + excludes[level]);
                                    }
                                }
                            }
                        }
                        lifetimes[level] = lifetimes[level].difference(excludes[level]);

                        // If lifetime is empty don't add to result
                        if (!lifetimes[level].isEmpty()) {
                            //System.out.println("SimpleSequencedSLCA adding to result " + slca);
                            if (verbose) {
                                System.out.println("SimpleSequencedSLCA adding to result " + slca + " lifetime " + lifetimes[level] + " excludes " + excludes[level]);
                            }
                            result.add(slca);
                        }
                        // Add excludes to at least one good level above
                        for (int i = level - 1; i >= 1; i--) {
                            if (slcas[i] != null) {
                                if (slca.isDescendantOf(slcas[i])) {
                                    excludes[i] = excludes[i].union(excludes[level].union(lifetimes[level]));
                                    if (verbose) {
                                        System.out.println("Modifying excludes " + i + " " + level + " slca " + slca + " slca[i] " + slcas[i] + " " + excludes[level]);
                                    }
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
        // Generate all of the SLCAs in the array
        for (int i = maxLevel; i >= 1; i--) {
            if (slcas[i] != null) {
                if (verbose) {
                    System.out.println("SimpleSLCASequenced :  final checking " + slcas[i] + " lifetimes " + lifetimes[i] + " excludes " + excludes[i]);
                }
                TimeElement lifetime = lifetimes[i].difference(excludes[i]);
                if (!lifetime.isEmpty()) {
                    //System.out.println("CurtSLCASequenced :  adding to result final " + slcas[i]);
                    if (verbose) {
                        System.out.println("SimpleSequencedSLCA adding to result final " + slcas[i] + " lifetime " + lifetime + " excludes " + excludes[i]);
                    }
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
        if (verbose) for (NodeId id : result) {
            System.out.println("Result " + id);
        }
        return result;
    }

}
