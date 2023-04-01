/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-06 The eXist Project
 *  http://exist-db.org
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  $Id: NodeId.java 7373 2008-02-08 20:07:40Z wolfgang_m $
 */
package usu.dln.old;

import usu.*;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;
import usu.dln.DLN;
//import usu.numbering.DLNBase;

//import usu.org.exist.storage.io.VariableByteOutputStream;
//import java.io.IOException;
//import usu.org.exist.numbering.DLN;
/**
 * Represents the internal id of a node within eXist. Basically, all stored
 * nodes in eXist need to have an id that implements this interface. The id will
 * be assigned according to used numbering scheme. From a given id, we can
 * determine the relationship of the node it represents to any other node in the
 * same document.
 */
public class OldPathId extends DLN implements Comparable, Serializable {

    /**
     * Static field representing the document node.
     */
    //public transient final static int IS_CHILD = 1;
    //public transient final static int IS_DESCENDANT = 2;
    //public transient final static int IS_SELF = 3;
    //public transient static NodeId DOCUMENT_NODE = new NodeId(0,1);
    //public transient static NodeId END_OF_DOCUMENT = new NodeId(0,1);
    private transient static final OldPathId ROOT_NODE = new OldPathId(1);
    // Establish a node that is last in the ordering, greater than the root node
    private transient static final OldPathId MAX_NODE = new OldPathId(2);
    // Establish a node that is first in the ordering, greater than the root node
    private transient static final OldPathId MIN_NODE = new OldPathId(0);
    
    public OldPathId(byte[] bits, int j) {
        super(bits, (short)j);
    }

    public OldPathId(DLN dln) {
        super(dln);
        //System.out.println("Curt: xxx " + dln);
    }

    public OldPathId(int i) {
        super(i);
        //System.out.println("Curt: xxx " + dln);
    }

    public static OldPathId getRootId() {
        return ROOT_NODE;
    }

    public static OldPathId getMaxId() {
        return MAX_NODE;
    }

    public static OldPathId getMinId() {
        return MIN_NODE;
    }

    public static void initializeLValues(int m, long levelOrder) {
    }

    public static void initializeLValues(int m, long[] levelOrder) {
    }

    private static void initializeLevelMap() {
    }

    public static void redoLValues(int m, long levelOrder) {
    }

    /**
     * Compute the common lca at this level (or below)
     *
     * @return common lca, or null if none
     */
    public int computeNCALevel(OldPathId other) {
        int tlevel = this.getLevel();
        int olevel = other.getLevel();
        int level = (tlevel < olevel) ? tlevel : olevel;
        //level = (level == 0) ? 0 : level - 1;
        for (int i = level; i >= 1; i--) {
            int bitCount = getStartBitAtLevel(i) - 1;
            int otherBitCount = other.getStartBitAtLevel(i) - 1;
            if (bitCount > otherBitCount) {
                bitCount = otherBitCount;
            }
            //System.out.println("Has Common LCA " + bitCount + " " + level + " " + otherBitCount + " " + this.toBitString() + " " + other.toBitString() + " " + this.toString() + " " + other.toString());
            if (compareBitsExact(other, bitCount)) //  return new DLNBase(this.bits, bitCount);
            {
                return i;
            }
        }
        //return new DLNBase(this.bits, getStartBitAtLevel(0)); 
        return 0;
        //System.out.println("Has Common LCA " + result + " " + bitCount + " " + level + " " + otherBitCount + " " + this.toBitString() + " " + other.toBitString() + " " + this.toString() + " " + other.toString());
    }

    public int computeNCALevel(int tlevel, OldPathId other, int olevel) {
        int level = (tlevel < olevel) ? tlevel : olevel;
        //level = (level == 0) ? 0 : level - 1;
        for (int i = level; i >= 1; i--) {
            int bitCount = getStartBitAtLevel(i) - 1;
            int otherBitCount = other.getStartBitAtLevel(i) - 1;
            if (bitCount > otherBitCount) {
                bitCount = otherBitCount;
            }
            //System.out.println("Has Common LCA " + bitCount + " " + level + " " + otherBitCount + " " + this.toBitString() + " " + other.toBitString() + " " + this.toString() + " " + other.toString());
            if (compareBitsExact(other, bitCount)) //  return new DLNBase(this.bits, bitCount);
            {
                return i;
            }
        }
        //return new DLNBase(this.bits, getStartBitAtLevel(0)); 
        return 0;
        //System.out.println("Has Common LCA " + result + " " + bitCount + " " + level + " " + otherBitCount + " " + this.toBitString() + " " + other.toBitString() + " " + this.toString() + " " + other.toString());
    }

    /* 
     Check if this is an ancestor or self of the other 
     */
    public boolean isAncestor(OldPathId anc) {
        return this.isDescendantOrSelfOf(anc);
    }

