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
package usu.history;

import usu.dln.*;
import java.io.Serializable;
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
public class SortedHDLN extends HDLN implements PathId, NodeId, Serializable, Comparable {

    
    protected transient static final SortedHDLN SORTED_ROOT_NODE = new SortedHDLN(1);
    // Establish a node that is last in the ordering, greater than the root node
    protected transient static final SortedHDLN SORTED_MAX_NODE = new SortedHDLN(2);
    // Establish a node that is first in the ordering, greater than the root node
    protected transient static final SortedHDLN SORTED_MIN_NODE = new SortedHDLN(0);
    
    public SortedHDLN() {
        super();
        this.timestamp = ALL_OF_TIME;
    }

    public SortedHDLN(SortedHDLN dln) {
        //System.out.println("Curt: DLN " + dln + " bitIndex " + dln.bitIndex);
        super((HDLN) dln);
        this.timestamp = dln.timestamp;
        //System.out.println("Curt: DLN2 " + this + " bitIndex " + bitIndex);
    }

    public SortedHDLN(DLN dln, Time t) {
        //System.out.println("Curt: DLN " + dln + " bitIndex " + dln.bitIndex);
        super((HDLN) dln);
        this.timestamp = t;
        //System.out.println("Curt: DLN2 " + this + " bitIndex " + bitIndex);
    }

    public SortedHDLN(short units, byte[] data, int startOffset) {
        super(units, data, startOffset);
        this.timestamp = ALL_OF_TIME;
    }

    public SortedHDLN(byte[] data, short nbits) {
        super(data, nbits);
        this.timestamp = ALL_OF_TIME;
    }

    public SortedHDLN(byte[] data, short nbits, Time time) {
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
    public SortedHDLN(String s) {
        super(s);
        this.timestamp = ALL_OF_TIME;
    }

    /**
     * Constructs a new DLN, using the passed id as its single level value.
     *
     * @param id
     */
    public SortedHDLN(int id) {
        super(id);
        this.timestamp = ALL_OF_TIME;
    }

    /**
     * Compute the common lca at this level (or below)
     *
     * @return common lca, or null if none
     */
    public SortedHDLN commonLCA(SortedHDLN other, int level) {
        return new SortedHDLN(super.commonLCA(other, level), timestamp.intersection(other.timestamp));
        //System.out.println("Has Common LCA " + result + " " + bitCount + " " + level + " " + otherBitCount + " " + this.toBitString() + " " + other.toBitString() + " " + this.toString() + " " + other.toString());

    }

    /**
     * Is this one equal to the other to the given level in the tree? Used to
     * check if they have a common lca at this level (or below)
     *
     * @return whether they are equal
     */
    public boolean hasCommonLCA(SortedHDLN other, int level) {
        return super.hasCommonLCA((DLN) other, level) && timestamp.overlaps(other.timestamp);
    }

    public boolean equals(SortedHDLN other) {
        return super.equals((DLN) other) && timestamp.equals(other.timestamp);
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
    public SortedHDLN newChild() {
        return new SortedHDLN(super.newChild(), this.timestamp);
    }

    /**
     * Returns a new DLN representing the next following sibling of this node.
     *
     * @return new sibling node id.
     */
    @Override
    public SortedHDLN nextSibling() {
        return new SortedHDLN(super.nextSibling(), this.timestamp);
    }

    @Override
    public SortedHDLN precedingSibling() {
        return new SortedHDLN(super.precedingSibling(), this.timestamp);
    }

    /*
     public DLN getChild(int child) {
     DLN nodeId = new DLN(this);
     nodeId.addLevelId(child, false);
     return nodeId;
     }
     */
    @Override
    public SortedHDLN insertNode(NodeId right) {
        return insertNode((SortedHDLN) right);
    }

    @Override
    public SortedHDLN insertNode(PathId right) {
        return insertNode((SortedHDLN) right);
    }

    public SortedHDLN insertNode(SortedHDLN right) {
        return new SortedHDLN(super.insertNode((DLN) right), this.timestamp.intersection(right.timestamp));
    }

    @Override
    public SortedHDLN insertBefore() {
        return new SortedHDLN(super.insertBefore(), this.timestamp);
    }

    // Not used
    public SortedHDLN append(SortedHDLN otherId) {
        return new SortedHDLN(super.append((DLN) otherId), this.timestamp.intersection(otherId.timestamp));
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
    public SortedHDLN getParentId() {
        return new SortedHDLN(super.getParentId(), this.timestamp);
    }

    public static SortedHDLN getRootId() {
        return SORTED_ROOT_NODE;
    }

    public static SortedHDLN getMaxId() {
        return SORTED_MAX_NODE;
    }

    public static SortedHDLN getMinId() {
        return SORTED_MIN_NODE;
    }


    /*
     public boolean hasCommonAncestor(long id1, long id2, int level) {
     return (id1 ^ id2) < lValues[level].value;
     }
     */
    public SortedHDLN getAncestor(int level) {
        return new SortedHDLN(super.getAncestor(level), this.timestamp);
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
    public SortedHDLN getNextFirstDescendant(int level) {
        return new SortedHDLN(super.getNextFirstDescendant(level), this.timestamp);
    }
 
    public static void main(String[] args) {
        SortedHDLN id0 = new SortedHDLN("1.1.7");
        SortedHDLN id1 = new SortedHDLN("1.1.6");
        SortedHDLN id2 = new SortedHDLN("1.1.7.1");
        SortedHDLN id3 = new SortedHDLN("1.1.7/1");
        SortedHDLN id4 = (SortedHDLN) id0.newChild();
        SortedHDLN id5 = (SortedHDLN) id0.nextSibling();

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
