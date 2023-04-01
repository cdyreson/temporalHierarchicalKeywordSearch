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
public class OldNodeId extends DLN implements Comparable, Serializable {

    /**
     * Static field representing the document node.
     */
    //public transient final static int IS_CHILD = 1;
    //public transient final static int IS_DESCENDANT = 2;
    //public transient final static int IS_SELF = 3;
    //public transient static NodeId DOCUMENT_NODE = new NodeId(0,1);
    //public transient static NodeId END_OF_DOCUMENT = new NodeId(0,1);
    private transient final static OldNodeId ROOT_NODE = new OldNodeId(1);
    // Establish a node that is last in the ordering, greater than the root node
    private transient final static OldNodeId MAX_NODE = new OldNodeId(2);
    // Establish a node that is first in the ordering, greater than the root node
    private transient final static OldNodeId MIN_NODE = new OldNodeId(0);


    /*
     public long value = 0;
     public int level = 0;
     */
    /*
     public NodeId() {
     value = 1;
     level = 1;
     }


     public NodeId(long i) {
     value = i;
     level = 1;
     }
     */
    /*
     public NodeId(long i, int j) {
     value = i;
     level = j;
     }
     */
    public OldNodeId(byte[] bits, int j) {
        super(bits, (short) j);
    }


    public OldNodeId(DLN dln) {
        super(dln);
    }

    public OldNodeId(int i) {
        super(i);
    }
    /*
     public static NodeId getEndOfDocumentId() {
     return DOCUMENT_NODE;
     }
     */

    public static OldNodeId getRootId() {
        return ROOT_NODE;
    }

    public static OldNodeId getMaxId() {
        return MAX_NODE;
    }

    public static OldNodeId getMinId() {
        return MIN_NODE;
    }

