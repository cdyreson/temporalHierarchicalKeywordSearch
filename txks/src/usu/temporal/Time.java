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

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Curtis Dyreson
 */
public class Time implements Comparable, Serializable {

    public final static int SERIALIZED_SIZE = 4 + 4;
    public final static int UNTIL_CHANGED = Integer.MAX_VALUE; //-1;
    //public final static int CURRENT_TIME = 1002;
    int begin;
    int end;

    public Time() {
        begin = 0;
        end = UNTIL_CHANGED;
    }

    public Time(int b) {
        begin = b;
        end = UNTIL_CHANGED;
    }

    public Time(int b, int e) {
        begin = b;
        end = e;
    }

    public Time(String time) {
        String[] t = time.split("-");
        begin = Integer.parseInt(t[0]);
        if ("uc".equals(t[1])) {
            end = UNTIL_CHANGED;
        } else {
            end = Integer.parseInt(t[1]);
        }
    }

    public boolean isUntilChanged() {
        return end == UNTIL_CHANGED;
    }

    public int getEndTime() {
        return end;
    }

    public int getBeginTime() {
        return begin;
    }

    public Time addToTupleInput(TupleInput in) {
        begin = in.readInt();
        end = in.readInt();
        return this;
    }

    public void addToTupleOutput(TupleOutput out) {
        out.writeInt(begin);
        out.writeInt(end);
    }

    public boolean overlaps(Time other) {
        return (other.end == UNTIL_CHANGED)
                ? (this.end == UNTIL_CHANGED)
                        ? /* this b --- uc, other o -- uc */ true
                        : /* this b -- e, other o -- uc */ (this.end >= other.begin)
                : (this.end == UNTIL_CHANGED)
                        ? /* this b -- uc, other o - g */ (other.end >= this.begin)
                        : /* this b -- e, other o - g */ (this.begin > other.begin) ? this.begin <= other.end : other.begin <= this.end;
    }

    public boolean before(Time other) {
        return (this.end == UNTIL_CHANGED)
                ? false : (other.begin > this.end);
    }

    public boolean endsBefore(Time other) {
        return (this.end == UNTIL_CHANGED)
                ? false : (other.end == UNTIL_CHANGED) ? true : (this.end < other.end);
    }

    public boolean meets(Time other) {
        return (this.end == UNTIL_CHANGED)
                ? false : (other.begin == this.end);
    }

    /*
     * Union of two times, knowing they overlap
     */
    public Time unionIfOverlaps(Time other) {
        //System.out.println("computing union");
        return (this.begin <= other.begin)
                ? (this.end == UNTIL_CHANGED)
                        ? /* o-b-e-uc */ this
                        : (other.end == UNTIL_CHANGED)
                                ? /* o-b-g-uc */ new Time(this.begin, other.end)
                                : (this.end >= other.end)
                                        ? /* o-b-e-g */ this
                                        : /* o-b-g-e */ new Time(this.begin, other.end)
                : (other.end == UNTIL_CHANGED)
                        ? /* b-o-e-uc */ other
                        : (end == UNTIL_CHANGED)
                                ? /* b-o-g-uc */ new Time(other.begin, this.end)
                                : (this.end >= other.end)
                                        ? /* b-o-e-g */ new Time(other.begin, this.end)
                                        : /* b-o-g-e */ other;      
    }

    /*
     * Intersection of two times, if there is no intersection return null
     */
    public Time intersection(Time other) {
        if (this.overlaps(other)) {
            return intersectionIfOverlaps(other);
        }
        return null;
    }

