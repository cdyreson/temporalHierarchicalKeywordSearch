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
 *  $Id: DLNFactory.java 3632 2006-05-31 16:03:32Z wolfgang_m $
 */
package usu.dln.old;

//import xmorph.org.exist.storage.io.VariableByteInput;
//import xmorph.org.exist.storage.io.VariableByteOutputStream;
import usu.dln.*;
import java.io.IOException;
import usu.NodeIdFactory;
import usu.PathIdFactory;
import usu.NodeId;

/**
 * Implementation of {@link NodeIdFactory} for DLN-based node ids.
 */
public class SortedDLNFactory extends DLNFactory implements NodeIdFactory, PathIdFactory {
   
    /*
     public NodeId createFromStream(VariableByteInput is) throws IOException {
     short bitCnt = is.readShort();
     return bitCnt == 0 ? DLN.END_OF_DOCUMENT : new DLN(bitCnt, is);
     }
    
    public SortedDLN createFromData(byte[] data, int startOffset) {
        return new SortedDLN(data, (short) startOffset);
    }
 */
    public SortedDLN createFromString(String string) {
        return new SortedDLN(string);
    }

    public SortedDLN rootId() {
        return SortedDLN.SORTED_ROOT_NODE;
    }

    public SortedDLN minId() {
        return SortedDLN.SORTED_MIN_NODE;
    }

    public SortedDLN maxId() {
        return SortedDLN.SORTED_MAX_NODE;
    }

}
