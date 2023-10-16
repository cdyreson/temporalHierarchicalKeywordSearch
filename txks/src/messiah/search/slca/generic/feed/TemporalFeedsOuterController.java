package messiah.search.slca.generic.feed;

import java.util.*;
import messiah.Config;
import messiah.database.Database;
import usu.NodeId;
import usu.algebra.KeywordSearchExpression;
import usu.dln.DLN;
import usu.dln.HistoryDLN;
import usu.dln.TimeElementHistoryDLN;
//import usu.temporal.Time;
import usu.temporal.TimeElement;

/**
 * The TemporalFeedsOuterController gathers timestamps for the
 * TemporalFeedsController.
 *
 * @author Curt
 */
public class TemporalFeedsOuterController implements Iterator<NodeId> {

    private final TemporalFeedsController innerController;
    private final boolean verbose = false;
    private NodeId[] lcas;
    private NodeId[][] lcaWindow;
    private TimeElement[][] elements;
    //private Database db;
    private short dimensions;
    private KeywordSearchExpression exp;
    private TimeElementHistoryDLN candidate = null;
    private boolean empty = false;

    public TemporalFeedsOuterController(Database db) {
        //this.db = db;
        innerController = new TemporalFeedsController(db);
    }

    public int initialize(KeywordSearchExpression exp) {
        this.exp = exp;
        String[] query = exp.getKeywordsAsArray();
        dimensions = (short) query.length;
        lcas = new NodeId[Config.MAX_DEPTH];
        for (int i = 0; i < dimensions; i++) {
            lcas[i] = null;
        }
        lcaWindow = new NodeId[Config.MAX_DEPTH][];
        elements = new TimeElement[Config.MAX_DEPTH][];
        for (int i = 0; i < Config.MAX_DEPTH; i++) {
            //elements[i] = new TimeElement[dimensions];
            //lcaWindow[i] = new NodeId[dimensions];
            lcas[i] = null;
        }
        return innerController.initialize(exp);
    }

    @Override
    public NodeId next() {
        // Get more candidates
        while (candidate == null) {
            if (innerController.hasNext()) {
                NodeId slca = innerController.getCandidateSLCA();
                innerController.next(); // Advance it
                int level = slca.getLevel();
                NodeId currentSlca = lcas[level];
                if (currentSlca == null) {
                    // We don't yet have an SLCA so add this one and continue
                    lcas[level] = slca;
                    NodeId[] nodes = innerController.getCurrentNodeIds();
                    if (lcaWindow[level] == null) {
                        lcaWindow[level] = nodes;
                        elements[level] = new TimeElement[dimensions];
                        for (int i = 0; i < dimensions; i++) {
                            elements[level][i] = new TimeElement(((HistoryDLN) nodes[i]).getTime());
                        }
                    } else {
                        for (int i = 0; i < dimensions; i++) {
                            if (verbose) System.out.println("TemporalFeedsOuterController: node is " + nodes[i]);
                            if (!lcaWindow[level][i].equals(nodes[i])) {
                                elements[level][i].add(((HistoryDLN) nodes[i]).getTime());
                            }
                        }
                    }
                } else {
                    // We have an slca at this level, check if it is the same
                    if (currentSlca.equals(slca)) {
                        // The same, extend the timestamps
                        NodeId[] nodes = innerController.getCurrentNodeIds();
                        for (int i = 0; i < dimensions; i++) {
                            if (!lcaWindow[level][i].equals(nodes[i])) {
                                elements[level][i].add(((HistoryDLN) nodes[i]).getTime());
                            }
                        }
                    } else {
                        // Not the same, emit the old slca as a candidate
                        // and build up the new slca at this level

                        // First see if we have a viable SLCA
                        TimeElement time = exp.evaluate(elements[level]);
                        if (!time.isEmpty()) {
                            // Create the candidate
                            candidate = new TimeElementHistoryDLN((DLN) lcas[level], time);
                        }

                        // Create the new slca
                        lcas[level] = slca;
                        NodeId[] nodes = innerController.getCurrentNodeIds();
                        lcaWindow[level] = nodes;
                        for (int i = 0; i < dimensions; i++) {
                            elements[level][i] = new TimeElement(((HistoryDLN) nodes[i]).getTime());
                        }
                    }
                }
            } else {
                // We have no more from the inner controller
                // Go through the levels from bottom to top and emit any
                // leftovers
                for (int i = Config.MAX_DEPTH - 1; i >= 0; i--) {
                    if (lcas[i] != null) {
                        NodeId nodeId = lcas[i];
                        lcas[i] = null;
                        TimeElement time = exp.evaluate(elements[i]);
                        if (!time.isEmpty()) {
                            // Create the candidate
                            candidate = null;
                            return new TimeElementHistoryDLN((DLN) nodeId, time);
                        }
                    }
                }
                empty = true;
                candidate = null;
                return null;
            }
        }
        NodeId toReturn = candidate;
        candidate = null;
        return toReturn;
    }

    /* 
     fragments returns the fragments of the current CachedNodeTriple that do not
     overlap the given lifetime, and the fragment must be at the given level.
     */
    public void startFeeds() {
        innerController.startFeeds();
    }

    /* 
     * Does this iterator have a next value?
     */
    @Override
    public boolean hasNext() {
        if (verbose) {
            System.out.println("TemporalFeedsOuterController hasnext empty? " + empty);
        }
        if (empty) {
            return false;
        }
        if (verbose) {
            System.out.println("TemporalFeedsOuterController hasnext not empty");
        }
        return true;
    }

    @Override
    public void remove() {
    }

}
