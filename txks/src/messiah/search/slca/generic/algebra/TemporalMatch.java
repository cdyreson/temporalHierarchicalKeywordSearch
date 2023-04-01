/*
 * The TemporalMatch operator sices the given sequence at a particular time
 */
package messiah.search.slca.generic.algebra;

import java.util.*;
import usu.NodeId;
import usu.PathId;

/**
 *
 * The FeedGroup is a group of Feeds, which are Iterators that produce an SLCA.
 *
 * @author Curt
 */
public class TemporalMatch implements Operator {

    List<Iterator<NodeId>> tempFeedList;
    PriorityQueue<NodeIdOperatorPair> feeds;
    PathId pathLCA;
    int levelOfPathLCA;
    boolean empty;

    public TemporalMatch() {
        empty = false;
        pathLCA = null;
        empty = false;
        levelOfPathLCA = 0;
        tempFeedList = new ArrayList(3);
        feeds = new PriorityQueue();
    }

    public void setLevel(int level) {
        levelOfPathLCA = level;
    }

    public boolean hasMore() {
        return false;
    }

    public NodeId peekAtLCA() {
        return null;
    }

    public NodeId peek() {
        return null;
    }

    public boolean advancePast(NodeId nodeId) {
        while (!feeds.isEmpty()) {
            NodeIdOperatorPair feed = feeds.poll();
            if (!feed.advancePast(nodeId)) {
                //feed.lca = feed.iter.peekAtLCA();
            }
        }
        return true;
    }

    public void addFeed(Iterator<NodeId> feed, PathId pathId) {
        // Iterator is empty, will not generate anything
        if (empty) {
            return;
        }

        // Check if this feed has something, if not merged iterator is empty
        if (!feed.hasNext()) {
            empty = true;
            return;
        }

        // It is OK, add it
        System.out.println("FeedGroup adding feed " + feed.hasNext());

        // The pathLCA holds the LCA of the current heap of feeds
        if (pathLCA == null) {
            // No pathLCA, so set it up
            pathLCA = pathId;
            levelOfPathLCA = pathId.getLevel();
            // Create the PriorityQueue for this feedset
        } else {
            levelOfPathLCA = pathLCA.computeNCALevel(pathId);
            pathLCA = pathLCA.getAncestor(levelOfPathLCA);
        }

        // Add the feed to the current feed set
        //feeds.add(new NodeIdIterPair(null, feed));
        tempFeedList.add(feed);
    }

    public void start() {
        // Figure out the pathLCA for every feed
        if (empty) {
            return;
        }
        feeds = new PriorityQueue();
        for (Iterator<NodeId> feed : tempFeedList) {
            NodeId nodeId = feed.next();
            nodeId = nodeId.getAncestor(levelOfPathLCA);
            feeds.add(new NodeIdOperatorPair(nodeId, feed));
        }
    }

    public int getLevelOfPathLCA() {
        return levelOfPathLCA;
    }

    /* 
     * Advance the feeds until the LCAs are all the same
     * If can't be synched returns false
     */
    public boolean sync() {
        System.out.println("FeedGroup synching");
        if (empty) {
            return false;
        }
        NodeId maxId = computeMaxLCA();
        if (maxId == null) {
            System.out.println("Synched");
            return true;
        }
        Iterator<NodeIdOperatorPair> iter = feeds.iterator();
        while (iter.hasNext()) {
            NodeIdOperatorPair pair = iter.next();
            pair.advanceToLCA(maxId);
        }
        return sync();

    }

    /*
     * Compute the maximum LCA
     * returns null if all the same
     * returns max otherwise
     */
    public NodeId computeMaxLCA() {
        NodeId lca = null;
        boolean hasMax = false;
        Iterator<NodeIdOperatorPair> iter = feeds.iterator();
        while (iter.hasNext()) {
            NodeIdOperatorPair pair = iter.next();
            if (lca == null) {
                lca = pair.lca;
            } else {
                if (lca.isNotEqualTo(pair.lca)) {
                    hasMax = true;
                    lca = (lca.lessThan(pair.lca)) ? pair.lca : lca;
                }
            }
        }
        return (hasMax) ? lca : null;
    }


    /* 
     * Does this iterator have a next value?
     */
    public boolean hasNext() {
        System.out.println("FeedGroup hasnext ");
        if (empty) {
            System.out.println("Feed is empty");
            return false;
        }

        // See if they can be synchronized on the next thing
        return sync();
    }

    public NodeId next() {
        System.out.println("FeedGroup next");
        if (!sync()) {
            return null;
        }
        NodeIdOperatorPair pair = feeds.peek();
        return pair.lca;
    }

    /*
     * Helper class that pairs a NodeId with an Iterator. The Iterator
     * is the feed for with the NodeId, which is the LCA at that level for
     * the iterator.
     */
    private class NodeIdOperatorPair implements Comparable, Operator {

        public Iterator<NodeId> iter;
        public NodeId lca;

        public NodeIdOperatorPair(NodeId lca, Iterator<NodeId> iter) {
            this.iter = iter;
            this.lca = lca;
        }

        /* 
         Advance this iterator past the given id, stop
         when advanced past it.  Does nothing if the
         iterator is already past or equal to it.
    
         Returns true (hasMore nodes)
         or false (no more nodes)
         */
        @Override
        public boolean advancePast(NodeId id) {
            if (!iter.hasNext()) {
                empty = true;  // Iterator is empty
                return false;
            }
            while (iter.hasNext()) {
                NodeId nodeId = iter.next();
                if (nodeId.getAncestor(levelOfPathLCA).equals(lca)) {
                    // Have a good nodeId
                    return true;
                }
            }
            empty = true;  // Iterator is exhausted if got here
            return false;
        }

        /*
         Peek at the current LCA node in the sequence
         */
        public NodeId peekAtLCA() {
            return lca;
        }

        /*
         Peek at the current LCA node in the sequence
         */
        public NodeId peek() {
            return lca;
        }

        /* 
         Do we have more NodeIds?  
         */
        public boolean hasMore() {
            return false;
        }

        /* 
         Set the level at which LCAs are produced  
         */
        public void setLevel(int level) {

        }

        @Override
        public int compareTo(Object other) {
            return compareTo((NodeIdOperatorPair) other);
        }

        public int compareTo(NodeIdOperatorPair other) {
            return lca.compareTo(other.lca);
        }

        public void advanceToLCA(NodeId lca) {

        }
    }
}
