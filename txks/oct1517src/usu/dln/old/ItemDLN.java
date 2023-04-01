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
 *  $Id: DLN.java 9474 2009-07-23 20:12:38Z dizzzz $
 */
package usu.dln.old;

import java.io.Serializable;
import usu.NodeId;
import usu.PathId;
import usu.dln.DLN;
import static usu.dln.DLN.toBitString;
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

// Author: Amani Shatnawi
public class ItemDLN extends DLN implements PathId, NodeId, Serializable, Comparable {

    protected transient static final Time ALL_OF_TIME = new Time(0, 100);
    protected Time timestamp = ALL_OF_TIME;
    private static int count = 1;

    protected transient static final ItemDLN ROOT_NODE = new ItemDLN(1);
    // Establish a node that is last in the ordering, greater than the root node
    protected transient static final ItemDLN MAX_NODE = new ItemDLN(2);
    // Establish a node that is first in the ordering, greater than the root node
    protected transient static final ItemDLN MIN_NODE = new ItemDLN(0);

    public ItemDLN() {
        super();
        this.timestamp = ALL_OF_TIME;
    }

    public ItemDLN(ItemDLN dln) {
        //System.out.println("Curt: DLN " + dln + " bitIndex " + dln.bitIndex);
        super((DLN) dln);
        this.timestamp = dln.timestamp;
        //System.out.println("Curt: DLN2 " + this + " bitIndex " + bitIndex);
    }

    public ItemDLN(DLN dln, Time t) {
        //System.out.println("Curt: DLN " + dln + " bitIndex " + dln.bitIndex);
        super((DLN) dln);
        this.timestamp = t;
        //System.out.println("Curt: DLN2 " + this + " bitIndex " + bitIndex);
    }

    public ItemDLN(short units, byte[] data, int startOffset) {
        super(units, data, startOffset);
        this.timestamp = ALL_OF_TIME;
    }

    public ItemDLN(byte[] data, short nbits) {
        super(data, nbits);
        this.timestamp = ALL_OF_TIME;
    }

