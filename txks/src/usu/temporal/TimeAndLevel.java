package usu.temporal;

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The TimeAndLevel class is not currently used
 * @author Curtis Dyreson
 */
public class TimeAndLevel extends Time implements Comparable, Serializable {

    int top = 0;
    int bottom = Integer.MAX_VALUE;

    public TimeAndLevel() {
        super();
        top = 0;
        bottom = Integer.MAX_VALUE;
    }

    public TimeAndLevel(int b) {
        super(b);
        top = 0;
        bottom = Integer.MAX_VALUE;
    }

    public TimeAndLevel(int b, int e) {
        super(b, e);
        top = 0;
        bottom = Integer.MAX_VALUE;
    }
    
    public TimeAndLevel(int b, int e, int top, int bottom) {
        super(b, e);
        this.top = top;
        this.bottom = bottom;
    }

    public TimeAndLevel(String time) {
        super(time);
        top = 0;
        bottom = Integer.MAX_VALUE;
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

    public TimeAndLevel addToTupleInput(TupleInput in) {
        super.addToTupleInput(in);
        begin = in.readInt();
        end = in.readInt();
        return this;
    }

    public void addToTupleOutput(TupleOutput out) {
        super.addToTupleOutput(out);
        out.writeInt(begin);
        out.writeInt(end);
    }

    public boolean overlaps(TimeAndLevel other) {
        return (other.end == UNTIL_CHANGED)
                ? (this.end == UNTIL_CHANGED)
                        ? /* this b --- uc, other o -- uc */ true
                        : /* this b -- e, other o -- uc */ (this.end >= other.begin)
                : (this.end == UNTIL_CHANGED)
                        ? /* this b -- uc, other o - g */ (other.end >= this.begin)
                        : /* this b -- e, other o - g */ (this.begin > other.begin) ? this.begin <= other.end : other.begin <= this.end;
    }

    public boolean before(TimeAndLevel other) {
        return (this.end == UNTIL_CHANGED)
                ? false : (other.begin > this.end);
    }

    public boolean endsBefore(TimeAndLevel other) {
        return (this.end == UNTIL_CHANGED)
                ? false : (other.end == UNTIL_CHANGED) ? true : (this.end < other.end);
    }

    public boolean meets(TimeAndLevel other) {
        return (this.end == UNTIL_CHANGED)
                ? false : (other.begin == this.end);
    }

    /*
     * Union of two times, knowing they overlap
     */
    public TimeAndLevel unionIfOverlaps(TimeAndLevel other) {
        //System.out.println("computing union");
        return (this.begin <= other.begin)
                ? (this.end == UNTIL_CHANGED)
                        ? /* o-b-e-uc */ this
                        : (other.end == UNTIL_CHANGED)
                                ? /* o-b-g-uc */ new TimeAndLevel(this.begin, other.end)
                                : (this.end >= other.end)
                                        ? /* o-b-e-g */ this
                                        : /* o-b-g-e */ new TimeAndLevel(this.begin, other.end)
                : (other.end == UNTIL_CHANGED)
                        ? /* b-o-e-uc */ other
                        : (end == UNTIL_CHANGED)
                                ? /* b-o-g-uc */ new TimeAndLevel(other.begin, this.end)
                                : (this.end >= other.end)
                                        ? /* b-o-e-g */ new TimeAndLevel(other.begin, this.end)
                                        : /* b-o-g-e */ other;      
    }

    /*
     * Intersection of two times, if there is no intersection return null
     */
    public TimeAndLevel intersection(TimeAndLevel other) {
        if (this.overlaps(other)) {
            return intersectionIfOverlaps(other);
        }
        return null;
    }

    /*
     * This assumes that the Times overlap
     */
    public TimeAndLevel intersectionIfOverlaps(TimeAndLevel other) {
        //System.out.println("computing intersection");
        return (this.begin > other.begin)
                ? (other.end == UNTIL_CHANGED)
                        ? /* o-b-e-uc */ this
                        : (end == UNTIL_CHANGED)
                                ? /* o-b-g-uc */ new TimeAndLevel(this.begin, other.end)
                                : (this.end <= other.end)
                                        ? /* o-b-e-g */ this
                                        : /* o-b-g-e */ new TimeAndLevel(this.begin, other.end)
                : (other.end == UNTIL_CHANGED)
                        ? /* b-o-e-uc */ new TimeAndLevel(other.begin, this.end)
                        : (end == UNTIL_CHANGED)
                                ? /* b-o-g-uc */ other
                                : (this.end <= other.end)
                                        ? /* b-o-e-g */ new TimeAndLevel(other.begin, this.end)
                                        : /* b-o-g-e */ other;

    }

     /*
     * Contains of two times, if there is no intersection return null
     */
    public TimeAndLevel contains(TimeAndLevel other) {
        if (this.overlaps(other)) {
            return containsIfOverlaps(other);
        }
        return null;
    }
    
    public TimeAndLevel containsIfOverlaps(TimeAndLevel other) {
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

    public List<TimeAndLevel> difference(TimeAndLevel other) {
        //System.out.println("computing intersection");
        List<TimeAndLevel> times = new ArrayList(2);
        Boolean added = (this.begin >= other.begin)
                ? (other.end == UNTIL_CHANGED)
                        ? /* o-b-e-uc */ false /* no Time for difference */
                        : (end == UNTIL_CHANGED)
                                ? /* o-b-g-uc */ times.add(new TimeAndLevel(other.end + 1, this.end))
                                : (this.end <= other.end)
                                        ? /* o-b-e-g */ false
                                        : /* o-b-g-e */ times.add(new TimeAndLevel(other.end + 1, this.end))
                : (other.end == UNTIL_CHANGED)
                        ? /* b-o-e-uc */ times.add(new TimeAndLevel(this.begin, other.begin - 1))
                        : (end == UNTIL_CHANGED)
                                ? /* b-o-g-uc */ times.add(new TimeAndLevel(this.begin, other.begin - 1)) && times.add(new TimeAndLevel(other.end + 1, this.end))
                                : (this.end <= other.end)
                                        ? /* b-o-e-g */ times.add(new TimeAndLevel(this.begin, other.begin - 1))
                                        : /* b-o-g-e */ times.add(new TimeAndLevel(this.begin, other.begin - 1)) && times.add(new TimeAndLevel(other.end + 1, this.end));

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
        final TimeAndLevel other = (TimeAndLevel) obj;
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
        return equals((TimeAndLevel) obj);
    }

    public boolean equals(TimeAndLevel other) {
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
