package messiah.parse;

import usu.NodeId;
import java.util.*;
import usu.NodeIdFactory;

/**
 * This is the class in charge of building the node IDs during parsing. It uses
 * the ParserListener interface to listen to parsing event
 *
 * @author John Truong Ba Quan, Curtis Dyreson
 */
public class NodeIdBuilder<T> implements ParserListener {

    protected NodeId currentNodeId; // This is the current id
    private final Stack<NodeId> stack; // The stack holds the next id at each level
    private boolean isRootElement;
    private final NodeIdFactory nodeIdFactory;

    public NodeIdBuilder(T factory) {
        isRootElement = false;
        stack = new Stack();
        this.nodeIdFactory = (NodeIdFactory) factory;
        stack.push(nodeIdFactory.rootId());
    }

    public NodeIdBuilder(T factory, NodeId nodeId) {
        isRootElement = false;
        stack = new Stack();
        this.nodeIdFactory = (NodeIdFactory) factory;
        stack.push(nodeIdFactory.rootId());
    }

    @Override
    public void startDocument() {
    }

    @Override
    public void endDocument() {
    }

    @Override
    public void start(String str, boolean isAttribute, boolean isValue) {
        // Is it an attribute?
        if (isAttribute) {
            if (isValue) {
                // Get my value
                NodeId me = stack.pop(); // Get the next id at this level
                //System.out.println("Curt: NodeIdBuilder attribute value id is " + me + " for " + str);
                currentNodeId = me;
                return;
            }
            // This is the start of an attribute
            NodeId me = stack.pop(); // Get the next id at this level
            //System.out.println("Curt: NodeIdBuilder attribute id is " + me + " for @" + str);
            currentNodeId = me;
            // Set up the next one at my level
            stack.push(me.nextSibling());
            // Set up my child's info (a value node)
            stack.push(me.newChild());
            return;
        }
        if (isValue) {
            // Is a text element, set up the next value
            NodeId me = stack.pop(); // Get the next id at this level
            //System.out.println("Curt: NodeIdBuilder text id is " + me + " for " + str);
            currentNodeId = me;
            // Set up the next one at my level
            stack.push(me.nextSibling());
            // Set up my child's info (a value node)
            return;
        }
        // This is the start of an element
        if (isRootElement) {
            stack.push(nodeIdFactory.rootId());
            currentNodeId = nodeIdFactory.rootId();
            //System.out.println("Curt: NodeIdBuilder root element " + str);
            isRootElement = false;
        } else {
            NodeId me = stack.pop(); // Get the next idat this level
            //System.out.println("Curt: NodeIdBuilder element my id is " + me + " for " + str);
            currentNodeId = me;
            // Set up the next one at my level
            stack.push(me.nextSibling());
            // Set up the first child at this level
            stack.push(me.newChild());
        }
    }

    @Override
    public void end(boolean isAttribute, boolean isValue) {
        if (isValue) {
            //System.out.println("Curt: NodeIdBuilder end value ");
            return;
        }
        if (!isAttribute) {
            //System.out.println("Curt: NodeIdBuilder end element ");
            // All done at this level, curNodeId will be restablished
            stack.pop();
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
        return currentNodeId;
    }

    public int getLevel() {
        //return stack.peek().getLevel(); //this.curLevel;
        return currentNodeId.getLevel();
    }

    /*
    * Not yet implemented
    */
    void setId(NodeId itemId) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}
