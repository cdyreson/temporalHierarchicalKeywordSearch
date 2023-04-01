/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usu.dln;

import usu.PathId;
import usu.PathIdBinding;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.bind.tuple.TupleInput;
import java.util.Arrays;
import com.sleepycat.bind.tuple.TupleMarshalledBinding;

/**
 *
 * @author Curt
 */
public class DLNPathIdBinding extends PathIdBinding {
//public class SortedNodeIdDLNBinding extends TupleMarshalledBinding<SortedDLN> {

    @Override
    public DLN entryToObject(TupleInput in) {

        int i = in.getBufferLength();
        byte[] r = in.getBufferBytes();
        //System.out.println(SortedDLN.SORTED_MAX_NODE.toBitString(r));
        short index = (short) ((((r[i - 2] & 0xff) << 8)) | ((r[i - 1] & 0xff)));
        int length = 1 + (index / 8);
        //System.out.println("i " + i + " imdex " + index + " length " + length + " x " + r.length);
        //byte[] x = new byte[length];
        //System.arraycopy(r,0,x,0,length);
        //in.read(x,i-(length + 1),length);
        //String s = in.readString();
        //s.trim();
        //in.readInt();
        //short i = in.readShort();
        //SortedDLN n = new SortedDLN(s,i);
        DLN n = new DLN(r,length,(short)(index));
        //System.out.println("to SortedDLN " + n.toBitString() + " " + n);
        //System.out.println("string       " + n.toBitString(in.getBufferBytes()));
        return n;
    }

    @Override
    public void objectToEntry(PathId s, TupleOutput out) {
        //System.out.println("from NodeId " + s);
        objectToEntry((DLN) s, out);
    }

    public void objectToEntry(DLN s, TupleOutput out) {
        //System.out.println("from SortedDLN to byte array " + s + " " + s.toBitString() + " " + s.toBitString(s.serializeToString().getBytes()));
        //out.writeString(s.serializeToString());
        out.write(s.bits);
        out.writeUnsignedInt(0);
        out.writeUnsignedShort(s.bitIndex);
        //out.writeInt(0);
    }

}
