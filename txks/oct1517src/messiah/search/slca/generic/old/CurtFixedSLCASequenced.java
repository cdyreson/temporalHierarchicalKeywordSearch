package messiah.search.slca.generic.old;

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
import messiah.database.berkeleydb.SplitIterator;
import messiah.database.berkeleydb.SplitMapKeywordNodeIndex;
import messiah.search.slca.generic.SLCAFinder;
import messiah.search.slca.generic.feed.CachingFeedGroup;
import messiah.search.slca.generic.feed.CachingFeedsController;
import usu.NodeId;
import usu.PathId;
import usu.algebra.KeywordSearchExpression;
import usu.temporal.TimeElement;

/**
 * This is the SLCA finder for sequenced search.
 *
 * @author curtis
 */
public class CurtFixedSLCASequenced extends SLCAFinder {

    private final Database db;
    private final CachingFeedsController controller;
    int shift[];
    Map<Integer, Integer> lcaLevelMap;
    int maxLevel = 0;

    public CurtFixedSLCASequenced(Database db) {
        this.db = db;
        controller = new CachingFeedsController();
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
        Set<NodeId> result = new LinkedHashSet<>();
        short dimensions = (short) query.length;
        Set<PathId>[] paths = new HashSet[dimensions];

        //System.out.println("CurtSLCA here");
        // Look up node lists for each path
        for (short i = 0; i < dimensions; i++) {
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

        SplitMapKeywordNodeIndex index = (SplitMapKeywordNodeIndex)db.keywordNodesIndex;
        for (short i = 0; i < dimensions; i++) {
            //System.out.println("CurtFixedSLCA: keyword " + query[i]);
            // Create the iterators
            CachingFeedGroup group = controller.addFeedGroup();
            short count = 0;

            index.addIters(group, i, query[i]);
            
            /*
            for (PathId pathId : paths[i]) {
                System.out.println("CurtFixedSLCA: doing path " + pathId + " " + db.keywordNodesIndex.get(query[i]).get(pathId).size());
                Iterator<NodeId> nIter = (db.keywordNodesIndex.get(query[i]).get(pathId)).iterator();

                //Iterator<NodeId> nIter = (Iterator<NodeId>)index.getIters(query[i],pathId);
                group.addFeed(i, count++, nIter, pathId, query[i]);
            }
                    */
                    
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

            // Advance controller
            controller.next();
            //System.out.println("CurtSLCA nodeId " + nodeId + " " + slca);
            //NodeId preNodeId = nodeId;
            if (nodeId != null) {
                int level = nodeId.getLevel();
                NodeId slca = slcas[level];
                TimeElement lifetime = controller.gather();
                if (slca == null) {
                    slcas[level] = nodeId;
                    lifetimes[level] = lifetime;
                    //excludes[level] = new TimeElement();
                } else {
                    if (slca.isDescendantOrSelfOf(nodeId)) {
                        lifetimes[level].union(lifetime);
                        continue;
                    } else {
                        if (nodeId.isDescendantOf(slca)) {
                            lifetimes[level].union(lifetime);
                            slca = nodeId;
                            continue;
                        } else {
                            // We have a new SLCA at this level
                            // Node lifetime is lifetimes[i] - excludes[i]
                            // Add liftetime + excludes to level above
                            // Build up excludes from all lower levels
                            //System.out.println("CurtSLCASequenced :  new " + slca);
                            for (int i = level + 1; i <= maxLevel; i++) {
                                if (slcas[i] != null) {
                                    if (slcas[i].isDescendantOf(slca)) {
                                        excludes[level].union(lifetimes[i].union(excludes[i]));
                                    }
                                }
                            }
                            lifetimes[level].difference(excludes[level]);

                            // If lifetime is empty don't add to result
                            if (!lifetimes[level].isEmpty()) {
                                result.add(nodeId);
                            }
                            // Add excludes to at least one good level above
                            for (int i = level - 1; i >= 1; i--) {
                                if (slcas[i] != null) {
                                    if (slca.isDescendantOf(slcas[i])) {
                                        excludes[i].union(excludes[level]);
                                        i = 0;
                                    }
                                }
                            }
                            excludes[level] = new TimeElement();
                            slcas[level] = nodeId;
                            lifetimes[level].union(lifetime);
                        }
                    }

                }
            }
        }
        // Generate all of the SLCAs in the array
        for (int i = maxLevel; i >= 1; i--) {
            if (slcas[i] != null) {
                //System.out.println("CurtSLCASequenced :  final checking " + slcas[i]);
                TimeElement lifetime = lifetimes[i].difference(excludes[i]);
                if (!lifetime.isEmpty()) {
                    //System.out.println("CurtSLCASequenced :  adding to result final " + slcas[i]);
                    result.add(slcas[i]);
                }
                // Exclude from everyone above me who is an ancestor.
                lifetime = lifetimes[i].union(excludes[i]);
                for (int j = i - 1; j >= 1; j--) {
                    if (slcas[j] != null) {
                        if (slcas[i].isDescendantOf(slcas[j])) {
                            lifetimes[j].difference(lifetime);
                        }
                    }
                }

            }
            excludes[i - 1].union(excludes[i]);
            excludes[i - 1].union(lifetimes[i]);
        }
        return result;
    }

}
