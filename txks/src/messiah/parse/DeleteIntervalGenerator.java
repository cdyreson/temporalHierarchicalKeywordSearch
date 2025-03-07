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
import usu.temporal.TimeItem;

/**
 *
 * @author Curtis Dyreson
 */
public class DeleteIntervalGenerator implements IntervalGenerator {
    static int percentage = 0;
    static int count = 1;
    static int deleted = 0;
    static Time defaultTime = new Time(0,100);
    static Random rand = new Random();
    
    public DeleteIntervalGenerator(int percentage) {
        this.percentage = percentage;
    }
    
    public TimeItem generate(TimeItem parentItem) {
        Time parent = parentItem.getTime();
        Time t;
        if ((deleted*100/count) <= percentage) {
            deleted++;
            t = new Time(parent.getBeginTime(), Math.max(parent.getBeginTime(), parent.getEndTime()-1));
        } else {
            t = parent;
        }
        count++;
        return new TimeItem(t);
    }
}
