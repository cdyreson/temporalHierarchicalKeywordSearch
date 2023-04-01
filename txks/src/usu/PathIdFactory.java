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
 *  $Id: PathIdFactory.java 4427 2006-09-28 13:33:50Z dizzzz $
 */
package usu;

import usu.PathId;


import java.io.IOException;

/**
 * A factory for creating node ids. To support different numbering
 * schemes, PathId instances should always be created through
 * this interface.
 *
 * The PathIdFactory for the current database instance can be
 * retrieved from {@link org.exist.storage.BrokerPool#getNodeFactory()}.
 */
public interface PathIdFactory {

    /**
     * Create a new PathId, initialized with the given
     * base id.
     * 
     * @param id
     * @return nodeId
     */
    PathId createInstance(int id);

    /**
     * Read a PathId from the given byte array. Start to read at
     * startOffset. sizeHint indicates the length of the id in an
     * implementation dependent manner. Some implementations
     * may require sizeHint to be specified, others not.
     *
     * @param data the byte array to read from
     * @param startOffset offset into the byte array
     * @return the PathId read
     */
    PathId createFromData(byte[] data, int startOffset);

    /**
     * Create a PathId instance from its string representation.
     * 
     * @param string the string representation of the node id as returned
     * by {@link Object#toString()}
     * @return nodeId
     */
    PathId createFromString(String string);
    
    /**
     * Returns the number of bytes occupied by the PathId stored
     * in the byte array at the given startOffset. This method is
     * similar to {@link #createFromData(int, byte[], int)}, but it
     * just returns the number of bytes.
     *
     * @param units
     * @param data
     * @param startOffset
     * @return number of bytes
     */
    int lengthInBytes(int units, byte[] data, int startOffset);

    /**
     * Returns a PathId representing the document node of a document.
     * Usually, this will be a singleton object.
     *
     * @return the document node id.
     */
    PathId rootId();
    
        /**
     * Returns a PathId representing the smallest possible nodeid.
     *
     * @return the minimum node id.
     */
    PathId minId();
    
            /**
     * Returns a PathId representing the greatest possible nodeid.
     *
     * @return the minimum node id.
     */
    PathId maxId();

}