package messiah.parse;

import usu.NodeId;
import java.util.*;
import usu.dln.HistoryDLN;
import usu.temporal.Time;

/**
 * This is the class in charge of building the node IDs during parsing. It uses
 * the ParserListener interface to listen to parsing event
 *
 * @author John Truong Ba Quan, Curtis Dyreson
 * @param <T>
 */
public class TemporalNodeIdBuilder<T> extends NodeIdBuilder<T> implements ParserListener {
 private final Stack<Time> timeStack; // The stack holds the current time at each level
    private boolean isRootElement;
    private final IntervalGenerator intervalGenerator;
    //private Time currentTime;

    public TemporalNodeIdBuilder(T factory, IntervalGenerator i) {
        super(factory);
        intervalGenerator = i;
        isRootElement = false;
        timeStack = new Stack();
        timeStack.push(new Time());
    }

    @Override
    public void startDocument() {
        super.startDocument();
    }

    @Override
    public void endDocument() {
        super.endDocument();
    }

    @Override
    public void start(String str, boolean isAttribute, boolean isValue) {
        super.start(str, isAttribute, isValue);
        // Is it an attribute?
        if (isAttribute) {
            if (isValue) {
                // Get my value
                ((HistoryDLN) currentNodeId).setTime(intervalGenerator.generate(timeStack.peek()));
                return;
            }
            // This is the start of an attribute
            ((HistoryDLN) currentNodeId).setTime(intervalGenerator.generate(timeStack.peek()));
            return;
        }
        if (isValue) {
            ((HistoryDLN) currentNodeId).setTime(intervalGenerator.generate(timeStack.peek()));
            // Set up my child's info (a value node)
            return;
        }
        // This is the start of an element
        if (isRootElement) {
            timeStack.push(intervalGenerator.generate(new Time()));
            ((HistoryDLN) currentNodeId).setTime(intervalGenerator.generate(timeStack.peek()));
            //currentTime = new Time();
            //System.out.println("Curt: NodeIdBuilder root element " + str);
            isRootElement = false;
        } else {
            Time parent = timeStack.peek(); // Get the parent
            //System.out.println("Curt: NodeIdBuilder element my id is " + me + " for " + str);
            Time me = intervalGenerator.generate(parent);
            ((HistoryDLN) currentNodeId).setTime(me);
            // Set up the next one at my level
            timeStack.push(me);
        }
    }

    @Override
    public void end(boolean isAttribute, boolean isValue) {
        super.end(isAttribute, isValue);
        if (isValue) {
            //System.out.println("Curt: NodeIdBuilder end value ");
            return;
        }
        if (!isAttribute) {
            //System.out.println("Curt: NodeIdBuilder end element ");
            // All done at this level, curNodeId will be restablished
            timeStack.pop();
        }
        //System.out.println("Curt: NodeIdBuilder end attribute ");
    }

    /**
     * Gets the NodeId for the current node
     *
     * @return Current node's NodeId
     */
    /*
    @Override
    public NodeId getNodeId() {
        //System.out.println("TemporalNodeIdBuilder: " + currentNodeId);
        return currentNodeId;
    }
*/

}

