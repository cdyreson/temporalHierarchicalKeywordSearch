package usu.dln;

import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
import usu.NodeId;
import usu.PathId;
import usu.temporal.*;

/**
 * Represents a node id in the form of a dynamic level number (DLN). DLN's are
 * hierarchical ids, which borrow from Dewey's decimal classification. Examples
 * for node ids: 1, 1.1, 1.2, 1.2.1, 1.2.2, 1.3. In this case, 1 represents the
 * root node, 1.1 is the first node on the second level, 1.2 the second, and so
 * on.
 *
 * To support efficient insertion of new nodes between existing nodes, we use
 * the concept of sublevel ids. Between two nodes 1.1 and 1.2, a new node can be
 * inserted as 1.1/1, where the / is the sublevel separator. The / does not
 * start a new level. 1.1 and 1.1/1 are thus on the same level of the tree.
 *
 * In the binary encoding, the '.' is represented by a 0-bit while '/' is
 * written as a 1-bit.
 */
public class HistoryDLN extends DLN implements PathId, NodeId, Serializable, Comparable {

    protected transient static final Time ALL_OF_TIME = new Time();
    protected Time timestamp = ALL_OF_TIME;
    //private static int count = 1;

    protected transient static final HistoryDLN ROOT_NODE = new HistoryDLN(1);
    // Establish a node that is last in the ordering, greater than the root node
    protected transient static final HistoryDLN MAX_NODE = new HistoryDLN(2);
    // Establish a node that is first in the ordering, greater than the root node
    protected transient static final HistoryDLN MIN_NODE = new HistoryDLN(0);

    boolean isMoved = false;
    TimeAndLevelList<Time> times = null;

    public HistoryDLN() {
        super();
        this.timestamp = ALL_OF_TIME;
    }

    public HistoryDLN(HistoryDLN dln) {
        //System.out.println("Curt: DLN " + dln + " bitIndex " + dln.bitIndex);
        super((DLN) dln);
        this.timestamp = dln.timestamp;
        //System.out.println("Curt: DLN2 " + this + " bitIndex " + bitIndex);
    }

    public HistoryDLN(DLN dln, Time t) {
        //System.out.println("Curt: DLN " + dln + " bitIndex " + dln.bitIndex);
        super((DLN) dln);
        this.timestamp = t;
        this.isMoved = false;
        //System.out.println("Curt: DLN2 nm " + this + " bitIndex " + bitIndex);
    }

    public HistoryDLN(DLN dln, Time t, boolean moved, int level) {
        //System.out.println("Curt: DLN " + dln + " bitIndex " + dln.bitIndex);
        super((DLN) dln);
        this.timestamp = t;
        isMoved = moved;
        if (isMoved) {
            times = new TimeAndLevelList();
            times.add(t, level);
        }
        //System.out.println("Curt: DLN2 mv " + this + " bitIndex " + bitIndex);
    }

    public HistoryDLN(short units, byte[] data, int startOffset) {
        super(units, data, startOffset);
        this.timestamp = ALL_OF_TIME;
    }

    public HistoryDLN(byte[] data, short nbits) {
        super(data, nbits);
        this.timestamp = ALL_OF_TIME;
    }

    public HistoryDLN(byte[] data, short nbits, Time time) {
        super(data, nbits);
        this.timestamp = time;
    }

    /**
     * Constructs a new DLN by parsing the string argument. In the string,
     * levels are separated by a '.', sublevels by a '/'. For example, '1.2/1'
     * or '1.2/1.2' are valid ids.
     *
     * @param s
     */
    public HistoryDLN(String s) {
        super(s);
        this.timestamp = ALL_OF_TIME;
    }

    /**
     * Constructs a new DLN, using the passed id as its single level value.
     *
     * @param id
     */
    public HistoryDLN(int id) {
        super(id);
        this.timestamp = ALL_OF_TIME;
    }


    /**
     * Compute the common lca at this level (or below)
     *
     * @return common lca, or null if none
     */
    public HistoryDLN commonLCA(HistoryDLN other, int level) {
        if (timestamp.overlaps(other.timestamp)) {
            return new HistoryDLN(super.commonLCA(other, level), timestamp.intersectionIfOverlaps(other.timestamp));
        }
        return null;
        //System.out.println("Has Common LCA " + result + " " + bitCount + " " + level + " " + otherBitCount + " " + this.toBitString() + " " + other.toBitString() + " " + this.toString() + " " + other.toString());

    }

    public Time getTime(int level) {
        if (isMoved) {
            return this.times.getTime(level);
        }
        return this.timestamp;
    }

    public void setTime(Time t) {
        this.timestamp = t;
        //System.out.println("Node is in HistoryDLN " + this);
    }
    
