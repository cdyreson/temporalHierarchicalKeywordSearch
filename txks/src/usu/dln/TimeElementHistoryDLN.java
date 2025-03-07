package usu.dln;

import java.io.Serializable;
import java.util.Set;
import usu.NodeId;
import usu.PathId;
import usu.temporal.*;

/**
 * A DLN together with a temporal element.
 */
public class TimeElementHistoryDLN extends DLN implements PathId, NodeId, Serializable, Comparable {

    protected transient static final TimeElement ALL_OF_TIME = new TimeElement(new Time(0, 100));
    //private static int count = 1;

    protected transient static final TimeElementHistoryDLN ROOT_NODE = new TimeElementHistoryDLN(1);
    // Establish a node that is last in the ordering, greater than the root node
    protected transient static final TimeElementHistoryDLN MAX_NODE = new TimeElementHistoryDLN(2);
    // Establish a node that is first in the ordering, greater than the root node
    protected transient static final TimeElementHistoryDLN MIN_NODE = new TimeElementHistoryDLN(0);
    
    TimeElement element;

    boolean isMoved = false;
    TimeAndLevelList<TimeElement> times = null;

    public TimeElementHistoryDLN() {
        super();
        this.element = ALL_OF_TIME;
    }

    public TimeElementHistoryDLN(TimeElementHistoryDLN dln, TimeElement time) {
        //System.out.println("Curt: DLN " + dln + " bitIndex " + dln.bitIndex);
        super((DLN) dln);
        this.element = time;
        //System.out.println("Curt TE: DLN2 " + this + " bitIndex " + bitIndex);
    }

    public TimeElementHistoryDLN(DLN dln, TimeElement t) {
        //System.out.println("Curt: DLN " + dln + " bitIndex " + dln.bitIndex);
        super((DLN) dln);
        this.element = t;
        //System.out.println("Curt: DLN2 " + this + " bitIndex " + bitIndex);
    }

    public TimeElementHistoryDLN(DLN dln, Time t) {
        //System.out.println("Curt: DLN " + dln + " bitIndex " + dln.bitIndex);
        super((DLN) dln);
        this.element = new TimeElement(t);
        //System.out.println("Curt: DLN2 " + this + " bitIndex " + bitIndex);
    }

    public TimeElementHistoryDLN(short units, byte[] data, int startOffset) {
        super(units, data, startOffset);
        this.element = ALL_OF_TIME;
    }

    public TimeElementHistoryDLN(byte[] data, short nbits) {
        super(data, nbits);
        this.element = ALL_OF_TIME;
    }

    public TimeElementHistoryDLN(byte[] data, short nbits, Time time) {
        super(data, nbits);
        this.element = new TimeElement(time);
    }

    /**
     * Constructs a new DLN by parsing the string argument. In the string,
     * levels are separated by a '.', sublevels by a '/'. For example, '1.2/1'
     * or '1.2/1.2' are valid ids.
     *
     * @param s
     */
    public TimeElementHistoryDLN(String s) {
        super(s);
        this.element = ALL_OF_TIME;
    }

    /**
     * Constructs a new DLN, using the passed id as its single level value.
     *
     * @param id
     */
    public TimeElementHistoryDLN(int id) {
        super(id);
        this.element = ALL_OF_TIME;
    }

    /**
     * Compute the common lca at this level (or below)
     *
     * @return common lca, or null if none
     */
    public TimeElementHistoryDLN commonLCA(TimeElementHistoryDLN other, int level) {
        return new TimeElementHistoryDLN(super.commonLCA(other, level), element.intersection(other.element));
        //System.out.println("Has Common LCA " + result + " " + bitCount + " " + level + " " + otherBitCount + " " + this.toBitString() + " " + other.toBitString() + " " + this.toString() + " " + other.toString());

    }

    /*
     @Override
     public int computeNCALevel(NodeId other) {
     if (!this.timestamp.overlaps(((HistoryDLN) other).timestamp)) {
     return 0;
     }
     return super.computeNCALevel((DLN) other);
     }
     */
    public TimeElement getTime(int level) {
        if (isMoved) {
            return times.getTime(level);
        }
        return this.element;
    }
    
    public HistoryDLN toHistoryDLN() {
        Set<Time> t = this.element.getTimes();
        if (t.isEmpty()) {
            return new HistoryDLN((DLN)this, new Time(0, 100));
        }    
        return new HistoryDLN((DLN)this, (Time)t.toArray()[0]);
        

    }

    /*
     public int computeNCALevel(HistoryDLN other) {
     //System.out.println("here");
     if (!this.timestamp.overlaps(other.timestamp)) {
     return 0;
     }
     return super.computeNCALevel((DLN) other);
     }

     public int computeNCALevel(int tlevel, HistoryDLN other, int olevel) {
     System.out.println("here");
     if (!this.timestamp.overlaps(other.timestamp)) {
     return 0;
     }
     return super.computeNCALevel(tlevel, (DLN) other, olevel);
     }
     */
    /**
     * Is this one equal to the other to the given level in the tree? Used to
     * check if they have a common lca at this level (or below)
     *
     * @return whether they are equal
     */
    /*
     public boolean hasCommonLCA(HistoryDLN other, int level) {
     return super.hasCommonLCA((DLN) other, level)  && timestamp.overlaps(other.timestamp) ;
     }

     public boolean equals(HistoryDLN other) {
     return super.equals((DLN) other) && timestamp.equals(other.timestamp);
     }
     */
    @Override
    public String debug() {
        return super.debug() + element.toString();
    }

