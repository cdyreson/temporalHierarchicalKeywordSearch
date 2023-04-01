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

import usu.dln.*;
import java.io.Serializable;
import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import usu.NodeId;
import usu.PathId;

/**
 * Represents a sorted node id. BerkeleyDB does not properly sort variable size
 * byte arrays, so special handling is needed.
 */
public class SortedDLN extends DLN implements PathId, NodeId, Comparable {

    protected transient static final SortedDLN SORTED_ROOT_NODE = new SortedDLN(1);
    // Establish a node that is last in the ordering, greater than the root node
    protected transient static final SortedDLN SORTED_MAX_NODE = new SortedDLN(2);
    // Establish a node that is first in the ordering, greater than the root node
    protected transient static final SortedDLN SORTED_MIN_NODE = new SortedDLN(0);

    // Character set for encoding byte array as String
    private final transient static String charset = "ISO-8859-1";

    public SortedDLN() {
        super(1);
    }

    public SortedDLN(int i) {
        super(i);
    }

    public SortedDLN(SortedDLN dln) {
        super(dln);
    }

    public SortedDLN(DLN dln) {
        super(dln);
    }

    public SortedDLN(byte[] data, short bitIndex) {
        super(data, bitIndex);
    }

    public SortedDLN(byte[] data, int length, short bitIndex) {
        super(data, length, bitIndex);
    }

    public SortedDLN(short units, byte[] data, int startOffset) {
        super(units, data, startOffset);
    }

    public SortedDLN(String s) {
        super(s);
    }

    /*
     public SortedDLN(String s) {
     try {
     byte[] r = s.getBytes(charset);
     this.bits = new byte[r.length - 6];
     System.arraycopy(r, 0, this.bits, 0, r.length - 6);
     restoreBitIndex(r);
     } catch (Exception ex) {
     System.err.println("Error in string conversion");
     System.exit(-1);
     }
     }
     */
    public SortedDLN(String s, short i) {
        try {
            byte[] r = s.getBytes(charset);
            this.bits = new byte[r.length - 4];
            System.arraycopy(r, 0, this.bits, 0, r.length - 4);
            //restoreBitIndex(r);
            this.bitIndex = i;
        } catch (Exception ex) {
            System.err.println("Error in string conversion");
            System.exit(-1);
        }
    }

    public String serializeToString() {
        //byte[] foo = new byte[6];
        //encodeBitIndex(foo);
        byte[] foo = new byte[4];
        foo[0] = (byte) 0;
        foo[1] = (byte) 0;
        foo[2] = (byte) 0;
        foo[3] = (byte) 0;
        try {
            //String s = new String(this.bits, charset) + new String(foo, charset);
            String s = new String(this.bits, charset) + new String(foo, charset);
            return s;
        } catch (Exception ex) {
            System.err.println("Error in string conversion");
            System.exit(-1);
        }
        return "";
    }

    /*
     private void encodeBitIndex(byte[] r) {
     int i = r.length;
     // Put bitIndex in place
     r[i - 1] = (byte) (bitIndex & 0xff);
     r[i - 2] = (byte) ((bitIndex >>> 8) & 0xff);
     }

     private void restoreBitIndex(byte[] r) {
     int i = r.length;
     this.bitIndex = (short) ((((r[i - 2] & 0xff) << 8)) | ((r[i - 1] & 0xff)));
     }
     */
    private final static String toBitString(String s) {
        try {
            return toBitString(s.getBytes(charset));
        } catch (Exception ex) {
            System.out.println("error in string conversion");
            System.exit(-1);
        }
        return "error in string conversion";
    }

    @Override
    public SortedDLN getParentId() {
        return new SortedDLN(super.getParentId());
    }

    @Override
    public SortedDLN insertBefore() {
        return new SortedDLN(super.insertBefore());
    }

    //public int hashCode() {
    //     System.out.println("hashcode");
    //      return super.hashCode();
    //   }
    public SortedDLN insertNode(SortedDLN other) {
        return new SortedDLN(super.insertNode((DLN) other));
    }

    @Override
    public SortedDLN newChild() {
        return new SortedDLN(super.newChild());
    }

    @Override
    public SortedDLN nextSibling() {
        return new SortedDLN(super.nextSibling());
    }

    @Override
    public SortedDLN precedingSibling() {
        return new SortedDLN(super.precedingSibling());
    }

    @Override
    public SortedDLN getAncestor(int level) {
        return new SortedDLN(super.getAncestor(level));
    }

    @Override
    public SortedDLN getNextFirstDescendant(int level) {
        return new SortedDLN(super.getNextFirstDescendant(level));
    }

