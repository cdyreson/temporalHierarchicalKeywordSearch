/*
 * This is the critical class. What we want to do is create one list that we
 * can traverse in sorted order from the merging of iterators of several
 * lists.
 */
package messiah.search.slca.generic.algebra;

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
public class CachingFeedGroup {

    private boolean verbose = false;

    private List<Iterator<NodeId>> tempFeedList;
    private PriorityQueue<NodeIdIterPair> feeds;
    private Map<PathId,String> pathIdToString;
    private PathId pathLCA;
    private int levelOfPathLCA;
    private boolean empty;
    private int feedCount = 0;
    private TimeElement lifetime;

    public CachingFeedGroup() {
        empty = false;
        pathLCA = null;
        levelOfPathLCA = 0;
        tempFeedList = new ArrayList(3);
        pathIdToString = new HashMap();
        //feeds = new PriorityQueue();
    }

    public TimeElement getLifetime() {
        return lifetime;
    }
    
    public void addFeed(Iterator<NodeId> feed, PathId pathId, String keyword) {
                    System.out.println("CachingFeedGroup addFeed " + keyword + " " + pathId);   
        if (verbose) {
            System.out.println("CachingFeedGroup addFeed " + keyword + " " + empty);    
        }
        // Iterator is empty, will not generate anything
        if (empty) {
            return;
        }

        // Check if this feed has something, if not merged iterator is empty
        if (!feed.hasNext()) {
            if (verbose) {
                System.out.println("CachingFeedGroup addFeed empty feed!");
            }
            empty = true;
            return;
        }

        //Increment the count of the number of feeds
        feedCount++;
        
        // It is OK, add it
        //System.out.println("CachingFeedGroup adding feed " + feed.hasNext());
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

        if (verbose) {
            System.out.println("CachingFeedGroup setting level of LCA " + pathLCA + " " + levelOfPathLCA + " " + pathId);
        }
        // Add the feed to the current feed set
        //feeds.add(new NodeIdIterPair(null, feed));
        tempFeedList.add(feed);
    }

    public NodeId start() {
        if (verbose) {
            System.out.println("CachingFeedGroup start: " + empty + " " + levelOfPathLCA);
        }
        // Figure out the pathLCA for every feed
        if (empty) {
            return null;
        }
        feeds = new PriorityQueue();
        for (Iterator<NodeId> feed : tempFeedList) {
            CachingFeed cf = new CachingFeed(feed, levelOfPathLCA);
            NodeId lca = cf.peekAtLCA();
            if (lca != null) {
                if (verbose) {
                    System.out.println("CachingFeedGroup start: adding a feed " + lca);
                }
                feeds.add(new NodeIdIterPair(lca, cf));
            } else {
                if (verbose) {
                    System.out.println("CachingFeedGroup start: CachingFeed does not have more!");
                }
                empty = true;
                return null;
            }
        }
        NodeId syncId = sync();

        if (syncId == null) {
            empty = true;
        }

        if (verbose) {
            System.out.println("CachingFeedGroup start: syncId " + syncId);
        }
        return syncId;
    }

    public int getLevelOfPathLCA() {
        return levelOfPathLCA;
    }

    /* 
     * Advance the feeds until the LCAs are all the same
     * Return the synched node or null if can't be synched
     */
    public NodeId sync() {
        if (verbose) {
            System.out.println("CachingFeedGroup synching " + empty + " " + levelOfPathLCA);
        }
        if (empty) {
            return null;
        }

        NodeId lca = null;
        boolean synched = true;
        Iterator<NodeIdIterPair> iter = feeds.iterator();
        while (iter.hasNext()) {
            if (verbose) {
                System.out.println("CachingFeedGroup synching lca " + lca);
            }
            NodeIdIterPair pair = iter.next();
            if (lca == null) {
                lca = pair.lca;
            } else {
                if (lca.isNotEqualTo(pair.lca)) {
                    synched = false;
                    lca = (lca.lessThan(pair.lca)) ? pair.lca : lca;
                }
            }
        }

        if (synched) {
            if (verbose) {
                System.out.println("Synched " + lca);
            }
            // Sync was successful, gather the nodes for this one
            System.out.println("CachingFeedGroup gathering " + lca);
            lifetime = gather();
            return lca;
        }

        // Not synched, advance to current max
        iter = feeds.iterator();
        while (iter.hasNext()) {
            NodeIdIterPair pair = iter.next();
            if (!pair.iter.advanceToLCA(lca)) {
                empty = true;
                return null;
            } else {
                pair.lca = pair.iter.peekAtLCA();
            }
        }
        return sync();
    }

