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
import messiah.search.slca.generic.SLCAFinder;
import messiah.search.slca.generic.algebra.CachingFeedGroup;
import messiah.search.slca.generic.algebra.CachingFeedsController;
import usu.NodeId;
import usu.PathId;
import usu.algebra.KeywordSearchExpression;
import usu.temporal.TimeElement;

/**
 * This is the SLCA finder for sequenced search. Sequenced search differs from
 * nontemporal in that the SLCA has to be maintained for each level in the
 * hierarchy.
 *
 * @author curtis
 */
public class CurtSLCASequenced extends SLCAFinder {

    private final Database db;
    private final CachingFeedsController controller;

    public CurtSLCASequenced(Database db) {
        this.db = db;
        controller = new CachingFeedsController();
    }

    private void addPaths(int i, Set<PathId>[] paths, String[] query, List<KeywordPathIdPair> iters) {
        if (i >= query.length) {
            //System.out.println("CurtSLCA: end recurse ");
            CachingFeedGroup group = controller.addFeedGroup();
            int j = 0;
            for (KeywordPathIdPair iter : iters) {
                Iterator<NodeId> nIter = (db.keywordNodesIndex.get(iter.getKeyword()).get(iter.getPathId())).iterator();
                group.addFeed(nIter, iter.getPathId(), query[j++]);
            }
            return;
        }

        // Do the remaining ones
        for (PathId pathId : paths[i]) {
            //System.out.println("CurtSLCA: doing path " + pathId);
            KeywordPathIdPair ij = new KeywordPathIdPair(query[i], pathId);
            iters.add(ij);
            addPaths(i + 1, paths, query, iters);
            iters.remove(ij);
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
        Set<PathId>[] paths = new HashSet[query.length];

        //System.out.println("CurtSLCA here");
        // Look up node lists for each path
        for (int i = 0; i < query.length; i++) {
            paths[i] = db.keywordPathsIndex.get(query[i]);
            //System.out.println("CurtSLCA " + query[i] + " " + paths[i]); // + " keys " + db.keywordPathsIndex.keySet());
            // If no list for the path, return empty result
            if (paths[i] == null) {
                return result;
            }
        }

        //System.out.println("CurtSLCA paths adding paths ");
        // Build the path/nodes iterator
        addPaths(0, paths, query, new ArrayList());
        //System.out.println("CurtSLCA paths added, starting feeds ");
        int maxLevel = controller.startFeeds();

        //System.out.println("CurtSLCA controller done");
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

        // Nodes are generated in pre-order traversal, so "higher" levels will
        // exist before lower ones do.
        while (controller.hasNext()) {
            NodeId nodeId = controller.next();
            System.out.println("CurtSLCASequenced :  got nodeId " + nodeId);
            TimeElement lifetime = controller.gather();
            //System.out.println("CurtSLCA nodeId " + nodeId + " " + slca);
            int level = nodeId.getLevel();
            NodeId slca = slcas[level];
            if (slca == null) {
                slcas[level] = nodeId;
                lifetimes[level] = lifetime;
                //excludes[level] = new TimeElement();
            } else {
                // It can't be a descendant or ancestor.  Must be same or a sibiling
                if (slca.isEqualTo(nodeId)) {
                    // Increase lifetime
                    lifetimes[level].union(lifetime);
                } else {
                    // We have a new SLCA at this level
                    // Node lifetime is lifetimes[i] - excludes[i]
                    // Add liftetime + excludes to level above
                    // Build up excludes from all lower levels
                    System.out.println("CurtSLCASequenced :  new " + slca);
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

        // Generate all of the SLCAs in the array
        for (int i = maxLevel; i >= 1; i--) {
            if (slcas[i] != null) {
                                                System.out.println("CurtSLCASequenced :  final checking " + slcas[i]);
                TimeElement lifetime = lifetimes[i].difference(excludes[i]);
                if (!lifetime.isEmpty()) {
                                System.out.println("CurtSLCASequenced :  adding to result final " + slcas[i]);
                    result.add(slcas[i]);
                }
                // Exclude from everyone above me who is an ancestor.
                lifetime = lifetimes[i].union(excludes[i]);
                for (int j = i + 1; j >= 1; j--) {
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

        /*
         // Old with levels
         if (nodeId != null) {
         int level = nodeId.getLevel();
         for (int i = level; i >= 0; i--) {
         if (slcas[i] == null) {
         slcas[i] = nodeId;
         lifetimes[i] = lifetime; // Needs to be gathered lifetimes
         excludes[i] = new TimeElement();
         //System.out.println("CurtSLCA setting slca " + nodeId + " " + slca);
         continue;
         }
         // We've initialized, so test the nodeId
         if (slcas[i].isDescendantOf(nodeId)) {
         continue;
         } else if (slcas[i].isSelfOf(nodeId)) {
         lifetimes[i].union(lifetime);
         continue;
         } else {
         if (nodeId.isDescendantOf(slcas[i])) {
         slcas[i] = nodeId;
         //excludes[i].union(lifetime);  // Add to exclusion at level above
         continue;
         } else {
         //controller.gather();
         // At this level we have a match
         // Need to compute lifetime
         result.add(slcas[i]);
         for (int j = i - 1; j >= 0; j--) {
         excludes[j].union(lifetimes[i]);
         }

         System.out.println("CurtSLCA adding to result " + slcas[i] + " " + nodeId);
         slcas[i] = nodeId;
         lifetimes[i] = lifetime;
         excludes[i] = new TimeElement();
         }
         }
         }

         }
         */
        return result;
    }

    /*
     * Helper class that pairs a NodeId with an Integer. The Integer
     * is the number or identifier of the feed set for with the NodeId
     * is the least LCA in the set.
     */
    private class KeywordPathIdPair {

        public PathId pathId;
        public String keyword;

        public KeywordPathIdPair(String keyword, PathId pathId) {
            this.pathId = pathId;
            this.keyword = keyword;
        }

        public PathId getPathId() {
            return this.pathId;
        }

        public String getKeyword() {
            return this.keyword;
        }
    }
}
