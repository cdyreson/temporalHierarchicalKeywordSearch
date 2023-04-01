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
 * A TwinedFeed provides lists of nodes that match at a particular level
 *
 * @author Curt
 */
public class TwinedFeed {

    private boolean verbose = false;

    private TwinedFeed twin;
    private boolean haveTwin = false;
    private Iterator<NodeId> myFeed;
    private boolean isEmpty = false;
    private int myLevel = 0;
    private NodeId currentNodeId;
    List<NodeId> nodes;

    public TwinedFeed(Iterator<NodeId> feed, int level) {
        haveTwin = false;
        myFeed = feed;
        isEmpty = !feed.hasNext();
        myLevel = level;

        // initialize the feed
        if (!isEmpty) {
            currentNodeId = feed.next().getAncestor(myLevel);
        }
    }

    public void addTwin(TwinedFeed twin) {
        if (verbose) {
            System.out.println("TwinedFeed addTwin ");
        }
        this.twin = twin;
        haveTwin = true;
    }

    public boolean hasNext() {
        if (verbose) {
            System.out.println("TwinedFeed hasNext " + myFeed.hasNext());
        }
        return !isEmpty;
    }
    
    public NodeId getTarget() {
        return currentNodeId;
    }

    public boolean nextAt(NodeId targetNodeId) {
        if (verbose) {
            System.out.println("TwinedFeed nextAt: " + targetNodeId);
        }

        // Check to see if there are any more for me
        if (isEmpty) {
            // Should never get here, did not check hasNext() first
            //System.err.println("Bad feed in TwinedFeed");
            return false;
        }

        while (true) {
            nodes = new ArrayList(); // List of nodes at this level
            while (true) {
                int compare = currentNodeId.compareTo(targetNodeId);
                if (compare == 0) {
                    // collect all the nodes that are at this node
                    nodes.add(currentNodeId);
                } else if (compare == -1) {
                    // This node is before the target, do nothing but continue
                } else {
                    // This node is after the target so exit loop
                    break;
                }
                if (myFeed.hasNext()) {
                    currentNodeId = myFeed.next().getAncestor(myLevel);
                } else {
                    currentNodeId = null;
                    isEmpty = true;
                    break;
                }
            }
            // Check that we added one
            if (nodes.isEmpty()) {
                return false;
            } else if (haveTwin) {
                return twin.nextAt(targetNodeId);
            } else {
                return true;
            }
        }
    }

    /*
    public NodeId next() {
        if (verbose) {
            System.out.println("TwinedFeed next: " + hasNext());
        }

        // Check to see if there are any more for me
        if (isEmpty) {
            // Should never get here, did not check hasNext() first
            //System.err.println("Bad feed in TwinedFeed");
            return null;
        }

        while (true) {
            if (!twin.hasNext()) {
                return null;
            }
            NodeId twinNodeId = twin.next();
            List<NodeId> nodes = new ArrayList(); // List of nodes at this level
            while (myFeed.hasNext()) {

                int compare = currentNodeId.compareTo(twinNodeId);
                if (compare == 0) {
                    // collect all the nodes that are at this node
                    nodes.add(currentNodeId);

                } else if (compare == -1) {
                    // This node is before the twin continue
                    currentNodeId = myFeed.next().getAncestor(myLevel);
                } else {
                    // This node is after the twin advance twin and continue
                    //twin.skipTo(currentNodeId);
                    twinNodeId = twin.next();
                }
            }
            if (currentNodeId.lessThan(twinNodeId)) {

            }
        }
    }
*/
}
