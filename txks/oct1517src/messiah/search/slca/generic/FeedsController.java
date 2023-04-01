/*
 * This is the critical class. What we want to do is create one list that we
 * can traverse in sorted order from the merging of iterators of several
 * lists.
 */
package messiah.search.slca.generic;

import java.util.*;
import usu.NodeId;

/**
 * The Feeds Controller generates sets of potential SLCAs. It merges the feeds
 * for the FeedGroups that go into producing an SLCA
 *
 * @author Curt
 */
public class FeedsController implements Iterator<NodeId> {
    PriorityQueue<NodeIdIntegerPair> whoIsNext;
    Map<Integer, FeedGroup> feedMap;
    Map<Integer, Integer> lcaLevelMap;
    boolean empty;
    int count;

    public FeedsController() {
        feedMap = new HashMap();
        whoIsNext = new PriorityQueue();
        lcaLevelMap = new HashMap();
        empty = false;
        count = 0;
        empty = false;
    }

    public FeedGroup addFeedGroup() {
        FeedGroup feedGroup = new FeedGroup();

        // Set the level of the current feed group as nothing
        lcaLevelMap.put(count, 0);

        // Add the feed to the current feed set
        feedMap.put(count, feedGroup);
        return feedGroup;
    }

    public void startFeeds() {
        for (int i = 0; i < count; i++) {
            startFeed(i);
        }
    }

    public void startFeed(int i) {
        FeedGroup group = feedMap.get(count);
        
        // What is the level of this LCA
        int level = group.getLevelOfPathLCA();
        lcaLevelMap.put(i, level);
        
        group.start();
        if (group.hasNext()) {
            NodeId nodeId = group.next();
            whoIsNext.add(new NodeIdIntegerPair(nodeId,i));
        }
    }
    
    /* 
     * Does this iterator have a next value?
     */
    @Override
    public boolean hasNext() {
        System.out.println("FeedsController hasnext ");
        if (empty) {
            return false;
        }
        if (whoIsNext.isEmpty()) {
            empty = true;
            return false;
        }
        NodeIdIntegerPair pair = whoIsNext.poll();
        return true;
    }

    @Override
    public NodeId next() {
        System.out.println("FeedsController next");
        if (empty) {
            // Should never get here, asking an empty iterator to produce
            System.err.println("Bad feed in merged iterator");
            return null;
        }
        NodeIdIntegerPair pair = whoIsNext.poll();
        int index = pair.level;
        FeedGroup group = feedMap.get(index);
        if (!group.hasNext()) {
           
            System.out.println("Exiting FeedsController next has no next");
            System.exit(1);
        }
        return group.next();
    }

    /* 
     * Remove is not implemented
     */
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
            if (level > other.level) {
                // I am at a lower level, get my ancestor
                NodeId anc = null;
                if (!ancestorAtLevel.containsKey(other.level)) {
                    anc = lca.getAncestor(other.level);
                    //ancestorAtLevel.put(level)
                }

            }
            int result = lca.compareTo(other.lca);
            if (result != 0) {
                return result;
            }
            if (level == other.level) {
                return 0;
            }
            if (level > other.level) {
                return 1;
            }
            return -1;

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
