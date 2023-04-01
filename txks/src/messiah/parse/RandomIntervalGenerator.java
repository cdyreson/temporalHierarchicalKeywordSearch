/*
 * Copyright (C) 2017 Curtis Dyreson
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package messiah.parse;

import java.util.Random;
import usu.temporal.Time;

/**
 *
 * @author Curtis Dyreson
 */
public class RandomIntervalGenerator implements IntervalGenerator {
    static int maxRange = 100;
    static int maxInterval = 100;
    static boolean isRandomIntervals = false;
    static Time defaultTime = new Time(0,100);
    static Random rand = new Random();
    
    public RandomIntervalGenerator(int maxRange, int maxInterval, boolean isRandom) {
        this.maxRange = maxRange;
        this.maxInterval = maxInterval;
        this.isRandomIntervals = isRandom;
    }
    
    public Time generate(Time parent) {
        Time t;
        if (isRandomIntervals) {
            int begin = parent.getBeginTime();
            int end = Math.min(maxRange, parent.getEndTime());
            //System.out.println("begin " + begin + " end " + end);
            //int size = (end - begin) > 0 ? rand.nextInt(Math.min(maxInterval, end-begin)) : 0;
            int size = (end - begin) > 0 ? rand.nextInt(maxInterval) : 0;
            int offset = ((end - begin) - size) > 0 ? rand.nextInt((end - begin) - size) : 0;
            t = new Time(Math.max(begin, begin + offset), Math.min(end, begin + offset + size));
            //System.out.println("Time is " + t);
        } else {
            t = new Time(0, maxInterval);
        }
        return t;
    }
}
