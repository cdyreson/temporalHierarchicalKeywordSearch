/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usu.dln;

import usu.NodeId;
import usu.NodeIdBinding;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.bind.tuple.TupleInput;
//import java.util.Arrays;
//import com.sleepycat.bind.tuple.TupleMarshalledBinding;

/**
 *
 * @author Curt
 */
public class DLNNodeIdBinding extends NodeIdBinding {
//public class SortedNodeIdDLNBinding extends TupleMarshalledBinding<SortedDLN> {

    @Override
    public DLN entryToObject(TupleInput in) {
        return entryToObject(in, in.getBufferLength());
    }

    public DLN entryToObject(TupleInput in, int lengthOfDln) {
        //int i = in.getBufferLength() - lengthOfDln;
        int i = lengthOfDln;
        byte[] r = in.getBufferBytes();
        //System.out.println(.toBitString(r));
        short index = (short) ((((r[i - 2] & 0xff) << 8)) | ((r[i - 1] & 0xff)));
        //System.out.println("index is " + index + " " + i + " " + r[i-2] + " " + r[i-1]);
        int length = 1 + (index / 8);
        DLN n = new DLN(r, length, (short) (index));
        //System.out.println("to SortedDLN " + n.toBitString() + " " + n);
        //System.out.println("string       " + n.toBitString(in.getBufferBytes()));
        return n;
    }

    @Override
    public void objectToEntry(NodeId s, TupleOutput out) {
        //System.out.println("from NodeId " + s);
        objectToEntry((DLN) s, out);
    }

    public void objectToEntry(DLN s, TupleOutput out) {
        //System.out.println("from SortedDLN to byte array " + s + " " + s.toBitString() + " " + s.bitIndex);
        out.write(s.bits);
        out.writeUnsignedInt(0);
        out.writeUnsignedShort(s.bitIndex);
    }

}
