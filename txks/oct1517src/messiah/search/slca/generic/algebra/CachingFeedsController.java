/*
 * This is the critical class. What we want to do is create one list that we
 * can traverse in sorted order from the merging of iterators of several
 * lists.
 */
package messiah.search.slca.generic.algebra;

import messiah.search.slca.generic.*;
import java.util.*;
import usu.NodeId;
import usu.temporal.TimeElement;

/**
 * The Feeds Controller generates sets of potential SLCAs. It merges the feeds
 * for the FeedGroups that go into producing an SLCA
 *
 * @author Curt
 */
public class CachingFeedsController implements Iterator<NodeId> {
    private boolean verbose = false;
    
    TimeElement lifetime = new TimeElement();
    PriorityQueue<NodeIdIntegerPair> whoIsNext;
    Map<Integer, CachingFeedGroup> feedMap;
    //Map<Integer, Integer> lcaLevelMap;
    boolean empty;
    Integer count;
    NodeId previousResult;

    public CachingFeedsController() {
        feedMap = new HashMap();
        whoIsNext = new PriorityQueue();
        //lcaLevelMap = new HashMap();
        empty = false;
        count = 0;
        empty = false;
        previousResult = null;
    }

    public CachingFeedGroup addFeedGroup() {
        CachingFeedGroup feedGroup = new CachingFeedGroup();

        // Set the level of the current feed group as nothing
        //lcaLevelMap.put(count, 0);

        // Add the feed to the current feed set
        if (verbose) System.out.println("CachingFeedsController addFeedGroup count is " + count + " " + feedGroup);

        feedMap.put(count, feedGroup);
        count++;
        return feedGroup;
    }

    public TimeElement gather() {
        /*
        TimeElement lifetime = new TimeElement();
        for (Integer key : feedMap.keySet()) {
            lifetime.union(feedMap.get(key).gather());
        }
        */
        return lifetime;
    }
    
    public int startFeeds() {
        int level = 0;
        if (verbose) System.out.println("CachingFeedsController startFeeds count is " + count);
        for (Integer i = 0; i < count; i++) {
            int j = startFeed(i);
            level = (j > level)? j : level;
        }
        return level;
    }

    private int startFeed(Integer i) {
        if (verbose) System.out.println("CachingFeedsController startFeed " + i);
        //if (verbose) System.out.println("CachingFeedsController startFeed " + feedMap.keySet());
        CachingFeedGroup group = feedMap.get(i);
        //if (verbose) System.out.println("CachingFeedsController startFeed " + group);
        // What is the level of this LCA
        Integer level = group.getLevelOfPathLCA();
        //lcaLevelMap.put(i, level);

        NodeId lca = group.start();
        if (verbose) System.out.println("CachingFeedsController synched " + lca);
        if (lca != null) {
            whoIsNext.add(new NodeIdIntegerPair(lca, i));
                    if (verbose) System.out.println("CachingFeedsController synched queue size " + whoIsNext.size());
        }
        // else feed is empty, don't add
        return level;
    }
    
    /* 
     * Does this iterator have a next value?
     */
    @Override
    public boolean hasNext() {
        if (verbose) System.out.println("CachingFeedsController hasnext empty? " + empty + " queue size " + whoIsNext.size());
        if (empty) {
            return false;
        }
        if (verbose) System.out.println("CachingFeedsController hasnext not empty");
        if (whoIsNext.isEmpty()) {
            empty = true;
            return false;
        }
        return true;
    }

    @Override
    public NodeId next() {
        if (verbose) System.out.println("CachingFeedsController next");
        if (empty) {
            // Should never get here, asking an empty iterator to produce
            System.err.println("Bad feed in cachingFeedsController");
            return null;
        }
        int index;
        NodeId result;
        NodeIdIntegerPair pair;
        if (previousResult == null) {
            // First time through feeds
            pair = whoIsNext.poll();
            index = pair.level;
            result = pair.lca;
            previousResult = pair.lca;
        } else {
            do {

                if (whoIsNext.isEmpty()) {
                    empty = true;
                    return null;
                }
                pair = whoIsNext.poll();
                index = pair.level;
                result = pair.lca;
                                //System.out.println("in here " + previousResult + " " + result);
                                
                if (!result.lessThanOrEqualTo(previousResult)) {
                    // Result is greater than it, continue
                    break;
                }
                
                CachingFeedGroup group = feedMap.get(index);
                group.advancePast(previousResult);
                NodeId lca = group.sync();
                lifetime = group.getLifetime();
                if (verbose) System.out.println("CachingFeedsController after advance " + lca);
                if (lca != null) {
                    pair.lca = lca;
                    previousResult = result;
                    whoIsNext.add(pair);
                }

            } while (true);

        }
        // Need to advance every group not just one
        CachingFeedGroup group = feedMap.get(index);
        group.advancePast(result);
        NodeId lca = group.sync();
        lifetime = group.getLifetime();
        if (lca != null) {
            pair.lca = lca;
            whoIsNext.add(pair);
        }
        return result;
    }

    @Override
    public void remove() {
    }

    private class LCALevelPair implements Comparable {

        public Integer level;
        public NodeId lca;
        public Map<Integer, NodeId> ancestorAtLevel;

        public LCALevelPair(NodeId lca, int level) {
            this.level = level;
            this.lca = lca;
            ancestorAtLevel = new HashMap();
        }

        @Override
        public int compareTo(Object other) {
            return compareTo((LCALevelPair) other);
        }

        public int compareTo(LCALevelPair other) {

            int result = lca.compareTo(other.lca);
            if (result != 0) {
                return result;
            }
            if (level.equals(other.level)) {
                return 0;
            }
            if (level > other.level) {
                return -1;
            }
            return 1;

        }
    }

    /*
     * Helper class that pairs a NodeId with an Integer. The Integer
     * is the number or identifier of the feed set for with the NodeId
     * is the least LCA in the set.
     */
    private class NodeIdIntegerPair implements Comparable {

        public Integer level;
        public NodeId lca;

        public NodeIdIntegerPair(NodeId lca, int level) {
            this.level = level;
            this.lca = lca;
        }

        @Override
        public int compareTo(Object other) {
            return compareTo((NodeIdIntegerPair) other);
        }

        public int compareTo(NodeIdIntegerPair other) {
            return lca.compareTo(other.lca);
        }
    }

}
