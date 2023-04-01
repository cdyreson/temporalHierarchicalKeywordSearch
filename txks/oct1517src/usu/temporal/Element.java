/*
 * Copyright (C) 2015 Curtis Dyreson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package usu.temporal;

//import com.sleepycat.bind.tuple.TupleInput;
//import com.sleepycat.bind.tuple.TupleOutput;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * An Element is a set of Time objects. It gathers the objects and performs some
 * of the temporal operations on them.
 *
 * @author Curtis Dyreson
 */
public class Element implements Serializable {

    Set<Time> timeSet;

    public Element() {
        timeSet = new HashSet();
    }

    public void add(Time time) {
        timeSet.add(time);
    }

    /*
     A Constructor,
     * Computes the intersection of this Element with another.
     Returns null if there is no intersectin.
     */
    public Element intersection(Element other) {
        Element constructed = new Element();
        for (Time otherTime : other.timeSet) {
            for (Time time : this.timeSet) {
                //if (otherTime.end < time.begin) break;
                Time intersection = time.intersection(otherTime);
                if (intersection != null) {
                    constructed.add(intersection);
                }
            }
        }
        return constructed;
    }

    public String toString() {
        String s = "";
        for (Time time : this.timeSet) {
            s += time;
        }
        return "{" + s + "}";
    }

}
