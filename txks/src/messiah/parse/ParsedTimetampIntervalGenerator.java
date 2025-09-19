/*
 * Copyright (C) 2017 Curtis Dyreson
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package messiah.parse;

import java.util.Stack;
import usu.temporal.Time;
import usu.temporal.TimeItem;

/**
 *
 * @author Curtis Dyreson
 */
public class ParsedTimetampIntervalGenerator implements IntervalGenerator {
    public static Stack<TimeItem> timeStack = new Stack();
    
    public ParsedTimetampIntervalGenerator() {
        timeStack = new Stack();
        timeStack.push(new TimeItem(new Time()));
    }
    
    public ParsedTimetampIntervalGenerator(int size) {
        timeStack = new Stack();
        timeStack.push(new TimeItem(new Time(0, size)));
    }
    
    @Override
    public TimeItem generate(TimeItem parent) {
        //System.out.println("Timestack size is " + timeStack.size());
        //if (timeStack.isEmpty()) timeStack.push(new TimeItem());
        return timeStack.peek();
    }
    
    public static void pushTime(TimeItem t) {
        // Make sure it is within the current top time
        // TimeItem top = timeStack.peek();
        //timeStack.push(new TimeItem(top.getTime().intersection(t)));
        timeStack.push(t);
    }
    
    public static void popTime() {
        timeStack.pop();
    }
}
