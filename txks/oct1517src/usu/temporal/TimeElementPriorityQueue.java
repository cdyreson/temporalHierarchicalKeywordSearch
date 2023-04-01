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
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * A TimeElement is a set of Time objects. It gathers the objects and performs
 * some of the temporal operations on them.
 *
 * @author Curtis Dyreson
 */
public class TimeElementPriorityQueue implements Serializable {

    //Set<Time> timeSet;
    PriorityQueue<Integer> beginTimes;
    PriorityQueue<Integer> endTimes;

    public TimeElementPriorityQueue() {
        //timeSet = new HashSet();
        beginTimes = new PriorityQueue();
        endTimes = new PriorityQueue();
    }

    public void add(Time time) {
        beginTimes.add(time.begin);
        endTimes.add(time.end);
    }
    
    public boolean hasTimes() {
        return !(beginTimes.isEmpty() || endTimes.isEmpty());
    }

    /*
     A Constructor,
     * Computes the intersection of this Element with another.
     Returns null if there is no intersectin.
     */
    public TimeElementPriorityQueue intersection(TimeElementPriorityQueue other) {
        TimeElementPriorityQueue constructed = new TimeElementPriorityQueue();
        Iterator<Integer> beginIter = beginTimes.iterator();
        Iterator<Integer> endIter = endTimes.iterator();
        Iterator<Integer> otherEndIter = other.endTimes.iterator();
        Iterator<Integer> otherBeginIter = other.beginTimes.iterator();
        if (!beginIter.hasNext()) {
            return null;
        }
        if (!endIter.hasNext()) {
            return null;
        }
        if (!otherEndIter.hasNext()) {
            return null;
        }
        Integer begin = beginIter.next();
        Integer end = endIter.next();
        Integer otherEnd = otherEndIter.next();
        Integer otherBegin = otherBeginIter.next();
        while (true) {
            while (end < otherBegin) {
                if (!endIter.hasNext()) {
                    return constructed;
                }
                //if (!beginIter.hasNext()) {
                //        return constructed;
                //    }
                end = endIter.next();
                begin = beginIter.next();
            }
            // Check to see if we have an intersectino
            if (otherEnd < begin) {
                // No intersection
                if (!otherBeginIter.hasNext()) {
                    return constructed;
                }
                //Can probably omit this test
                //if (!otherEndIter.hasNext()) {
                //    return constructed;
                //}
                otherEnd = otherEndIter.next();
                otherBegin = otherBeginIter.next();
            } else {
                Time t = new Time((begin > otherBegin) ? begin : otherBegin, (end < otherEnd) ? end : otherEnd);
                constructed.add(t);
                if (begin < otherBegin) {
                    // Advance this
                    if (!endIter.hasNext()) {
                        return constructed;
                    }
                    //if (!beginIter.hasNext()) {
                    //        return constructed;
                    //    }
                    end = endIter.next();
                    begin = beginIter.next();
                } else {
                    // Advance other
                    // No intersection
                    if (!otherBeginIter.hasNext()) {
                        return constructed;
                    }
                    //Can probably omit this test
                    //if (!otherEndIter.hasNext()) {
                    //    return constructed;
                    //}
                    otherEnd = otherEndIter.next();
                    otherBegin = otherBeginIter.next();
                }
            }
        }
    }

    public String toString() {
        String s = "begins ";
        for (Integer time : this.beginTimes) {
            s += time;
        }
        s += " ends ";
        for (Integer time : this.endTimes) {
            s += time;
        }
        return "{" + s + "}";
    }

}
