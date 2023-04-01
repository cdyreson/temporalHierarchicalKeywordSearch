/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package usu;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.bind.tuple.TupleInput;

/**
 *
 * @author Curt
 */
public abstract class PathIdBinding extends TupleBinding<PathId> {
  
      @Override
      abstract public PathId entryToObject(TupleInput in);
      
      @Override
      abstract public void objectToEntry(PathId s, TupleOutput out);
         
}