    public void setTime(TimeItem t) {
        if (t.isMoved()) {
            this.isMoved = true;
            this.times = t.getTimes();
        } else {
            this.timestamp = t.getTime();
        }
        
        //System.out.println("Node is in HistoryDLN " + this);
    }

    @Override
    public String debug() {
        return super.debug() + timestamp.debug();
    }

    @Override
    public String toString() {
        if (isMoved) {
            return super.toString() + times.toString();
        }
        if (timestamp == null) {
            return super.toString() + " null timestamp ";
        }
        return super.toString() + timestamp.toString();
    }

    @Override
    public String toBitString() {
        StringBuilder buf = new StringBuilder();
        int len = bits.length; // - SIZE_OF_BITINDEX;
        for (int i = 0; i < len; i++) {
            buf.append(toBitString(bits[i]));
        }
        return buf.toString();
    }

    /**
     * Returns a new DLN representing the first child node of this node.
     *
     * @return new child node id
     */
    @Override
    public HistoryDLN newChild() {
        return new HistoryDLN(super.newChild(), this.timestamp);
        //return new HistoryDLN(super.newChild(), new Time(count, count));
    }

    /**
     * Returns a new DLN representing the next following sibling of this node.
     *
     * @return new sibling node id.
     */
    @Override
    public HistoryDLN nextSibling() {
        //count++;
        return new HistoryDLN(super.nextSibling(), this.timestamp);
        //return new HistoryDLN(super.nextSibling(), new Time(count, count));
    }

    @Override
    public HistoryDLN precedingSibling() {
        return new HistoryDLN(super.precedingSibling(), this.timestamp);
    }

    /*
     public DLN getChild(int child) {
     DLN nodeId = new DLN(this);
     nodeId.addLevelId(child, false);
     return nodeId;
     }
     */
    @Override
    public HistoryDLN insertNode(NodeId right) {
        return insertNode((HistoryDLN) right);
    }

    @Override
    public HistoryDLN insertNode(PathId right) {
        return insertNode((HistoryDLN) right);
    }

    public HistoryDLN insertNode(HistoryDLN right) {
                if (timestamp.overlaps(right.timestamp)) {
            return new HistoryDLN(super.insertNode((DLN) right), this.timestamp.intersectionIfOverlaps(right.timestamp));
        }
        return null;
        
    }

    @Override
    public HistoryDLN insertBefore() {
        return new HistoryDLN(super.insertBefore(), this.timestamp);
    }

    // Not used
    public HistoryDLN append(HistoryDLN otherId) {
        return null;
    }

    /**
     * Returns a new DLN representing the parent of the current node. If the
     * current node is the root element of the document, the method returns
     * {@link NodeId#DOCUMENT_NODE}. If the current node is the document node,
     * null is returned.
     *
     * @see NodeId#getParentId()
     */
    @Override
    public HistoryDLN getParentId() {
        return new HistoryDLN(super.getParentId(), this.timestamp);
    }

    /*REMOVED, DONT CARE ABOUT TIMESTAMP 
     @Override
     public boolean isDescendantOf(NodeId ancestor) {
     return isDescendantOf((HistoryDLN) ancestor);
     }

     @Override
     public boolean isDescendantOf(PathId ancestor) {
     return isDescendantOf((HistoryDLN) ancestor);
     }

     public boolean isAncestorOrDescendant(HistoryDLN other) {
     return timestamp.overlaps(other.timestamp) &&  super.isAncestorOrDescendant((DLN) other);
     }

     public boolean isDescendantOf(HistoryDLN other) {
     return timestamp.overlaps(other.timestamp) &&  super.isDescendantOf((DLN) other);
     }

     @Override
     public boolean isDescendantOrSelfOf(NodeId ancestor) {
     return isDescendantOrSelfOf((HistoryDLN) ancestor);
     }

     @Override
     public boolean isDescendantOrSelfOf(PathId ancestor) {
     return isDescendantOrSelfOf((HistoryDLN) ancestor);
     }

     public boolean isDescendantOrSelfOf(HistoryDLN other) {
     return timestamp.overlaps(other.timestamp) && super.isDescendantOrSelfOf((DLN) other);
     }

     @Override
     public boolean isChildOf(NodeId other) {
     return isChildOf((HistoryDLN) other);
     }

     @Override
     public boolean isChildOf(PathId other) {
     return isChildOf((HistoryDLN) other);
     }

     public boolean isChildOf(HistoryDLN other) {
     return timestamp.overlaps(other.timestamp) && super.isChildOf((DLN) other);
     }

     @Override
     public int computeRelation(NodeId other) {
     return computeRelation((HistoryDLN) other);
     }

     @Override
     public boolean isSiblingOf(NodeId sibling) {
     return isSiblingOf((HistoryDLN) sibling);
     }

     @Override
     public boolean isSiblingOf(PathId sibling) {
     return isSiblingOf((HistoryDLN) sibling);
     }

     public boolean isSiblingOf(HistoryDLN other) {
     return timestamp.overlaps(other.timestamp) && super.isSiblingOf((DLN) other);
     }

     @Override
     public boolean after(NodeId other, boolean isFollowing) {

     return after((HistoryDLN) other, isFollowing);
     }

     @Override
     public boolean after(PathId other, boolean isFollowing) {
     return after((HistoryDLN) other, isFollowing);
     }

     public boolean after(HistoryDLN other, boolean isFollowing) {
     return timestamp.overlaps(other.timestamp) && super.after((DLN) other, isFollowing);
     }

     @Override
     public boolean before(NodeId other, boolean isPreceding) {
     return before((HistoryDLN) other, isPreceding);
     }

     @Override
     public boolean before(PathId other, boolean isPreceding) {
     return before((HistoryDLN) other, isPreceding);
     }

     public boolean before(HistoryDLN other, boolean isPreceding) {
     return timestamp.overlaps(other.timestamp) && super.before((DLN) other, isPreceding);
     }

     END OF REMOVED, DONT CARE ABOUT TIMESTAMP */
    public static HistoryDLN getRootId() {
        return HistoryDLN.ROOT_NODE;
    }

