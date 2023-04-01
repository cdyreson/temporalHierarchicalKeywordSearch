/*
 * This is the critical class. What we want to do is create one list that we
 * can traverse in sorted order from the merging of iterators of several
 * lists.
 */
package messiah.search.slca.generic;

import java.util.*;
import usu.NodeId;
import usu.PathId;

/**
 *
 * The FeedGroup is a group of Feeds, which are Iterators that produce an SLCA.
 *
 * @author Curt
 */
public class FeedGroup implements Iterator {

    List<Iterator<NodeId>> tempFeedList;
    PriorityQueue<NodeIdIterPair> feeds;
    PathId pathLCA;
    int levelOfPathLCA;
    boolean empty;

    public FeedGroup() {
        empty = false;
        pathLCA = null;
        empty = false;
        levelOfPathLCA = 0;
        tempFeedList = new ArrayList(3);
        feeds = new PriorityQueue();
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
            feeds.add(new NodeIdIterPair(nodeId, feed));
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
        Iterator<NodeIdIterPair> iter = feeds.iterator();
        while (iter.hasNext()) {
            NodeIdIterPair pair = iter.next();
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
        Iterator<NodeIdIterPair> iter = feeds.iterator();
        while (iter.hasNext()) {
            NodeIdIterPair pair = iter.next();
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
    @Override
    public boolean hasNext() {
        System.out.println("FeedGroup hasnext ");
        if (empty) {
            System.out.println("Feed is empty");
            return false;
        }

        // See if they can be synchronized on the next thing
        return sync();
    }

    @Override
    public NodeId next() {
        System.out.println("FeedGroup next");
        if (!sync()) {
            return null;
        }
        NodeIdIterPair pair = feeds.peek();
        return pair.lca;
    }

    /* 
     * Remove is not implemented
     */
    @Override
    public void remove() {
    }

    /*
     * Helper class that pairs a NodeId with an Iterator. The Iterator
     * is the feed for with the NodeId, which is the LCA at that level for
     * the iterator.
     */
    private class NodeIdIterPair implements Comparable {

        public Iterator<NodeId> iter;
        public NodeId lca;

        public NodeIdIterPair(NodeId lca, Iterator<NodeId> iter) {
            this.iter = iter;
            this.lca = lca;
        }

        @Override
        public int compareTo(Object other) {
            return compareTo((NodeIdIterPair) other);
        }

        public int compareTo(NodeIdIterPair other) {
            return lca.compareTo(other.lca);
        }

        public void advanceToLCA(NodeId lca) {
            if (!iter.hasNext()) {
                empty = true;  // Iterator is empty
                return;
            }
            while (iter.hasNext()) {
                NodeId nodeId = iter.next();
                if (nodeId.getAncestor(levelOfPathLCA).equals(lca)) {
                    // Have a good nodeId
                    return;
                }
            }
            empty = true;  // Iterator is exhausted if got here
        }
    }
}