    @Override
    public String toString() {
        if (element == null) {
            return super.toString() + " null timestamp ";
        }
        return super.toString() + element.toString();
    }

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
    public TimeElementHistoryDLN newChild() {
        return new TimeElementHistoryDLN(super.newChild(), this.element);
        //return new HistoryDLN(super.newChild(), new Time(count, count));
    }

    /**
     * Returns a new DLN representing the next following sibling of this node.
     *
     * @return new sibling node id.
     */
    @Override
    public TimeElementHistoryDLN nextSibling() {
        //count++;
        return new TimeElementHistoryDLN(super.nextSibling(), this.element);
        //return new HistoryDLN(super.nextSibling(), new Time(count, count));
    }

    @Override
    public TimeElementHistoryDLN precedingSibling() {
        return new TimeElementHistoryDLN(super.precedingSibling(), this.element);
    }

    /*
     public DLN getChild(int child) {
     DLN nodeId = new DLN(this);
     nodeId.addLevelId(child, false);
     return nodeId;
     }
     */
    @Override
    public TimeElementHistoryDLN insertNode(NodeId right) {
        return insertNode((TimeElementHistoryDLN) right);
    }

    @Override
    public TimeElementHistoryDLN insertNode(PathId right) {
        return insertNode((TimeElementHistoryDLN) right);
    }

    public TimeElementHistoryDLN insertNode(TimeElementHistoryDLN right) {
        return new TimeElementHistoryDLN(super.insertNode((DLN) right), this.element.intersection(right.element));
    }

    @Override
    public TimeElementHistoryDLN insertBefore() {
        return new TimeElementHistoryDLN(super.insertBefore(), this.element);
    }

    // Not used
    public TimeElementHistoryDLN append(TimeElementHistoryDLN otherId) {
        return new TimeElementHistoryDLN(super.append((DLN) otherId), this.element.intersection(otherId.element));
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
    public TimeElementHistoryDLN getParentId() {
        return new TimeElementHistoryDLN(super.getParentId(), this.element);
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
    public static TimeElementHistoryDLN getRootId() {
        return TimeElementHistoryDLN.ROOT_NODE;
    }

    public static TimeElementHistoryDLN getMaxId() {
        return TimeElementHistoryDLN.MAX_NODE;
    }

    public static TimeElementHistoryDLN getMinId() {
        return TimeElementHistoryDLN.MIN_NODE;
    }


    /*
     public boolean hasCommonAncestor(long id1, long id2, int level) {
     return (id1 ^ id2) < lValues[level].value;
     }
     */
    public TimeElementHistoryDLN getAncestor(int level) {
        return new TimeElementHistoryDLN(super.getAncestor(level), this.element);
    }

    /**
     * Gets the first descendant of the next node after the ancestor at the
     * given level. This binary dewey could be used as the upper bound
     * (exclusive) of the range of descendant leaf nodes under the ancestor at
     * the given level.
     *
     * @param level
     * @return
     */
    @Override
    public TimeElementHistoryDLN getNextFirstDescendant(int level) {
        return new TimeElementHistoryDLN(super.getNextFirstDescendant(level), this.element);
    }

    @Override
    public int compareTo(Object other) {
        //return compareTo((TimeElementHistoryDLN) other);
        return super.compareTo((DLN) other);
    }
    
    public int compareTo(HistoryDLN other) {
        return super.compareTo((DLN) other);
    }

    @Override
    public int compareTo(NodeId other) {
        //System.out.println("other NodeId is " + other.getClass());
        return compareTo((TimeElementHistoryDLN) other);
    }

    @Override
    public int compareTo(PathId other) {
        //System.out.println("other PathId is " + other.getClass());
        return compareTo((TimeElementHistoryDLN) other);
    }

    public int compareTo(TimeElementHistoryDLN other) {
        //System.out.println("other HistoryId is " + this.toString() + " " + other.toString());
        int i = super.compareTo((DLN) other);
        if (i != 0) {
            return i;
        }
        int hash = element.hashCode();
        int otherHash = other.element.hashCode();
        return (hash == otherHash)? 0 : ((hash > otherHash)? 1 : -1);        
        //return element.compareTo(other.element);
    }
    
    
    //public static NodeId deserialize(byte[] b) {
    //    return new NodeId(bits);
    //return n;
    //}
    public static void main(String[] args) {
        TimeElementHistoryDLN id0 = new TimeElementHistoryDLN("1.1.7");
        TimeElementHistoryDLN id1 = new TimeElementHistoryDLN("1.1.6");
        TimeElementHistoryDLN id2 = new TimeElementHistoryDLN("1.1.7.1");
        TimeElementHistoryDLN id3 = new TimeElementHistoryDLN("1.1.7/1");
        TimeElementHistoryDLN id4 = (TimeElementHistoryDLN) id0.newChild();
        TimeElementHistoryDLN id5 = (TimeElementHistoryDLN) id0.nextSibling();

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