    /*
     * This assumes that the Times overlap
     */
    public Time intersectionIfOverlaps(Time other) {
        //System.out.println("computing intersection");
        return (this.begin > other.begin)
                ? (other.end == UNTIL_CHANGED)
                        ? /* o-b-e-uc */ this
                        : (end == UNTIL_CHANGED)
                                ? /* o-b-g-uc */ new Time(this.begin, other.end)
                                : (this.end <= other.end)
                                        ? /* o-b-e-g */ this
                                        : /* o-b-g-e */ new Time(this.begin, other.end)
                : (other.end == UNTIL_CHANGED)
                        ? /* b-o-e-uc */ new Time(other.begin, this.end)
                        : (end == UNTIL_CHANGED)
                                ? /* b-o-g-uc */ other
                                : (this.end <= other.end)
                                        ? /* b-o-e-g */ new Time(other.begin, this.end)
                                        : /* b-o-g-e */ other;

    }

     /*
     * Contains of two times, if there is no intersection return null
     */
    public Time contains(Time other) {
        if (this.overlaps(other)) {
            return containsIfOverlaps(other);
        }
        return null;
    }
    
    public Time containsIfOverlaps(Time other) {
        return (this.begin <= other.begin)
                ? (other.end == UNTIL_CHANGED)
                        ? (end == UNTIL_CHANGED) ? this : null
                        : (end == UNTIL_CHANGED)
                                ? null
                                : (this.end >= other.end)
                                        ? /* o-b-e-g */ this
                                        : null
                : null;        
    }

    public List<Time> difference(Time other) {
        //System.out.println("computing intersection");
        List<Time> times = new ArrayList(2);
        Boolean added = (this.begin >= other.begin)
                ? (other.end == UNTIL_CHANGED)
                        ? /* o-b-e-uc */ false /* no Time for difference */
                        : (end == UNTIL_CHANGED)
                                ? /* o-b-g-uc */ times.add(new Time(other.end + 1, this.end))
                                : (this.end <= other.end)
                                        ? /* o-b-e-g */ false
                                        : /* o-b-g-e */ times.add(new Time(other.end + 1, this.end))
                : (other.end == UNTIL_CHANGED)
                        ? /* b-o-e-uc */ times.add(new Time(this.begin, other.begin - 1))
                        : (end == UNTIL_CHANGED)
                                ? /* b-o-g-uc */ times.add(new Time(this.begin, other.begin - 1)) && times.add(new Time(other.end + 1, this.end))
                                : (this.end <= other.end)
                                        ? /* b-o-e-g */ times.add(new Time(this.begin, other.begin - 1))
                                        : /* b-o-g-e */ times.add(new Time(this.begin, other.begin - 1)) && times.add(new Time(other.end + 1, this.end));

        return (added) ? times : null;
    }

    public String debug() {
        return "[" + begin + "," + end + "]";
    }

    @Override
    public String toString() {
        return "[" + begin + "," + end + "]";
    }

    /* Ordering is by begin point, then by end point */
    @Override
    public int compareTo(Object obj) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        //this optimization is usually worthwhile, and can
        //always be added
        if (this == obj) {
            return EQUAL;
        }
        final Time other = (Time) obj;
        return (this.begin > other.begin)
                ? /* b-o- */ AFTER
                : (other.begin < this.begin)
                        ? /* o-b-- */ BEFORE
                        : (other.end == this.end)
                                ? /* ob-eg */ EQUAL
                                : (other.end == UNTIL_CHANGED)
                                        ? /* ob-e-uc */ BEFORE
                                        : (other.end == UNTIL_CHANGED)
                                                ? /* ob-g-uc */ AFTER
                                                : /* ob- e or g? */ (this.end < other.end) ? BEFORE : AFTER;
    }

    @Override
    public int hashCode() {
        return (int) begin + (int) end;
    }

    @Override
    public boolean equals(Object obj) {
        return equals((Time) obj);
    }

    public boolean equals(Time other) {
        if (this.begin != other.begin) {
            return false;
        }
        return this.end == other.end;
    }

    public void setEnd(int endTime) {
        if (endTime == -1) {
            end = UNTIL_CHANGED;
        } else {
            end = endTime;
        }
    }

}