    /* 
     Advance the group past the given LCA
     */
    public void advancePast(NodeId lca) {
        if (empty) {
            return;
        }

        Iterator<NodeIdIterPair> iter = feeds.iterator();
        while (iter.hasNext()) {
            NodeIdIterPair pair = iter.next();
            if (!pair.iter.advancePast(lca)) {
                        if (verbose) System.out.println("CachingFeedGroup: advacePast " + lca + " empty ");
                empty = true;
                return;
            } else {
                        if (verbose) System.out.println("CachingFeedGroup: advacePast " + lca + " pairLCA " + pair.lca);
                pair.lca = pair.iter.peekAtLCA();
            }
        }
    }

    /*
    private boolean hasMax = false;
    private NodeId maxLCA = null;

    public boolean hasMax() {
        return hasMax;
    }

    public NodeId getMaxLCA() {
        return maxLCA;
    }
    */

    /* 
     Gather all of the Elements for the times associated with this
     LCA (called after a sync()) for the sequenced case.
     */
    public TimeElement gather() {
        //if (verbose) return null;
        if (verbose) {
            System.out.println("CachingFeedGroup gathering " + empty);
        }
        if (empty) {
            // Should never be empty
            //System.err.println("Bad gather, empty FeedGroup");
            //System.exit(-1);
            return new TimeElement();
        }

        Iterator<NodeIdIterPair> iter = feeds.iterator();
        TimeElement[] elements = new TimeElement[feedCount];
        int i = 0;
        while (iter.hasNext()) {
            NodeIdIterPair pair = iter.next();
            CachingFeed feed = pair.iter;
            elements[i++]=feed.gather();
        }
        
        TimeElement e = elements[0];
        for (i = 1; i < feedCount; i++) {
            e = e.intersection(elements[i]);
        }
        
        return e;

    }

    /*
     * Compute the maximum LCA
     * returns null if all the same
     * returns max otherwise
     */
    /*
     public void computeMaxLCA() {
     if (empty) {
     hasMax = false;
     maxLCA = null;
     return;
     }
     NodeId lca = null;
     hasMax = false;
     Iterator<NodeIdIterPair> iter = feeds.iterator();
     while (iter.hasNext()) {
     NodeIdIterPair pair = iter.next();
     if (lca == null) {
     lca = pair.lca;
     } else {
     if (lca.notEquals(pair.lca)) {
     hasMax = true;
     lca = (lca.lessThan(pair.lca)) ? pair.lca : lca;
     }
     }
     }
     maxLCA = (hasMax) ? lca : null;
     }
     */
    /*
     * Helper class that pairs a NodeId with an Iterator. The Iterator
     * is the feed for with the NodeId, which is the LCA at that level for
     * the iterator.
     */
    protected class NodeIdIterPair implements Comparable {

        public CachingFeed iter;
        public NodeId lca;

        public NodeIdIterPair(NodeId lca, CachingFeed iter) {
            this.iter = iter;
            this.lca = lca;
        }

        @Override
        public int compareTo(Object other) {
            //System.out.println("CCCompareTo: ");
            return compareTo((NodeIdIterPair) other);
        }

        public int compareTo(NodeIdIterPair other) {
            //System.out.println("CompareTo: " + lca.compareTo(other.lca) + " " + lca + " " + other.lca);
            return lca.compareTo(other.lca);
            /*
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
             */
        }

    }
}