    /* 
     Check if this is a descendant or self of the other 
     */
    public boolean isDescendant(OldPathId desc) {
        return desc.isDescendantOrSelfOf(this);
    }

    public boolean isAncestorOrDescendant(OldPathId anc) {
        return this.isAncestor(anc) || this.isDescendant(anc);
    }

    /*
     public boolean hasCommonAncestor(long id1, long id2, int level) {
     return (id1 ^ id2) < lValues[level].value;
     }
     */
    public OldPathId getAncestor(int level) {
        if (level == 1) {
            return ROOT_NODE;
        }
        if (level == this.getLevel()) {
            return this;
        }
        //System.out.println("getAncestorId " + last + " " + level + " " + getStartBitAtLevel(level));

        //System.out.println("Curt: PathId " + lastLevelOffset() + " " + getStartBitAtLevel(level));
        //return new PathId(bits, lastLevelOffset() - level);
        int numberOfBits = getStartBitAtLevel(level);
        //numberOfBits -= 1;
        //if (level > 1) 
        //System.out.println("Curt: getAncestor " + (numberOfBits+1) + " " + getStartBitAtLevel(level-1) + " " + bits.length);
        return new OldPathId(bits, numberOfBits - 1);

    }

    /**
     * Get the first descendant of the given node's ancestor at the given level.
     * This binary dewey is also the lower bound (inclusive) of the range of
     * descendant leaf nodes under the ancestor at the given level. This first
     * descendant can also be used as the only representative leaf node of the
     * ancestor.
     *
     * @param binaryDewey
     * @param level
     * @return
     */
    /*
     public NodeId getFirstDescendant(int level) {
     if (level <= 0) {
     throw new IllegalArgumentException();
     }
     int myLevel = getLevel();
     NodeId anc = this.getAncestor(level);
     for (int i = level; i < myLevel)
     anc.addLevelId(level, true);
        
     return new NodeId(value & (~(lValues[level].value - 1)), level);
     }
     */
    /*
     public NodeId getFirstChild(NodeId parentBinaryDewey, int parentLevel) {
     return new NodeId(parentBinaryDewey.value + this.lValues[parentLevel + 1].value, parentLevel + 1);
     }
     */
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
    public OldPathId getNextFirstDescendant(int level) {
        if (level <= 0) {
            throw new IllegalArgumentException();
        }
        OldPathId anc = this.getAncestor(level);
        DLN dln = anc.nextSibling();
        return new OldPathId(dln);
        //return new NodeId(anc.getFirstDescendant(level).value + this.lValues[level].value, level);
    }

    public boolean lessThan(OldPathId other) {
        return this.before(other, true);
        //return (value < other.value);
    }

    public boolean notEquals(OldPathId other) {
        return !this.equals(other);
        //return (value != other.value);
    }

    public boolean lessThanOrEqualTo(OldPathId other) {
        return this.before(other, true) || this.equals(other);
        //return (value <= other.value);
    }

    /*
     public NodeId newChild() {
     return calculate(level + 1);
     }
     */
    @Override
    public OldPathId newChild() {
        //DLN dln = super.newChild();
        //PathId n = new PathId(dln);
        //System.out.println("Curt: newchildPath " + n);
        //return n;
        return new OldPathId(super.newChild());
    }

    public OldPathId nextSibling() {
        return new OldPathId(super.nextSibling());
    }

    public void write(ObjectOutputStream o) throws IOException {
        o.writeObject(this);
    }

    /**
     * Calculates a new binary dewey value
     *
     * @param level The level of the owner node of the binary dewey value
     * @return
     */
    /*
     public static NodeId calculate(int level) {
     System.out.println("Curt: nodeId calculate " + level);
     long result = lastBinaryDewey == -1 ? 0
     : (lastBinaryDewey & ~(lValues[level].value - 1)) // parent ID
     + lValues[level].value; // next sibling ID (first is at 1)
     if (result <= lastBinaryDewey) {
     System.out.println("Bug!!" + lastBinaryDewey + ":" + level + ":" + lValues[level].value + ":" + result);
     }
     lastBinaryDewey = result;
     return new NodeId(result, level);
     }
     */
    /*
     public boolean isFirstDescendant(NodeId binaryDewey, int level) {
     if (level <= 0) {
     throw new IllegalArgumentException();
     }
     if (level == 1) {
     return (binaryDewey.value == 0);
     } else {
     return (binaryDewey.value % this.lValues[level].value == 0);
     }
     }
     */
    public int getLevel() {
        return super.getLevel();
    }

    /*
     @Override

     public int hashCode() {
     return new Long(value).hashCode();
     }
     */
    public boolean equals(OldPathId other) {
        return super.equals((DLN) other);
    }

    public int compareTo(Object other) {
        return compareTo((OldPathId) other);
    }

    public String toString() {
        return super.toString();
    }

