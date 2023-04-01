/*
 * The CachingFeed operator implements a feed that caches the member we are looking at
 */
package messiah.search.slca.generic.algebra;

import java.util.*;
import usu.NodeId;
import usu.dln.HistoryDLN;
import usu.temporal.TimeElement;

/**
 *
 * The CachingFeed implements a feed that caches the member we are looking at
 *
 * @author Curt
 */
public class CachingFeed implements Operator {

    private boolean verbose = false;

    private Iterator<NodeId> iter;
    private int levelOfPathLCA;
    // Records the current LCA
    private NodeId current;
    // Records the current NodeId
    private NodeId currentNodeId;
    // The feed is empty if the iterator has no more nodes
    private boolean empty;
    //private List<NodeId> cache;

    public CachingFeed(Iterator<NodeId> iter, int level) {
        if (verbose) {
            System.out.println("CachingFeed new " + level);
        }
        this.iter = iter;
        levelOfPathLCA = level - 1;
        //clearCache();
        advance();
    }

    //private void clearCache() {
    //    cache = new ArrayList(1);
    //}

    /*
     Call this to set up the cached feed, level isn't known till later
     */
    //public void initialize(int level) {
    //    levelOfPathLCA = level;
    //    advance();
    //}

    /*
     Advance the iterator by one
     */
    protected void advance() {
        if (verbose) {
            System.out.println("CachingFeed advance ");
        }
        if (!iter.hasNext()) {
            if (verbose) {
                System.out.println("CachingFeed advance is empty! ");
            }
            empty = true;
            return;
        }
        currentNodeId = iter.next();
        //cache.add(currentNodeId);
        if (verbose) {
            System.out.println("CachingFeed advance next " + currentNodeId + " " + levelOfPathLCA);
        }
        current = currentNodeId.getAncestor(levelOfPathLCA);
    }

    /* 
     Advance this iterator to the given LCA.
    
     Returns true (hasMore nodes)
     or false (no more nodes)
     */
    public boolean advanceToLCA(NodeId lca) {
        if (verbose) {
            System.out.println("CachingFeed advanceToLCA: " + lca + " " + current);
        }
        if (empty) {
            // Iterator is empty
            return false;
        }
        if (lca.lessThanOrEqualTo(current)) {
            // Already here or past 
            //clearCache();
            //cache.add(currentNodeId);
            return true;
        }
        if (!iter.hasNext()) {
            empty = true;  // Iterator is empty
            return false;
        }
        currentNodeId = iter.next();

        current = currentNodeId.getAncestor(levelOfPathLCA);
        return advanceToLCA(lca);
    }

    /* 
     Advance this iterator past the given id, stop
     when advanced past it.  Does nothing if the
     iterator is already past or equal to it.
    
     Returns true (hasMore nodes)
     or false (no more nodes)
     */
    @Override
    public boolean advancePast(NodeId lca) {
        if (empty) {
            // Iterator is empty
            return false;
        }
        if (lca.lessThan(current)) {
            // Already past 
            //clearCache();
            //cache.add(currentNodeId);
            return true;
        }
        if (!iter.hasNext()) {
            empty = true;  // Iterator is empty
            return false;
        }
        currentNodeId = iter.next();
        current = currentNodeId.getAncestor(levelOfPathLCA);
        return advancePast(lca);
    }

    /* 
     Put all of the Time objects under the current LCA into
     an Element.
     */
    public TimeElement gather() {
        TimeElement element = new TimeElement();
        //System.out.println("CachingFeed: gathering " + current);
        NodeId old = current; // old is the old LCA
        do {
            HistoryDLN hDLN = (HistoryDLN) currentNodeId;
            //System.out.println("CachingFeed: getting time " + hDLN + " " + current + " " + old);
            element.add(hDLN.getTime());
            if (empty) {
                // Iterator is empty
                return element;
            }
            advance();
            //System.out.println("CachingFeed: checking time " + current + " " + old + " " + current.isEqualTo(old));
        } while (current.isEqualTo(old));
        return element;
    }

    /*
     Peek at the current LCA node in the sequence
     */
    public NodeId peekAtLCA() {
        if (verbose) {
            System.out.println("CachingFeed peekAtLCA: " + current);
        }
        return current;
    }

    /*
     Peek at the current node in the sequence
     */
    public NodeId peek() {
        if (verbose) {
            System.err.println("peek is not implemented");
        }
        return currentNodeId;
    }

    /* 
     Do we have more NodeIds?  
     */
    public boolean hasMore() {
        if (verbose) {
            System.out.println("CachingFeed hasMore: " + empty + " " + iter.hasNext());
        }

        if (empty) {
            return false;
        }
        if (!iter.hasNext()) {
            return false;
        }
        return true;
    }

    /* 
     Set the level at which LCAs are produced  
     */
    public void setLevel(int level) {
        levelOfPathLCA = level;
    }

}
