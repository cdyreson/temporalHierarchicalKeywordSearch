package messiah;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sleepycat.bind.tuple.TupleBinding;
import usu.dln.*;
import usu.NodeId;
import usu.NodeIdBinding;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.bind.tuple.TupleInput;

/**
 *
 * @author Curt
 */
public class NodeInfoBinding extends TupleBinding<NodeInfo> {

    public NodeInfo entryToObject(TupleInput in) {
        String s = in.readString();
        int i = in.readInt();
        System.out.println("Doing it " + s + " " + i);
        return new NodeInfo(null, s, i);
    }

    public void objectToEntry(NodeInfo s, TupleOutput out) {
        System.out.println("reverse " + s);
        out.writeString(s.getData());
        out.writeInt(s.getLevel());
    }

}
