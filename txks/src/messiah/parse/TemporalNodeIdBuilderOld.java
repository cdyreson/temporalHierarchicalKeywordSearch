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
public class TemporalNodeIdBuilderOld<T> extends NodeIdBuilder<T> implements ParserListener {

    private final Stack<Time> timeStack; // The stack holds the current time at each level
    private boolean isRootElement;
    private final IntervalGenerator intervalGenerator;
    private Time currentTime;

    public TemporalNodeIdBuilderOld(T factory, IntervalGenerator i) {
        super(factory);
        intervalGenerator = i;
        isRootElement = false;
        timeStack = new Stack();
        timeStack.push(new Time());
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
                currentTime = timeStack.pop(); // Get the next id at this level
                return;
            }
            // This is the start of an attribute
            Time me = timeStack.pop(); // Get the next id at this level
            //System.out.println("Curt: NodeIdBuilder attribute id is " + me + " for @" + str);
            currentTime = me;
            // Set up the next one at my level
            timeStack.push(intervalGenerator.generate(timeStack.peek()));
            // Set up my child's info (a value node)
            timeStack.push(intervalGenerator.generate(me));
            return;
        }
        if (isValue) {
            // Is a text element, set up the next value
            Time me = timeStack.pop(); // Get the next id at this level
            //System.out.println("Curt: NodeIdBuilder text id is " + me + " for " + str);
            currentTime = me;
            // Set up the next one at my level
            timeStack.push(intervalGenerator.generate(timeStack.peek()));
            // Set up my child's info (a value node)
            return;
        }
        // This is the start of an element
        if (isRootElement) {
            timeStack.push(new Time());
            currentTime = new Time();
            //System.out.println("Curt: NodeIdBuilder root element " + str);
            isRootElement = false;
        } else {
            Time me = timeStack.pop(); // Get the next idat this level
            //System.out.println("Curt: NodeIdBuilder element my id is " + me + " for " + str);
            currentTime = me;
            // Set up the next one at my level
            timeStack.push(intervalGenerator.generate(timeStack.peek()));
            // Set up the first child at this level
            timeStack.push(intervalGenerator.generate(me));
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
            return;
        }
        //System.out.println("Curt: NodeIdBuilder end attribute ");
    }

    /**
     * Gets the NodeId for the current node
     *
     * @return Current node's NodeId
     */
    public NodeId getNodeId() {
        //return stack.peek();
        HistoryDLN nodeId = (HistoryDLN)super.getNodeId();

        nodeId.setTime(currentTime);
                System.out.println("TemporalNodeIdBuilder: " + nodeId);
        return nodeId;
    }

    public int getLevel() {
        //return stack.peek().getLevel(); //this.curLevel;
        return super.getLevel();
    }

    /*
    * Not yet implemented
     */
    void setId(NodeId itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
