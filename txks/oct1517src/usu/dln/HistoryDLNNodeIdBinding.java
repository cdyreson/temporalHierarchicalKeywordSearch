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
import usu.temporal.Time;
//import java.util.Arrays;
//import com.sleepycat.bind.tuple.TupleMarshalledBinding;

/**
 *
 * @author Curt
 */
public class HistoryDLNNodeIdBinding extends NodeIdBinding {
    private DLNNodeIdBinding dlnBinding;

    public HistoryDLNNodeIdBinding() {
        dlnBinding = new DLNNodeIdBinding();
    }
    
    @Override
    public HistoryDLN entryToObject(TupleInput in) {
        //System.out.println("reading historyDLN " + in.getBufferLength() + " " + Time.SERIALIZED_SIZE);
        int lengthOfDln = (in.getBufferLength() - Time.SERIALIZED_SIZE) /*- 2 */;
        DLN dln = dlnBinding.entryToObject(in, lengthOfDln);
        Time time = new Time();
        in.skip(lengthOfDln);
        time.addToTupleInput(in);
        return new HistoryDLN(dln, time); /*, in.readBoolean());*/
    }

    @Override
    public void objectToEntry(NodeId s, TupleOutput out) {
        objectToEntry((HistoryDLN) s, out);
    }

    public void objectToEntry(HistoryDLN s, TupleOutput out) {
        //System.out.println("writing historyDLN " + out.size());
        dlnBinding.objectToEntry(s, out);
        Time time = s.getTime();
        time.addToTupleOutput(out);
        //System.out.println("writing2 historyDLN " + out.size());
        //out.writeBoolean(s.isMoved);
    }

}
