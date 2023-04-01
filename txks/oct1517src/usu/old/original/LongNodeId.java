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
package usu.original;

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
public class LongNodeId implements Comparable<LongNodeId>, Serializable {

    public transient static final int LENGTH_NODE_ID_UNITS = 2; //sizeof short


    /**
     * Static field representing the document node.
     */
    //public transient final static int IS_CHILD = 1;
    //public transient final static int IS_DESCENDANT = 2;
    //public transient final static int IS_SELF = 3;

    //public transient static NodeId DOCUMENT_NODE = new NodeId(0,1);
    //public transient static NodeId END_OF_DOCUMENT = new NodeId(0,1);
    public transient static LongNodeId ROOT_NODE = new LongNodeId(1,1);

    public long value = 0;
    public int level = 0;

    public LongNodeId() {
        value = 1;
        level = 1;
    }

    /*
    public NodeId(long i) {
        value = i;
        level = 1;
    }
    */
    public LongNodeId(long i, int j) {
        value = i;
        level = j;
    }

    /*
    public static NodeId getEndOfDocumentId() {
        return DOCUMENT_NODE;
    }
    */
    public static LongNodeId getRootId() {
        return ROOT_NODE;
    }

    public transient static final long[] POWER_OF_2 = new long[]{
        1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768,
        65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608,
        16777216, 33554432, 67108864, 134217728,
        268435456, 536870912, 1073741824};

    private transient static int maxDepth;
    private static LongNodeId[] lValues;
    private static int[] levelMap = new int[65]; // map from number of trailing zeros to level
    private static long lastBinaryDewey = -1;

    public static void initializeLValues(int m, long levelOrder) {
                System.out.println("Curt: Initializing NodeId m " + m + " o " + levelOrder);
        maxDepth = m;
        lValues = new LongNodeId[m + 1];
        lValues[maxDepth] = new LongNodeId(1, maxDepth);
        for (int i = maxDepth - 1; i >= 1; i--) {
            lValues[i] = new LongNodeId(lValues[i + 1].value * levelOrder, i);
        }
        initializeLevelMap();
    }

    public static void initializeLValues(int m, long[] levelOrder) {
                System.out.println("Curt: Initializing NodeIdArray m " + m + " o " + levelOrder.length);
        maxDepth = m;
        lValues = new LongNodeId[maxDepth + 1];
        lValues[maxDepth] = new LongNodeId(1,maxDepth);
        for (int i = maxDepth - 1; i >= 1; i--) {
            lValues[i] = new LongNodeId(lValues[i + 1].value * levelOrder[i], i);
        }
        initializeLevelMap();
    }

    private static void initializeLevelMap() {
        long temp = 1;
        int level = maxDepth;
        for (int i = 0; i < 64; i++) {
            if (level > 1 && temp == lValues[level - 1].value) {
                level--;
            }
            levelMap[i] = level;
            temp <<= 1;
        }
        levelMap[64] = 1;
    }

           public static void redoLValues(int m, long levelOrder) {
            maxDepth=m;
        lValues = new LongNodeId[m+1];
        lValues[maxDepth] = new LongNodeId(1, maxDepth);
        for (int i = maxDepth-1; i >= 2; i--) {
            lValues[i] = new LongNodeId(lValues[i+1].value*levelOrder, i);
            System.out.println("lValues["+i+"] = " + lValues[i]);
        }
    }
     
            /**
     * Constructs a calculator with different level orders at different level
     * @param maxDepth      Max Depth
     * @param levelOrder    Value at position i = Level order at level i
     */
    public static void redoLValues(int m, long[] levelOrder) {
        maxDepth = m;
        lValues = new LongNodeId[maxDepth+1];
        lValues[maxDepth] = new LongNodeId(1, maxDepth);
        for (int i = maxDepth-1; i >= 1; i--) {
            lValues[i] = new LongNodeId(lValues[i+1].value*levelOrder[i], i);
        }
        for (int i = 1; i <= maxDepth; i++) {
            System.out.println("levelOrder[" + i + "] = " + levelOrder[i]);
            System.out.println("lValues[" + i + "] = " + lValues[i]);
        }
    }
    public int computeNCALevel(LongNodeId binaryDewey2) {
        long xor = value ^ binaryDewey2.value;
        for (int i = 2; i <= maxDepth; i++) {
            if (xor >= this.lValues[i].value) {
                return i - 1;
            }
        }
        return maxDepth;
    }

