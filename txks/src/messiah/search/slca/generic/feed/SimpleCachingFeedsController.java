/*
 * This is the critical class. What we want to do is create one list that we
 * can traverse in sorted order from the merging of iterators of several
 * lists.
 */
package messiah.search.slca.generic.feed;

//import java.lang.reflect.Array;
import java.util.*;
import usu.NodeId;
import usu.dln.HistoryDLN;
//import usu.dln.TimeElementHistoryDLN;
import usu.temporal.Time;
import usu.temporal.TimeElement;

/**
 * The Feeds Controller generates sets of potential SLCAs. It merges the feeds
 * for the FeedGroups that go into producing an SLCA
 *
 * @author Curt
 */
public class SimpleCachingFeedsController implements Iterator<NodeId> {

    private final boolean verbose = false;

    //TimeElement lifetime = new TimeElement();
    PriorityQueue<CachedNodeTriple> whoIsNext;
    SimpleCachingFeedGroup[] feeds;
    //Map<Integer, Integer> lcaLevelMap;
    boolean empty;
    Integer count;
    int key;
    int previousShifted[];
    int shift[];
    Map<Integer, Integer> lcaLevelMap;
    NodeId[] currentNodeIds;

    public SimpleCachingFeedsController() {
    }

    public void initialize(short dimensions, int shift[], Map<Integer, Integer> lcaLevelMap) {
        whoIsNext = new PriorityQueue();
        empty = false;
        count = 0;
        key = 0;
        feeds = new SimpleCachingFeedGroup[dimensions];
        this.shift = shift;
        this.lcaLevelMap = lcaLevelMap;
        previousShifted = new int[dimensions];
        currentNodeIds = new NodeId[dimensions];
    }

    public NodeId[] getCurrentNodeIds() {
        return currentNodeIds;
    }

    public SimpleCachingFeedGroup addFeedGroup() {
        SimpleCachingFeedGroup feedGroup = new SimpleCachingFeedGroup();

        // Set the level of the current feed group as nothing
        //lcaLevelMap.put(count, 0);
        // Add the feed to the current feed set
        if (verbose) {
            System.out.println("SimpleCachingFeedsController addFeedGroup count is " + count + " " + feedGroup);
        }

        feeds[count] = feedGroup;
        count++;
        return feedGroup;
    }

    public NodeId getCandidateSLCA() {
        int level = lcaLevelMap.get(key);
        NodeId lca = null;
        for (CachedNodeTriple trip : whoIsNext) {
            NodeId nodeId = trip.nodeId;
            currentNodeIds[trip.dimension] = nodeId;
            NodeId lca2 = nodeId.getAncestor(level);
            if (lca == null) {
                lca = lca2;
            } else {
                int lcaLevel = lca.computeNCALevel(lca2);
                lca = lca.getAncestor(lcaLevel);
            }
        }
        if (verbose) {
            System.out.println("SimpleCachingFeedsController: Priority queue is ");
            for (NodeId n : currentNodeIds) {
                System.out.print(n + " ");
            }
            System.out.println("");
        }
        return lca;
    }

    /* 
     * gather returns the list of times for the current CachedNodeTriple
     * at the front of the priority queue.  Use it to get the lifetime.
     */
    public Time[] gatherTimes() {
        Time[] time = new Time[whoIsNext.size()];
        int i = 0;
        for (CachedNodeTriple trip : whoIsNext) {
            HistoryDLN nodeId = (HistoryDLN) trip.nodeId;
            Time time2 = nodeId.getTime();
            time[i++] = time2;
        }
        return time;
    }

    /* 
     gather returns the intersection of the times for the current CachedNodeTriple
     at the front of the priority queue.  Use it to get the lifetime.
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
            HistoryDLN nodeId = (HistoryDLN) trip.nodeId;
            Time time2 = nodeId.getTime();
            if (verbose) System.out.print("SimpleCachingFeedsController: gather nodeId " + nodeId);
            if (time == null) {
                time = time2;
            } else {
                time = time.intersection(time2);
                // If there is no intersection then exit with an empty element
                if (time == null) return new TimeElement();
            }
        }
        return new TimeElement(time);
    }

    /* 
     fragments returns the fragments of the current CachedNodeTriple that do not
     overlap the given lifetime, and the fragment must be at the given level.
     */
    /*
     public List<NodeId> fragments(TimeElement lifetime, int level) {
     List<NodeId> result = new ArrayList();
     for (CachedNodeTriple trip : whoIsNext) {
     HistoryDLN nodeId = (HistoryDLN) trip.nodeId;
     TimeElement time2 = lifetime.difference(new TimeElement(nodeId.getTime()));
     if (!time2.isEmpty()) {
     for (Time t : time2.getTimes()) {
     result.add(new HistoryDLN(nodeId.getAncestor(level), t));
     }
     }
     }
     return result;
     }
     */
    public void startFeeds() {
        if (verbose) {
            System.out.println("SimpleCachingFeedsController startFeeds count is " + count);
        }
        for (Integer i = 0; i < count; i++) {
            startFeed(i);
        }
    }

    private void startFeed(Integer i) {
        if (verbose) {
            System.out.println("SimpleCachingFeedsController startFeed " + i);
        }
        SimpleCachingFeedGroup group = feeds[i];

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
            System.out.println("SimpleCachingFeedsController found next " + tuple.nodeId);
        }
        if (tuple.nodeId != null) {
            whoIsNext.add(tuple);
            if (verbose) {
                System.out.println("SimpleCachingFeedsController queue size " + whoIsNext.size());
            }
        }
    }

    /* 
     * Does this iterator have a next value?
     */
    @Override
    public boolean hasNext() {
        if (verbose) {
            System.out.println("SimpleCachingFeedsController hasnext empty? " + empty + " queue size " + whoIsNext.size());
        }
        if (empty) {
            return false;
        }
        if (verbose) {
            System.out.println("SimpleCachingFeedsController hasnext not empty");
        }
        return true;
    }

    @Override
    public NodeId next() {
        if (verbose) {
            System.out.println("SimpleCachingFeedsController next");
        }
        if (empty) {
            // Should never get here, asking an empty iterator to produce
            //System.err.println("Bad feed in cachingFeedsController");
            return null;
        }
        CachedNodeTriple tuple = whoIsNext.poll();
        int index = tuple.dimension;
        NodeId result = tuple.nodeId;
        SimpleCachingFeedGroup group = feeds[index];
        if (group.hasNext()) {
            // Advance group
            CachedNodeTriple trip = group.next();
            key = key - (previousShifted[trip.dimension]);
            previousShifted[trip.dimension] = trip.id << shift[trip.dimension];
            key = key + previousShifted[trip.dimension];
            whoIsNext.add(trip);
        } else {
            // Group is done, controller is now empty
            empty = true;
        }
        if (verbose) {
            System.out.println("SimpleCachingFeedsController after advance " + result);
        }
        return result;
    }

    @Override
    public void remove() {
    }

}