    public int compareTo(OldPathId otherId) {
        if (otherId == null) {
            return 1;
        }
        return super.compareTo((DLN) otherId);
    }

    /**
     * Returns a new NodeId representing the first child node of this node. The
     * returned id can be used for creating new nodes. The actual id of the
     * first node might be different, depending on the implementation.
     *
     * @return new child node id
     */
    //NodeId newChild();
    /**
     * Returns a new NodeId representing the nth child node of this node. The
     * returned id can be used to create new child nodes. The actual id of the
     * child might be different, depending on the implementation.
     *
     * @param child the position of the child
     * @return new node id
     */
    //NodeId getChild(int child);
    /**
     * Returns a new NodeId representing the next following sibling of this
     * node. The returned id can be used to create new sibling nodes. The actual
     * id of the next sibling might be different, depending on the
     * implementation.
     *
     * @return new sibling node id.
     */
    //NodeId nextSibling();
    /**
     * Returns a new NodeId representing the prececing sibling of this node. The
     * returned id can be used to create new sibling nodes. The actual id of the
     * next sibling might be different, depending on the implementation.
     *
     * @return new sibling node id.
     */
    //NodeId precedingSibling();
    //NodeId insertNode(NodeId right);
    //NodeId insertBefore();
    //NodeId append(NodeId other);
    /**
     * Returns a new NodeId representing the parent of the current node. If the
     * parent is the document, the constant {@link #DOCUMENT_NODE} will be
     * returned. For the document itself, the parent id will be null.
     *
     * @return the id of the parent node or null if the current node is the
     * document node.
     */
    //NodeId getParentId();
    /**
     * Returns true if the node represented by this node id comes after the
     * argument node in document order. If isFollowing is set to true, the
     * method behaves as if called to evaluate a following::* XPath select, i.e.
     * it returns false for descendants of the current node.
     *
     * @param other the node id to compare with
     * @param isFollowing if true, return false for descendants of the current
     * node
     * @return true true if the current node comes after the other node in
     * document order
     */
    //boolean after(NodeId other, boolean isFollowing);
    /**
     * Returns true if the node represented by this node id comes before the
     * argument node in document order. If isPreceding is set to true, the
     * method behaves as if called to evaluate a preceding::* XPath select, i.e.
     * it returns false for ancestors of the current node.
     *
     * @param other the node id to compare with
     * @param isPreceding if true, return false for ancestors of the current
     * node
     * @return true if the current node comes before the other node in document
     * order.
     */
    //boolean before(NodeId other, boolean isPreceding);
    /**
     * Is the current node id a descendant of the specified node?
     *
     * @param ancestor node id of the potential ancestor
     * @return true if the node id is a descendant of the given node, false
     * otherwise
     */
    //boolean isDescendantOf(NodeId ancestor);
    //boolean isDescendantOrSelfOf(NodeId ancestor);
    /**
     * Is the current node a child node of the specified parent?
     *
     * @param parent the parent node
     * @return true if the current node is a child of the specified parent
     */
    //boolean isChildOf(NodeId parent);
    /**
     * Computes the relationship of this node to the given potential ancestor
     * node. The method returns an int constant indicating the relation.
     * Possible relations are: {@link #IS_CHILD}, {@link #IS_DESCENDANT} or
     * {@link #IS_SELF}. If the nodes are not in a ancestor-descendant relation,
     * the method returns -1.
     *
     * @param ancestor the (potential) ancestor node to check against
     * @return an int value indicating the relation
     */
    //int computeRelation(NodeId ancestor);
    //boolean isSiblingOf(NodeId sibling);
    /**
     * Returns the level within the document tree at which this node occurs.
     *
     * @return the tree level
     */
    //int getTreeLevel();
    //int compareTo(NodeId other);
    //boolean equals(NodeId other);
    /**
     * Returns the size (in bytes) of this node id. Depends on the concrete
     * implementation.
     *
     * @return size
     */
    //int size();
    //int units();
    /**
     * Serializes the node id to an array of bytes. The first byte is written at
     * offset.
     *
     * @param data the byte array to be filled
     * @param offset offset into the array
     */
    //void serialize(byte[] data, int offset);
    /**
     * Write the node id to a
     * {@link org.exist.storage.io.VariableByteOutputStream}.
     *
     * @param os the output stream
     * @throws java.io.IOException if there's a problem with the underlying
     * output stream
     */
    //void write(VariableByteOutputStream os) throws IOException;
    /**
     * Write the node id to a
     * {@link org.exist.storage.io.VariableByteOutputStream}. To save storage
     * space, only store those byte which are different from the previous node
     * id.
     *
     * @param previous the node id previously written or null
     * @param os the output stream
     * @return this node id
     * @throws IOException if there's a problem with the underlying output
     * stream
     */
    //NodeId write(NodeId previous, VariableByteOutputStream os) throws IOException;
    //int getTypeID();
    //void setTypeID(int id);
}