    /**
     * Compute the common lca at this level (or below)
     *
     * @return common lca, or null if none
     */
    public int computeNCALevel(OldNodeId other) {
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
            //System.out.println("Has Common LCA " + bitCount + " " + i + " " + otherBitCount + " " + this.toBitString() + " " + other.toBitString() + " " + this.toString() + " " + other.toString());
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
     public int computeNCALevel(NodeId binaryDewey2) {
     long xor = value ^ binaryDewey2.value;
     for (int i = 2; i <= maxDepth; i++) {
     if (xor >= this.lValues[i].value) {
     return i - 1;
     }
     }
     return maxDepth;
     }
     */

    /*
     public int computeNCALevel(int level1, NodeId binaryDewey2, int level2) {
     //if (node1.equals(node2)) return node1.getLevel();
     int minLevel = Math.min(level1, level2);
     long xor = value ^ binaryDewey2.value;
     for (int i = 2; i <= minLevel; i++) {
     if (xor >= this.lValues[i].value) {
     return i - 1;
     }
     }
     return minLevel;
     } 
     */
//    public static boolean isAncDesc(long anc, long desc) {
//        boolean res = (~((anc&(~anc+1))-1) & desc) == anc; // (anc&(~anc+1)) returns the rightmost 1-bit of anc
//        return res;
//    }
    /*
     public boolean isAncestor(NodeId anc) {
     int ancLevel = anc.getLevel();
     return this.getAncestor(ancLevel).value == anc.value;
     }

     public boolean isDescendant(NodeId desc) {
     NodeId otheranc = desc.getAncestor(level);
     return value == otheranc.value;
     }
     */
    /* 
     Check if this is an ancestor or self of the other 
     */
    public boolean isAncestor(OldNodeId anc) {
        return this.isDescendantOrSelfOf(anc);
    }

    /* 
     Check if this is a descendant or self of the other 
     */
    public boolean isDescendant(OldNodeId desc) {
        return desc.isDescendantOrSelfOf(this);
    }

    public boolean isAncestorOrDescendant(OldNodeId anc) {
        return this.isAncestor(anc) || this.isDescendant(anc);
    }

    /*
     public boolean hasCommonAncestor(long id1, long id2, int level) {
     return (id1 ^ id2) < lValues[level].value;
     }
     */
    public OldNodeId getAncestor(int level) {
        if (level == 1) {
            return ROOT_NODE;
        }
        if (level == this.getLevel()) {
            return this;
        }
        //System.out.println("getAncestorId " + last + " " + level + " " + getStartBitAtLevel(level));

        //return new DLN(bits, last - level);
        return new OldNodeId(bits, getStartBitAtLevel(level) - 1);
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
    public OldNodeId getNextFirstDescendant(int level) {
        if (level <= 0) {
            throw new IllegalArgumentException();
        }
        OldNodeId anc = this.getAncestor(level);
        DLN dln = anc.nextSibling();
        return new OldNodeId(dln);
        //return new NodeId(anc.getFirstDescendant(level).value + this.lValues[level].value, level);
    }

    public boolean lessThan(OldNodeId other) {
        return this.before(other, true);
        //return (value < other.value);
    }

    public boolean notEqual(OldNodeId other) {
        return !this.equals(other);
        //return (value != other.value);
    }

    public boolean lessThanOrEqualTo(OldNodeId other) {
        return this.before(other, true) || this.equals(other);
        //return (value <= other.value);
    }

    /*
     public NodeId newChild() {
     return calculate(level + 1);
     }
     */
    public OldNodeId newChild() {
        OldNodeId n = new OldNodeId(super.newChild());
        System.out.println("Curt NodeId newChild    " + n + " " + n.debug());
        return n;
        //return new NodeId(super.newChild());
    }

    public OldNodeId nextSibling() {
        OldNodeId n = new OldNodeId(super.nextSibling());
        System.out.println("Curt NodeId nextSibling " + n + " " + n.debug());
        return n;
        //return new NodeId(super.nextSibling());
    }

    public void write(ObjectOutputStream o) throws IOException {
        o.writeObject(this);
    }

    /*
    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.writeObject(bits);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        //this.bits = new byte[NUMBEROFBYTES];
        this.bits = (byte[]) in.readObject();
        restoreBitIndex();
        //this.bitIndex = in.readInt();
        //System.out.println("Curt: reading " + this);
    }
    */

    //private void readObjectNoData()
    //    throws ObjectStreamException {
    //    
    //}

    /*
     private void writeObject(java.io.ObjectOutputStream out)
     throws IOException {
     int i = this.bits.length;
     if (serialized == null) {
     serialized = new byte[i + 2];
     System.arraycopy(this.bits, 0, this.serialized, 0, i);
     } else if (serialized.length != i + 2) {
     serialized = new byte[i + 2];
     System.arraycopy(this.bits, 0, this.serialized, 0, i);
     } else {
     // serialization already in place
     }
     // Put bitIndex in place
     serialized[i] = (byte) (bitIndex & 0xff);
     serialized[i + 1] = (byte) ((bitIndex >>> 8) & 0xff);
     out.write(serialized);
     }

     private void readObject(java.io.ObjectInputStream in)
     throws IOException, ClassNotFoundException {
     //this.bits = new byte[NUMBEROFBYTES];
     this.serialized = (byte[]) in.readObject();
     int i = this.serialized.length;
     this.bitIndex = (short) (((serialized[i] << 8)) | ((serialized[i + 1] & 0xff)));
     this.bits = new byte[i - 2];
     System.arraycopy(serialized, 0, this.bits, 0, i - 2);
     //this.bitIndex = in.readInt();
     //System.out.println("Curt: reading " + this);
     }
     */
    //private void readObjectNoData()
    //    throws ObjectStreamException {
    //    
    //}
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
    public boolean equals(OldNodeId other) {
        return super.equals((DLN) other);
    }

    public int compareTo(Object other) {
        return compareTo((OldNodeId) other);
    }

    public String toString() {
        return super.toString();
    }

    public byte[] serialize() {
        System.out.println("seriliazing " + this.toBitString());
        // Right justify this sucker
        byte[] r = new byte[16];
        System.arraycopy(bits, 0, r, 0, bits.length);
        r[15]=(byte)bits.length;
        return r;
    }

    //public static NodeId deserialize(byte[] b) {
    //    return new NodeId(bits);
        //return n;
    //}

    public int compareTo(OldNodeId otherId) {
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