    public ItemDLN(byte[] data, short nbits, Time time) {
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
    public ItemDLN(String s) {
        super(s);
        this.timestamp = ALL_OF_TIME;
    }

    /**
     * Constructs a new DLN, using the passed id as its single level value.
     *
     * @param id
     */
    public ItemDLN(int id) {
        super(id);
        this.timestamp = ALL_OF_TIME;
    }

    /**
     * Compute the common lca at this level (or below)
     *
     * @return common lca, or null if none
     */
    public ItemDLN commonLCA(ItemDLN other, int level) {
        return new ItemDLN(super.commonLCA(other, level), timestamp.intersection(other.timestamp));
        //System.out.println("Has Common LCA " + result + " " + bitCount + " " + level + " " + otherBitCount + " " + this.toBitString() + " " + other.toBitString() + " " + this.toString() + " " + other.toString());

    }

    @Override
    public int computeNCALevel(NodeId other) {
        if (!this.timestamp.overlaps(((ItemDLN)other).timestamp)) {
            return 0;
        }
        return super.computeNCALevel((DLN) other);
    }

    public int computeNCALevel(ItemDLN other) {
        //System.out.println("here");
        if (!this.timestamp.overlaps(other.timestamp)) {
            return 0;
        }
        return super.computeNCALevel((DLN) other);
    }

    public int computeNCALevel(int tlevel, ItemDLN other, int olevel) {
        System.out.println("here");
        if (!this.timestamp.overlaps(other.timestamp)) {
            return 0;
        }
        return super.computeNCALevel(tlevel, (DLN) other, olevel);
    }

    /**
     * Is this one equal to the other to the given level in the tree? Used to
     * check if they have a common lca at this level (or below)
     *
     * @return whether they are equal
     */
    public boolean hasCommonLCA(ItemDLN other, int level) {
        return super.hasCommonLCA((DLN) other, level) && timestamp.overlaps(other.timestamp);
    }

    public boolean equals(ItemDLN other) {
        return super.equals((DLN) other) && timestamp.equals(other.timestamp);
    }

    @Override
    public String debug() {
        return super.debug() + timestamp.debug();
    }

    @Override
    public String toString() {
        if (timestamp == null) {
            return super.toString() + " null timestamp ";
        }
        return super.toString() + timestamp.toString();
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
    public ItemDLN newChild() {
        return new ItemDLN(super.newChild(), this.timestamp);
        //return new ItemDLN(super.newChild(), new Time(count, count));
    }

    /**
     * Returns a new DLN representing the next following sibling of this node.
     *
     * @return new sibling node id.
     */
    @Override
    public ItemDLN nextSibling() {
        count++;
        return new ItemDLN(super.nextSibling(), this.timestamp);
        //return new ItemDLN(super.nextSibling(), new Time(count, count));
    }

    @Override
    public ItemDLN precedingSibling() {
        return new ItemDLN(super.precedingSibling(), this.timestamp);
    }

    /*
     public DLN getChild(int child) {
     DLN nodeId = new DLN(this);
     nodeId.addLevelId(child, false);
     return nodeId;
     }
     */
    @Override
    public ItemDLN insertNode(NodeId right) {
        return insertNode((ItemDLN) right);
    }

    @Override
    public ItemDLN insertNode(PathId right) {
        return insertNode((ItemDLN) right);
    }

    public ItemDLN insertNode(ItemDLN right) {
        return new ItemDLN(super.insertNode((DLN) right), this.timestamp.intersection(right.timestamp));
    }

    @Override
    public ItemDLN insertBefore() {
        return new ItemDLN(super.insertBefore(), this.timestamp);
    }

    // Not used
    public ItemDLN append(ItemDLN otherId) {
        return new ItemDLN(super.append((DLN) otherId), this.timestamp.intersection(otherId.timestamp));
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
    public ItemDLN getParentId() {
        return new ItemDLN(super.getParentId(), this.timestamp);
    }

    @Override
    public boolean isDescendantOf(NodeId ancestor) {
        return isDescendantOf((ItemDLN) ancestor);
    }

    @Override
    public boolean isDescendantOf(PathId ancestor) {
        return isDescendantOf((ItemDLN) ancestor);
    }

    public boolean isAncestorOrDescendant(ItemDLN other) {
        return timestamp.overlaps(other.timestamp) && super.isAncestorOrDescendant((DLN) other);
    }

    public boolean isDescendantOf(ItemDLN other) {
        return timestamp.overlaps(other.timestamp) && super.isDescendantOf((DLN) other);
    }

    @Override
    public boolean isDescendantOrSelfOf(NodeId ancestor) {
        return isDescendantOrSelfOf((ItemDLN) ancestor);
    }

    @Override
    public boolean isDescendantOrSelfOf(PathId ancestor) {
        return isDescendantOrSelfOf((ItemDLN) ancestor);
    }

    public boolean isDescendantOrSelfOf(ItemDLN other) {
        return timestamp.overlaps(other.timestamp) && super.isDescendantOrSelfOf((DLN) other);
    }

    @Override
    public boolean isChildOf(NodeId other) {
        return isChildOf((ItemDLN) other);
    }

    @Override
    public boolean isChildOf(PathId other) {
        return isChildOf((ItemDLN) other);
    }

    public boolean isChildOf(ItemDLN other) {
        return timestamp.overlaps(other.timestamp) && super.isChildOf((DLN) other);
    }

    @Override
    public int computeRelation(NodeId other) {
        return computeRelation((ItemDLN) other);
    }

    @Override
    public boolean isSiblingOf(NodeId sibling) {
        return isSiblingOf((ItemDLN) sibling);
    }

    @Override
    public boolean isSiblingOf(PathId sibling) {
        return isSiblingOf((ItemDLN) sibling);
    }

    public boolean isSiblingOf(ItemDLN other) {
        return timestamp.overlaps(other.timestamp) && super.isSiblingOf((DLN) other);
    }

    @Override
    public boolean after(NodeId other, boolean isFollowing) {

        return after((ItemDLN) other, isFollowing);
    }

    @Override
    public boolean after(PathId other, boolean isFollowing) {
        return after((ItemDLN) other, isFollowing);
    }

    public boolean after(ItemDLN other, boolean isFollowing) {
        return timestamp.overlaps(other.timestamp) && super.after((DLN) other, isFollowing);
    }

    @Override
    public boolean before(NodeId other, boolean isPreceding) {
        return before((ItemDLN) other, isPreceding);
    }

    @Override
    public boolean before(PathId other, boolean isPreceding) {
        return before((ItemDLN) other, isPreceding);
    }

    public boolean before(ItemDLN other, boolean isPreceding) {
        return timestamp.overlaps(other.timestamp) && super.before((DLN) other, isPreceding);
    }

    public static ItemDLN getRootId() {
        return ItemDLN.ROOT_NODE;
    }

    public static ItemDLN getMaxId() {
        return ItemDLN.MAX_NODE;
    }

    public static ItemDLN getMinId() {
        return ItemDLN.MIN_NODE;
    }


    /*
     public boolean hasCommonAncestor(long id1, long id2, int level) {
     return (id1 ^ id2) < lValues[level].value;
     }
     */
    public ItemDLN getAncestor(int level) {
        return new ItemDLN(super.getAncestor(level), this.timestamp);
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
    public ItemDLN getNextFirstDescendant(int level) {
        return new ItemDLN(super.getNextFirstDescendant(level), this.timestamp);
    }

    @Override
    public int compareTo(Object other) {
        return compareTo((ItemDLN) other);
    }

    @Override
    public int compareTo(NodeId other) {
        //System.out.println("other NodeId is " + other.getClass());
        return compareTo((ItemDLN) other);
    }

    @Override
    public int compareTo(PathId other) {
        //System.out.println("other PathId is " + other.getClass());
        return compareTo((ItemDLN) other);
    }

    public int compareTo(ItemDLN other) {
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
        ItemDLN id0 = new ItemDLN("1.1.7");
        ItemDLN id1 = new ItemDLN("1.1.6");
        ItemDLN id2 = new ItemDLN("1.1.7.1");
        ItemDLN id3 = new ItemDLN("1.1.7/1");
        ItemDLN id4 = (ItemDLN) id0.newChild();
        ItemDLN id5 = (ItemDLN) id0.nextSibling();

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
