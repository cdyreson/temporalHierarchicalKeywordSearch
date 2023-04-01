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
package usu.dln;

//import xmorph.org.exist.storage.io.VariableByteInput;
//import xmorph.org.exist.storage.io.VariableByteOutputStream;
import java.io.IOException;
import usu.NodeIdFactory;
import usu.PathIdFactory;
import usu.NodeId;

/**
 * Implementation of {@link NodeIdFactory} for DLN-based node ids.
 */
public class DLNFactory implements NodeIdFactory, PathIdFactory {

    public DLN createInstance(int id) {
        return new DLN(id);
    }

    /*
     public NodeId createFromStream(VariableByteInput is) throws IOException {
     short bitCnt = is.readShort();
     return bitCnt == 0 ? DLN.END_OF_DOCUMENT : new DLN(bitCnt, is);
     }
     */
    public DLN createFromData(byte[] data, int startOffset) {
        return new DLN(data, (short) startOffset);
    }

    public DLN createFromString(String string) {
        return new DLN(string);
    }

    public DLN rootId() {
        return DLN.ROOT_NODE;
    }

    public DLN minId() {
        return DLN.MIN_NODE;
    }

    public DLN maxId() {
        return DLN.MAX_NODE;
    }

    public int lengthInBytes(int units, byte[] data, int startOffset) {
        return DLN.getLengthInBytes(units, data, startOffset);
    }

    /*
     public void writeEndOfDocument(VariableByteOutputStream os) {
     os.writeShort(0);
     }
     */
}
