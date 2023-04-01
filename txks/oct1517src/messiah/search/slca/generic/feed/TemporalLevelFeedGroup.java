/*
 * This is the critical class. What we want to do is create one list that we
 * can traverse in sorted order from the merging of iterators of several
 * lists.
 */
package messiah.search.slca.generic.feed;

import java.util.*;
import usu.NodeId;
import usu.PathId;
import usu.temporal.TimeElement;

/**
 *
 * The FeedGroup is a group of Feeds, which are Iterators that produce an SLCA.
 *
 * @author Curt
 */
public class TemporalLevelFeedGroup implements Iterator<CachedNodeTriple> {

    private boolean verbose = false;
    private List<NodeId>[] levels;
    private int maxLevel;

    private PriorityQueue<CachedNodeInfo> feeds;
    //private short feedCount = 0;
    //private TimeElement lifetime;
    //public short cachedId = 0;
    //public short cachedDimension = 0;

    public TemporalLevelFeedGroup(int maxLevel) {
        feeds = new PriorityQueue();
        this.maxLevel = maxLevel;
        levels = new List[maxLevel];
    }

    //public TimeElement getLifetime() {
    //    return lifetime;
    //}

    public void addFeed(short dimension, short count, Iterator<NodeId> feed, PathId pathId, String keyword) {
        //System.out.println("CachingFeedGroup addFeed " + keyword + " " + pathId + " " + dimension + " " + count);
        if (verbose) {
            System.out.println("SimpleCachingFeedGroup addFeed " + keyword + " " + pathId + " " + dimension + " " + count);
        }

        //Increment the count of the number of feeds
        //feedCount = count;
        
        // Check if this feed has something, if not merged iterator is empty
        if (!feed.hasNext()) {
            if (verbose) {
                System.out.println("SimpleCachingFeedGroup addFeed empty feed!");
            }
            return;
        }

        NodeId nodeId = feed.next();
        //System.out.println("CachingFeedGroup addFeed " + keyword + " " + pathId + " " + dimension + " " + count + " " + nodeId);
        feeds.add(new CachedNodeInfo(nodeId, feed, count, dimension));
    }

    public boolean hasNext() {
        if (verbose) {
            System.out.println("SimpleCachingFeedGroup hasNext " + feeds.isEmpty());
        }
        return !feeds.isEmpty();
    }

    public CachedNodeTriple next() {
        if (verbose) {
            System.out.println("SipmleCachingFeedGroup next: " + hasNext());
        }
        
        // Figure out the pathLCA for every feed
        if (!hasNext()) {
            // Should never get here, did not check hasNext() first
            //System.err.println("Bad feed in CachingFeedGroup");
            return null;
        }
        
        // Get the next one
        CachedNodeInfo tuple = feeds.poll();
        NodeId nodeId = tuple.nodeId;
        //System.out.println("CachingFeedGroup got " + nodeId + " " + tuple.id + " " + tuple.dimension);
        if (tuple.iter.hasNext()) {
            tuple.nodeId = tuple.iter.next();
            //cachedId = tuple.id;
            //cachedDimension = tuple.dimension;
            feeds.add(tuple);
        }

        if (verbose) {
            System.out.println("SimpleCachingFeedGroup start: nodeId " + nodeId + " id " + tuple.id + " dimension " + tuple.dimension);
        }
        return new CachedNodeTriple(nodeId, tuple.dimension, tuple.id);
    }

   
}
