/*
 * This is the critical class. What we want to do is create one list that we
 * can traverse in sorted order from the merging of iterators of several
 * lists.
 */
package messiah.search.slca.generic.feed;

import java.util.*;
import usu.NodeId;
import usu.dln.HistoryDLN;
import usu.temporal.Time;
import usu.temporal.TimeElement;

/**
 * The Feeds Controller generates sets of potential SLCAs. It merges the feeds
 * for the FeedGroups that go into producing an SLCA
 *
 * @author Curt
 */
public class CachingFeedsController implements Iterator<NodeId> {

    private final boolean verbose = false;

    //TimeElement lifetime = new TimeElement();
    PriorityQueue<CachedNodeTriple> whoIsNext;
    CachingFeedGroup[] feeds;
    //Map<Integer, Integer> lcaLevelMap;
    boolean empty;
    Integer count;
    int key;
    int previousShifted[];
    int shift[];
    Map<Integer, Integer> lcaLevelMap;

    public CachingFeedsController() {
    }

    public void initialize(short dimensions, int shift[], Map<Integer, Integer> lcaLevelMap) {
        whoIsNext = new PriorityQueue();
        empty = false;
        count = 0;
        key = 0;
        feeds = new CachingFeedGroup[dimensions];
        this.shift = shift;
        this.lcaLevelMap = lcaLevelMap;
        previousShifted = new int[dimensions];
    }

    public CachingFeedGroup addFeedGroup() {
        CachingFeedGroup feedGroup = new CachingFeedGroup();

        // Set the level of the current feed group as nothing
        //lcaLevelMap.put(count, 0);
        // Add the feed to the current feed set
        if (verbose) {
            System.out.println("CachingFeedsController addFeedGroup count is " + count + " " + feedGroup);
        }

        feeds[count] = feedGroup;
        count++;
        return feedGroup;
    }

    public NodeId getCandidateSLCA() {
        //System.out.println("key is " + key + " " + shift[0] + " " + shift[1] + " " + shift[2]);
        //System.out.println("key is " + key);
        int level = lcaLevelMap.get(key);
        NodeId lca = null;
        if (verbose) System.out.println("Consider level " + level);

        for (CachedNodeTriple trip : whoIsNext) {
            NodeId nodeId = trip.nodeId;
            NodeId lca2 = nodeId.getAncestor(level);
            if (verbose) System.out.println(" nodeId " + nodeId + " lca2 ");
            if (lca == null) {
                lca = lca2;
            } else {
                if (!lca.equals(lca2)) {
                    //System.out.println(" no match");
                    return null;
                }
            }
        }
        //System.out.println(" found " + lca);
        return lca;
    }

    /* The gather method determines the lifetime of the SLCA, which
    is computed from the expression/semantics for this computation.
    */
    public TimeElement gather() {
        /*
         TimeElement lifetime = new TimeElement();
         for (Integer key : feedMap.keySet()) {
         lifetime.union(feedMap.get(key).gather());
         }
         */
        Time time = null;
        for (CachedNodeTriple trip : whoIsNext) {
            HistoryDLN nodeId = (HistoryDLN)trip.nodeId;
            Time time2 = nodeId.getTime();
            //System.out.print(" nodeId " + nodeId + " lca2 ");
            if (time == null) {
                time = time2;
            } else {
               time = time.intersection(time2);
                               // If there is no intersection then exit with an empty element
                if (time == null) return new TimeElement();
            }
        }
        //lifetime = new TimeElement(time);
        //return lifetime;
        return new TimeElement(time);
    }

    public void startFeeds() {
        if (verbose) {
            System.out.println("CachingFeedsController startFeeds count is " + count);
        }
        for (Integer i = 0; i < count; i++) {
            startFeed(i);
        }
    }

    private void startFeed(Integer i) {
        if (verbose) {
            System.out.println("CachingFeedsController startFeed " + i);
        }
        CachingFeedGroup group = feeds[i];

        if (!group.hasNext()) {
            // feed is empty, don't add
            //System.out.println("CachingFeedsController feed is empty " + i);
            empty = true;
            return;
        }
        CachedNodeTriple tuple = group.next();
        key = key + (tuple.id << shift[tuple.dimension]);
        previousShifted[tuple.dimension] = tuple.id << shift[tuple.dimension];

        if (verbose) {
            System.out.println("CachingFeedsController found next " + tuple.nodeId);
        }
        if (tuple.nodeId != null) {
            whoIsNext.add(tuple);
            if (verbose) {
                System.out.println("CachingFeedsController queue size " + whoIsNext.size());
            }
        }
    }

    /* 
     * Does this iterator have a next value?
     */
    @Override
    public boolean hasNext() {
        if (verbose) {
            System.out.println("CachingFeedsController hasnext empty? " + empty + " queue size " + whoIsNext.size());
        }
        if (empty) {
            return false;
        }
        if (verbose) {
            System.out.println("CachingFeedsController hasnext not empty");
        }
        return true;
    }

    @Override
    public NodeId next() {
        if (verbose) {
            System.out.println("CachingFeedsController next");
        }
        if (empty) {
            // Should never get here, asking an empty iterator to produce
            //System.err.println("Bad feed in cachingFeedsController");
            return null;
        }
        CachedNodeTriple tuple = whoIsNext.poll();
        //System.out.println("CachingFeedController next " + tuple.nodeId + " " + tuple.dimension + " " + tuple.id);

        int index = tuple.dimension;
        NodeId result = tuple.nodeId;
        CachingFeedGroup group = feeds[index];
        if (group.hasNext()) {
            // Advance group
            CachedNodeTriple trip = group.next();
            key = key - (previousShifted[trip.dimension]);
            previousShifted[trip.dimension] = trip.id << shift[trip.dimension];
            key = key + previousShifted[trip.dimension];
            //System.out.println("CachingFeedController adding " + trip.nodeId + " " + trip.dimension + " " + trip.id);
            whoIsNext.add(trip);
        } else {
            // Group is done, controller is now empty
            empty = true;
        }
        //lifetime = group.getLifetime();
        if (verbose) {
            System.out.println("CachingFeedsController after advance " + result);
        }
        return result;
    }

    @Override
    public void remove() {
    }

}
