/*
 * Copyright (C) 2017 Curtis Dyreson
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package messiah.parse;

import usu.temporal.Time;
import usu.temporal.TimeItem;

/**
 *
 * @author Curtis Dyreson
 */
public class StaticIntervalGenerator implements IntervalGenerator {
    Time time = new Time(0,100);
    
    public StaticIntervalGenerator() {
    }
    
    public StaticIntervalGenerator(int size) {
        this.time = new Time(0, size);
    }
    
    public TimeItem generate(TimeItem parent) {
        return new TimeItem(time);
    }
}
