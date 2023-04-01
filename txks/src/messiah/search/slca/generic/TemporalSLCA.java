package messiah.search.slca.generic;

import java.util.LinkedHashSet;
import java.util.Set;
import messiah.database.Database;
import messiah.search.slca.generic.feed.TemporalFeedsOuterController;
import usu.NodeId;
import usu.algebra.KeywordSearchExpression;
import usu.dln.TimeElementHistoryDLN;
import usu.temporal.TimeElement;

/**
 * This is the SLCA finder for all temporal search, both nonsequenced and
 * sequenced search.
 *
 * @author curtis
 */
public class TemporalSLCA extends SLCAFinder {
    private final TemporalFeedsOuterController controller;
    private int maxLevel = 0;
    private boolean verbose = false;

    public TemporalSLCA(Database db) {
        controller = new TemporalFeedsOuterController(db);
    }

    /*
     Fetch the SLCAs
     */
    @Override
    public Set<NodeId> getSLCA(KeywordSearchExpression exp) {
        // results
        //Stopwatch stopwatch = new Stopwatch();
        //stopwatch.start();
        Set<NodeId> result = new LinkedHashSet<>(); 

        maxLevel = controller.initialize(exp);
        // Check to see if we have no result (initialize will return 0)
        if (maxLevel == 0) return result;

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
            nodeId = controller.next();

             // Check to see if we are done, out of nodeIds
            if (nodeId == null) {
                break;
            }
            
            if (verbose) {
                System.out.println("TemporalSLCA next nodeId " + nodeId);
            }

            int level = nodeId.getLevel();
            NodeId slca = slcas[level];
            TimeElement lifetime = ((TimeElementHistoryDLN)nodeId).getTime();
            if (slca == null) {
                slcas[level] = nodeId;
                lifetimes[level] = lifetime;
                if (verbose) {
                    System.out.println("TemporalSLCA first slca at level " + level);
                }
                //excludes[level] = new TimeElement();
            } else {
                // Check to see if this is the slca and the node are the same
                // Shouldn't we just check self?
                if (slca.isDescendantOrSelfOf(nodeId)) {
                    if (verbose) {
                        System.out.println("TemporalSLCA slca " + slca + " is descendantSelf of " + nodeId);
                    }
                    lifetimes[level] = lifetimes[level].union(lifetime);
                    continue;
                } else {
                    if (nodeId.isDescendantOf(slca)) {
                        if (verbose) {
                            System.out.println("TemporalSLCA nodeId " + nodeId + " is descendant of " + slca);
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
                            System.out.println("TemporalSLCA :  new " + slca);
                        }
                        for (int i = maxLevel; i > level; i--) {
                            if (slcas[i] != null) {
                                if (verbose) {
                                    System.out.println("TemporalSLCA checking " + slcas[i]);
                                }
                                if (slcas[i].isDescendantOf(slca)) {
                                    lifetimes[i] = lifetimes[i].difference(excludes[i]);
                                    if (verbose) {
                                        System.out.println("TemporalSLCA is descendant " + slcas[i]);
                                    }
                                    // If lifetime is empty don't add to result
                                    if (!lifetimes[i].isEmpty()) {
                                        //System.out.println("TemporalSLCA adding to result " + slcas[i]);
                                        if (verbose) {
                                            System.out.println("TemporalSLCA adding A to result " + slcas[i] + " lifetime " + lifetimes[i] + " excludes " + excludes[i]);
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
                                        System.out.println("TemporalSLCA: Modifying excludes at level " + i + " " + level);
                                    }
                                    excludes[level] = excludes[level].union(lifetimes[i].union(excludes[i]));
                                    if (verbose) {
                                        System.out.println("TemporalSLCA: excluded " + excludes[level]);
                                    }
                                }
                            }
                        }
                        lifetimes[level] = lifetimes[level].difference(excludes[level]);

                        // If lifetime is empty don't add to result
                        if (!lifetimes[level].isEmpty()) {
                            //System.out.println("TemporalSLCA adding to result " + slca);
                            if (verbose) {
                                System.out.println("TemporalSLCA adding to result " + slca + " lifetime " + lifetimes[level] + " excludes " + excludes[level]);
                            }
                            result.add(slca);
                        }
                        // Add excludes to at least one good level above
                        for (int i = level - 1; i >= 1; i--) {
                            if (slcas[i] != null) {
                                if (slca.isDescendantOf(slcas[i])) {
                                    if (verbose) {
                                        System.out.println("TemporalSLCA: Modifying excludes " + i + " " + level + " slca " + slca + " slca[i] " + slcas[i]);
                                    }
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
        // Generate all of the SLCAs in the array
        for (int i = maxLevel; i >= 1; i--) {
            if (slcas[i] != null) {
                if (verbose) {
                    System.out.println("TemporalSLCA :  final checking " + slcas[i] + " lifetimes " + lifetimes[i] + " excludes " + excludes[i]);
                }
                TimeElement lifetime = lifetimes[i].difference(excludes[i]);
                if (!lifetime.isEmpty()) {
                    //System.out.println("CurtSLCASequenced :  adding to result final " + slcas[i]);
                    if (verbose) {
                        System.out.println("TemporalSLCA adding to result final " + slcas[i] + " lifetime " + lifetime + " excludes " + excludes[i]);
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
