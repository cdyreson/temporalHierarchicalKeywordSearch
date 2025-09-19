/*
 * Copyright (C) 2025 Curtis Dyreson
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package usu.temporal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author curt
 */
public class TimeAndLevelList<T> implements Serializable {
    List<TimeLevelPair<T>> myList;
    
    public TimeAndLevelList() {
        myList = new ArrayList(2);
    }
    
    /*
    Find the time given the level, the times are in ascending level order
    If it returns null, then there is no such timestamp
    */
    public T getTime(int slcaLevel) {
        for (int i = 0; i < myList.size(); i++) {
            TimeLevelPair<T> currentPair = myList.get(i);
            if (currentPair.level > slcaLevel) {
                // Found it as the previous pair
                
                // No previous pair?
                if (i == 0) {
                    // No such time
                    return null;
                } else {
                    return myList.get(i-1).time;
                }
            }
        }
        // It is the last thing in the list, return it
        if (myList.isEmpty()) {
            return null;
        } else {
            return myList.get(myList.size()-1).time;
        }
    }
    
    public void add(T time, int slcaLevel) {
        // Create the list if it is empty
        if (myList == null) {
            myList = new ArrayList(1);
        }
        
        // Create the element to add
        TimeLevelPair<T> pair = new TimeLevelPair(time, slcaLevel);
        
        // If empty then insert here
        if (myList.isEmpty()) {
            if (slcaLevel == 0) {
                myList.add(pair);
            } else {
                // Create a null Time above the time to add
                myList.add(new TimeLevelPair(null, 0));
                myList.add(pair);
            }
        } else {
           
            // Create a new list to copy the old list into
            List<TimeLevelPair<T>> newList = new ArrayList(myList.size()+1);
            // Find the place to insert, must be kept in level order
            
            boolean added = false;
            for (int i = 0; i < myList.size(); i++) {
                TimeLevelPair<T> currentPair = myList.get(i);
                if (!added && currentPair.level >= slcaLevel) {
                    newList.add(pair);
                    added = true;
                }
                newList.add(currentPair);
            }
            // Add if not added
            if (!added) {
                newList.add(pair);
            } 
            myList = newList;
        }
    }   
    
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < myList.size(); i++) {
            s += myList.get(i).level;
            if (myList.get(i).time == null) {
                s += ": null";
            } else {
                s += ":" + myList.get(i).time.toString();
            }
            s += ", ";
        }
        return s;
    }
}

class TimeLevelPair<T> {
    T time;
    int level;
    
    TimeLevelPair(T time, int level) {
        this.time = time;
        this.level = level;
    }
    
}