    public static HistoryDLN getMaxId() {
        return HistoryDLN.MAX_NODE;
    }

    public static HistoryDLN getMinId() {
        return HistoryDLN.MIN_NODE;
    }


    /*
     public boolean hasCommonAncestor(long id1, long id2, int level) {
     return (id1 ^ id2) < lValues[level].value;
     }
     */
    public HistoryDLN getAncestor(int level) {
        return new HistoryDLN(super.getAncestor(level), this.timestamp);
    }

    /**
     * Gets the first descendant of the next node after the ancestor at the
     * given level. This binary dewey could be used as the upper bound
     * (exclusive) of the range of descendant leaf nodes under the ancestor at
     * the given level.
     *
     * @param binaryDewey
     * @param level
     * @return
     */
    public HistoryDLN getNextFirstDescendant(int level) {
        return new HistoryDLN(super.getNextFirstDescendant(level), this.timestamp);
    }

    @Override
    public int compareTo(Object other) {
        //return compareTo((HistoryDLN) other);
        return super.compareTo((DLN) other);
    }

    @Override
    public int compareTo(NodeId other) {
        //System.out.println("other NodeId is " + other.getClass());
        return compareTo((HistoryDLN) other);
    }

    @Override
    public int compareTo(PathId other) {
        //System.out.println("other PathId is " + other.getClass());
        return compareTo((HistoryDLN) other);
    }

    public int compareTo(HistoryDLN other) {
        //System.out.println("other HistoryId is " + this.toString() + " " + other.toString());
        int i = super.compareTo((DLN) other);
        if (i != 0) {
            return i;
        }
        return timestamp.compareTo(other.timestamp);
    }

    //public static NodeId deserialize(byte[] b) {
    //    return new NodeId(bits);
    //return n;
    //}
    public static void main(String[] args) {
        HistoryDLN id0 = new HistoryDLN("1.1.7");
        HistoryDLN id1 = new HistoryDLN("1.1.6");
        HistoryDLN id2 = new HistoryDLN("1.1.7.1");
        HistoryDLN id3 = new HistoryDLN("1.1.7/1");
        HistoryDLN id4 = (HistoryDLN) id0.newChild();
        HistoryDLN id5 = (HistoryDLN) id0.nextSibling();

        System.out.println(id4.toString());
        System.out.println(id5.toString());
        System.out.println(id0.debug());
        System.out.println(id1.debug());
        System.out.println(id2.debug());
        System.out.println(id0.toString() + " -> " + id1.toString() + ": " + id0.isSiblingOf(id1));
        System.out.println(id1.toString() + " -> " + id0.toString() + ": " + id1.isSiblingOf(id0));
        System.out.println(id2.toString() + " -> " + id0.toString() + ": " + id2.isSiblingOf(id0));
        System.out.println(id0.toString() + " -> " + id2.toString() + ": " + id0.isSiblingOf(id2));
        System.out.println(id3.toString() + " -> " + id0.toString() + ": " + id3.isSiblingOf(id0));
        System.out.println(id0.toString() + " -> " + id3.toString() + ": " + id0.isSiblingOf(id3));
    }
}