    public SortedDLN commonLCA(SortedDLN other, int level) {
        return new SortedDLN(super.commonLCA(other, level));
    }
    /*
     private void writeObject(java.io.ObjectOutputStream out) throws IOException {
     //System.out.println("Curt: writing " + this);
     int i = this.bits.length;
     if (serialized == null) {
     serialized = new byte[i+2];
     System.arraycopy(this.bits, 0, this.serialized, 0, i);
     } else if (serialized.length != i+2) {
     serialized = new byte[i+2];
     System.arraycopy(this.bits, 0, this.serialized, 0, i);
     } else {
     // serialization already in place
     }
     // Put bitIndex in place
     serialized[i] = (byte) (bitIndex & 0xff);
     serialized[i+1] = (byte) ((bitIndex >>> 8) & 0xff);  
     System.arraycopy(serialized, 0, this.bits, 0, i-2);
     out.write(serialized);
     }

     private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
     //this.bits = new byte[NUMBEROFBYTES];
     this.serialized = (byte[]) in.readObject();
     int i = this.serialized.length;
     this.bitIndex = (short)(((serialized[i] << 8)) | ((serialized[i + 1] & 0xff)));
     this.bits = new byte[i-2];
     System.arraycopy(serialized, 0, this.bits, 0, i-2);
     //this.bitIndex = in.readInt();
     //System.out.println("Curt: reading " + this);
     }
     */
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
    /*
     public void writeExternal(ObjectOutput out) throws IOException {
     //System.out.println("serializing");
     //out.writeObject(this.bits);     
     //System.out.println("seriliazing " + this + " " + this.toBitString() + " " + bits.length + " index " + this.bitIndex);
     // Right justify this sucker
     byte[] r = new byte[16];
     System.arraycopy(bits, 0, r, 0, bits.length);
     encodeBitIndex(r);
     r[15] = (byte) bits.length;
     out.writeObject(r);
     //out.defaultWriteObject();       
     }
     */
    /*
     private Object writeReplace() throws ObjectStreamException {
     System.out.println("replacing " + this + " " + this.toBitString() + " " + bits.length + " index " + this.bitIndex);
     // Right justify this sucker
     byte[] r = new byte[16];
     System.arraycopy(bits, 0, r, 0, bits.length);
     r[15] = (byte) bits.length;
     return r;
     }
     */
    /*
     public void readExternal(ObjectInput in)
     throws IOException, ClassNotFoundException {

     byte[] r = new byte[16];
     r = (byte[]) in.readObject();
     byte length = r[15];
     //System.out.println("deseriliazing length is " + length);
     //this.bitIndex = (short)(((serialized[i] << 8)) | ((serialized[i + 1] & 0xff)));
     this.bits = new byte[length];
     System.arraycopy(r, 0, this.bits, 0, length);
     restoreBitIndex(r);
     //System.out.println("deseriliazing length is " + length + " bitindex " + bitIndex + " " + this.debug());

     //byte[] r = (byte[])in.readObject();
     //in.defaultReadObject();
     //restoreBitIndex();
     }
     */
    /*
     private Object readResolve()
     throws ObjectStreamException {
     byte[] r = new byte[16];
     r = (byte[]) in.readObject();
     byte length = r[15];
     System.out.println("deseriliazing length is " + length);
     //this.bitIndex = (short)(((serialized[i] << 8)) | ((serialized[i + 1] & 0xff)));
     this.bits = new byte[length];
     System.arraycopy(r, 0, this.bits, 0, length);
     restoreBitIndex();
     System.out.println("deseriliazing length is " + length + " bitindex " + bitIndex + " " + this.debug());
     }
     */
    /*
     public byte[] serialize() {
     //System.out.println("seriliazing " + this.toBitString());
     // Right justify this sucker
     byte[] r = new byte[16];
     System.arraycopy(bits, 0, r, 0, bits.length);
     r[15] = (byte) bits.length;
     return r;
     }

     */
    //public static NodeId deserialize(byte[] b) {
    //    return new NodeId(bits);
    //return n;
    //}
    public static void main(String[] args) {
        SortedDLN id0 = new SortedDLN("1.1.7");
        SortedDLN id1 = new SortedDLN("1.1.6");
        SortedDLN id2 = new SortedDLN("1.1.7.1");
        SortedDLN id3 = new SortedDLN("1.1.7/1");
        SortedDLN id4 = (SortedDLN) id0.newChild();
        SortedDLN id5 = (SortedDLN) id0.nextSibling();

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
