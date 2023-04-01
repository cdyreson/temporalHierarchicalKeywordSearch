/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package usu.dln.old;

import usu.dln.*;
import usu.PathId;
import usu.PathIdBinding;
//import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.bind.tuple.TupleInput;

/**
 *
 * @author Curt
 */
public class SortedPathIdDLNBinding extends PathIdBinding {

    
      @Override
      public SortedDLN entryToObject(TupleInput in) {
          //System.out.println("Doing it");
          return new SortedDLN(in.readString());
      }
      
      @Override
      public void objectToEntry(PathId s, TupleOutput out) {
          objectToEntry((SortedDLN)s, out);
      }
      
      public void objectToEntry(SortedDLN s, TupleOutput out) {
          out.writeString(s.serializeToString());
          //return entry;
      }
      
}
