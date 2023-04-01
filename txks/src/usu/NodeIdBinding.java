/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package usu;

import usu.dln.*;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.bind.tuple.TupleInput;

/**
 *
 * @author Curt
 */
public abstract class NodeIdBinding extends TupleBinding<NodeId> {
  
      @Override
      abstract public NodeId entryToObject(TupleInput in);
      
      @Override
      abstract public void objectToEntry(NodeId s, TupleOutput out);
          //return entry;
     
      
}
