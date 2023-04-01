/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usu.dln.old;

import usu.dln.old.ItemDLN;
import usu.NodeId;
import usu.NodeIdBinding;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.bind.tuple.TupleInput;
import usu.dln.DLN;
import usu.dln.DLNNodeIdBinding;
import usu.temporal.Time;
//import java.util.Arrays;
//import com.sleepycat.bind.tuple.TupleMarshalledBinding;

/**
 *
 * @author Amani Shatnawi
 */
public class ItemDLNNodeIdBinding extends NodeIdBinding {
    private DLNNodeIdBinding dlnBinding;

    public ItemDLNNodeIdBinding() {
        dlnBinding = new DLNNodeIdBinding();
    }
    
    @Override
    public ItemDLN entryToObject(TupleInput in) {
        System.out.println("reading historyDLN");
        int lengthOfDln = in.getBufferLength() - Time.SERIALIZED_SIZE;
        DLN dln = dlnBinding.entryToObject(in, lengthOfDln);
        Time time = new Time();
        in.skip(lengthOfDln);
        time.addToTupleInput(in);
        return new ItemDLN(dln, time);
    }

    @Override
    public void objectToEntry(NodeId s, TupleOutput out) {
        objectToEntry((ItemDLN) s, out);
    }

    public void objectToEntry(ItemDLN s, TupleOutput out) {
        System.out.println("writing itemDLN");
        dlnBinding.objectToEntry(s, out);
    }

}