    public int computeNCALevel(int level1, LongNodeId binaryDewey2, int level2) {
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

//    public static boolean isAncDesc(long anc, long desc) {
//        boolean res = (~((anc&(~anc+1))-1) & desc) == anc; // (anc&(~anc+1)) returns the rightmost 1-bit of anc
//        return res;
//    }
    public boolean isAncestor(LongNodeId anc) {
        int ancLevel = anc.getLevel();
        return this.getAncestor(ancLevel).value == anc.value;
    }

    public boolean isDescendant(LongNodeId desc) {
        LongNodeId otheranc = desc.getAncestor(level);
        return value == otheranc.value;
    }

    public boolean isAncestorOrDescendant(LongNodeId anc) {
        return this.isAncestor(anc) || this.isDescendant(anc);
    }

    public boolean hasCommonAncestor(long id1, long id2, int level) {
        return (id1 ^ id2) < lValues[level].value;
    }

    public LongNodeId getAncestor(int level) {
        return this.getFirstDescendant(level);
        //return binaryDewey;
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
    public LongNodeId getFirstDescendant(int level) {
        if (level <= 0) {
            throw new IllegalArgumentException();
        }
        return new LongNodeId(value & (~(lValues[level].value - 1)), level);
    }

    public LongNodeId getFirstChild(LongNodeId parentBinaryDewey, int parentLevel) {
        return new LongNodeId(parentBinaryDewey.value + this.lValues[parentLevel + 1].value, parentLevel+1);
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
    public LongNodeId getNextFirstDescendant(int level) {
        if (level <= 0) {
            throw new IllegalArgumentException();
        }
        LongNodeId anc = this.getAncestor(level);
        return new LongNodeId(anc.getFirstDescendant(level).value + this.lValues[level].value, level);
    }

    public boolean lessThan(LongNodeId other) {
        return (value < other.value);
    }

    public boolean lessThanOrEqualTo(LongNodeId other) {
        return (value <= other.value);
    }

    public LongNodeId newChild() {
        return calculate(level + 1);
    }

    public LongNodeId nextSibling() {
        return calculate(level);
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
    public static LongNodeId calculate(int level) {
        System.out.println("Curt: nodeId calculate " + level);
        long result = lastBinaryDewey == -1 ? 0
                : (lastBinaryDewey & ~(lValues[level].value - 1)) // parent ID
                + lValues[level].value; // next sibling ID (first is at 1)
        if (result <= lastBinaryDewey) {
            System.out.println("Bug!!" + lastBinaryDewey + ":" + level + ":" + lValues[level].value + ":" + result);
        }
        lastBinaryDewey = result;
        return new LongNodeId(result, level);
    }

    public boolean isFirstDescendant(LongNodeId binaryDewey, int level) {
        if (level <= 0) {
            throw new IllegalArgumentException();
        }
        if (level == 1) {
            return (binaryDewey.value == 0);
        } else {
            return (binaryDewey.value % this.lValues[level].value == 0);
        }
    }

    public int getLevel() {
        return level;
    }

    @Override

    public int hashCode() {
        return new Long(value).hashCode();
    }

    public boolean equals(LongNodeId other) {
        return value == other.value;
    }

    /*
    public int compareTo(Object other) {
        return compareTo((NodeId) other);
    }
    */
    
    public String toString() {
      return "Node value is " + value + " level is " + level;    
    }
    
    public int compareTo(LongNodeId otherId) {
        if (otherId == null) {
            return 1;
        }

        return new Long(value).compareTo(new Long(otherId.value));
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
