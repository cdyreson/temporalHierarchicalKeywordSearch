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
package usu;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Represents the internal id of a node within eXist. Basically, all stored
 * nodes in eXist need to have an id that implements this interface. The id will
 * be assigned according to used numbering scheme. From a given id, we can
 * determine the relationship of the node it represents to any other node in the
 * same document.
 */
public interface PathId extends Comparable {

    /**
     * Returns a new NodeId representing the first child node of this node. The
     * returned id can be used for creating new nodes. The actual id of the
     * first node might be different, depending on the implementation.
     *
     * @return new child node id
     */
    PathId newChild();

    /**
     * Returns a new NodeId representing the nth child node of this node. The
     * returned id can be used to create new child nodes. The actual id of the
     * child might be different, depending on the implementation.
     *
     * @param child the position of the child
     * @return new node id
     */
    //PathId getChild(int child);
    /**
     * Returns a new NodeId representing the next following sibling of this
     * node. The returned id can be used to create new sibling nodes. The actual
     * id of the next sibling might be different, depending on the
     * implementation.
     *
     * @return new sibling node id.
     */
    PathId nextSibling();

    /**
     * Returns a new NodeId representing the prececing sibling of this node. The
     * returned id can be used to create new sibling nodes. The actual id of the
     * next sibling might be different, depending on the implementation.
     *
     * @return new sibling node id.
     */
    PathId precedingSibling();

    PathId insertNode(PathId right);

    PathId insertBefore();

    //boolean isRoot();
    //PathId append(PathId other);
    /**
     * Returns a new NodeId representing the parent of the current node. If the
     * parent is the document, the constant {@link #DOCUMENT_NODE} will be
     * returned. For the document itself, the parent id will be null.
     *
     * @return the id of the parent node or null if the current node is the
     * document node.
     */
    PathId getParentId();

    public int computeNCALevel(PathId other);

    public int computeNCALevel(int tlevel, PathId other, int olevel);

    /**
     * Returns a new NodeId representing the ancestor at the level above the
     * current node. If the ancestor is the root, the root id will be returned.
     *
     * @return the id of the ancestor at the level or the root id
     */
    PathId getAncestor(int level);

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
    boolean after(PathId other, boolean isFollowing);

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
    boolean before(PathId other, boolean isPreceding);

    /**
     * Is the current node id a descendant of the specified node?
     *
     * @param ancestor node id of the potential ancestor
     * @return true if the node id is a descendant of the given node, false
     * otherwise
     */
    boolean isDescendantOf(PathId ancestor);

    boolean isDescendantOrSelfOf(PathId ancestor);

    /**
     * Is the current node a child node of the specified parent?
     *
     * @param parent the parent node
     * @return true if the current node is a child of the specified parent
     */
    boolean isChildOf(PathId parent);

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
    int computeRelation(PathId ancestor);

    boolean isSiblingOf(PathId sibling);

    /**
     * Returns the level within the document tree at which this node occurs.
     *
     * @return the tree level
     */
    int getLevel();

    int compareTo(PathId other);

    public boolean equals(PathId other);

    public boolean isNotEqualTo(PathId other);

    public boolean isEqualTo(PathId other);

    public boolean lessThan(PathId other);

    public boolean lessThanOrEqualTo(PathId other);

    PathId getNextFirstDescendant(int level);

    public boolean isAncestorOrDescendant(PathId anc);

    String debug();

    /**
     * Write the node id to a
     * {@link org.exist.storage.io.VariableByteOutputStream}.
     *
     * @param os the output stream
     * @throws java.io.IOException if there's a problem with the underlying
     * output stream
     */
    void write(ObjectOutputStream os) throws IOException;

}
