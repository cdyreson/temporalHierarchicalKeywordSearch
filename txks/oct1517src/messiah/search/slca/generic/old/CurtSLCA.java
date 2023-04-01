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

/**
 * This is the SLCA finder for both sequenced and nonsequenced search.
 *
 * @author curtis
 */
public class CurtSLCA extends SLCAFinder {

    private final Database db;
    private final CachingFeedsController controller;

    public CurtSLCA(Database db) {
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
            System.out.println("CurtSLCA " + query[i] + " " + paths[i]); // + " keys " + db.keywordPathsIndex.keySet());
            // If no list for the path, return empty result
            if (paths[i] == null) {
                return result;
            }
        }

        //System.out.println("CurtSLCA paths adding paths ");
        // Build the path/nodes iterator
        addPaths(0, paths, query, new ArrayList());
        //System.out.println("CurtSLCA paths added, starting feeds ");
        controller.startFeeds();

        //System.out.println("CurtSLCA controller done");
        // Now do all of the searching through each merged iterator
        // slca holds the current candidate
        NodeId slca = null;
        NodeId nodeId = null;
        while (controller.hasNext()) {
            nodeId = controller.next();
            System.out.println("CurtSLCA nodeId " + nodeId + " " + slca);
            NodeId preNodeId = nodeId;
            if (nodeId != null) {
                if (slca == null) {
                    slca = nodeId;
                                //System.out.println("CurtSLCA setting slca " + nodeId + " " + slca);
                    continue;
                }
                if (slca.isDescendantOrSelfOf(nodeId)) {
                    continue;
                } else {
                    if (nodeId.isDescendantOf(slca)) {
                        slca = nodeId;
                        continue;
                    } else {
                            //controller.gather();
                        // Add only if SLCA is not ancestor or descendant of previous
                        result.add(slca);
  
                        System.out.println("CurtSLCA adding to result " + slca + " " + preNodeId);
                                              slca = nodeId;
                    }
                }

            }
        }
        if (nodeId != null) {
            if (slca != null) {
                if (slca.isDescendantOrSelfOf(nodeId)) {
                    System.out.println("CurtSLCA final adding slca to result " + slca);
                    result.add(slca);
                } else {
                    if (nodeId.isDescendantOf(slca)) {
                        System.out.println("CurtSLCA final adding nodeId to result " + nodeId);
                        result.add(nodeId);
                    } else {
                        // add both
                        result.add(nodeId);
                        result.add(slca);
                        System.out.println("CurtSLCA final adding both to result " + nodeId);
                        System.out.println("CurtSLCA final adding both to result " + slca);
                    }
                }
            } else {
                result.add(nodeId);
                System.out.println("CurtSLCA final adding to result " + nodeId);
            }
        } else {
            if (slca != null) {
                result.add(slca);
                System.out.println("CurtSLCA final adding to result " + slca);
            }
        }
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
