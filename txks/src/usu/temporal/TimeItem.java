package usu.temporal;

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A time item is an object that is either a single time, or a list of times and levels
 * @author Curtis Dyreson
 */
public class TimeItem implements Serializable {

    Time time = null;
    boolean isMoved = false;
    TimeAndLevelList<Time> times = null;

    public TimeItem() {
        time = new Time();
    }
    
    public TimeItem(TimeAndLevelList<Time> t) {
        times = t;
        isMoved = true;
    }
    
    public TimeItem(Time t) {
        time = t;
    }

    public TimeItem(int b) {
        time = new Time(b);
    }

    public TimeItem(int b, int e) {
        time = new Time(b,e);
    }

    public TimeItem(String time) {
        this.time = new Time(time);
    }
    
    public void moved() {
        isMoved = true;
        times = new TimeAndLevelList();
    }
    
    public boolean isMoved() {
        return isMoved;
    }
    
    public TimeAndLevelList<Time> getTimes() {
        return times;
    }
        
    public Time getTime() {
        return time;
    }
    
    public void addTime(Time t, int level) {
        times.add(time, level);
    }

    @Override
    public String toString() {
        if (isMoved) {
            return times.toString();
        }
        return time.toString();
    }

}
