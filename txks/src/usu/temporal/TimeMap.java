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

import java.util.*;
import java.io.*;

/**
 *
 * @author Curtis Dyreson
 * @param <K>
 * @param <V>
 */
//public class TimeMap<K,V> extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable {
public class TimeMap<K extends Comparable, V> implements Cloneable, Serializable {
    //TreeMap<Pair, V> map;
    TreeMap<String, List<V>> timeMap;
    TreeMap<K, TreeMap<String, V>> valueMap;

    public TimeMap() {
        //map = new TreeMap();
        timeMap = new TreeMap();
        valueMap = new TreeMap();
    }

    /*
    * Remove everything from the map
    */
    public void clear() {
        //map.clear();
        timeMap.clear();
        valueMap.clear();
    }

    /*
    * Put key, value combination into the map from time b to e
    * WILL OVERWITE existing pair
    * @param key - Key object
    * @param value - value object
    * @param b - beginning time
    * @param e - ending time
    * returns value that was put, 
    */
    public V put(K key, V value, Time t) {
        Set<String> ls = (new LogSegments()).segment(t.begin, t.end);
        
        // First add to valueMap
        if (!valueMap.containsKey(key)) {
            valueMap.put(key, new TreeMap());
        }
        TreeMap<String, V> myMap = valueMap.get(key);
        for (String seg : ls) {
            if (myMap.containsKey(seg)) {
                System.out.println("already contains key");
            }
            System.out.println("putting myMap " + seg + " value " + value);                  
            myMap.put(seg, value);           
        }
        
        // Next add to timeMap
        for (String seg : ls) {
            if (!timeMap.containsKey(seg)) {
                timeMap.put(seg, new ArrayList(4));
            }
            List<V> a = timeMap.get(seg);
            a.add(value);
        }
        return value;
    }

    public V get(K key, Time t) {
        // Does it contain the key?
        if (!valueMap.containsKey(key)) return null;
        
        System.out.println("key found");
        // Look for key at that time
        TreeMap<String, V> myMap = valueMap.get(key);
        String start = (new LogSegment()).segment(t.begin);
        String finish = (new LogSegment()).segment(t.end);
        
        System.out.println("key found " + start + " finish " + finish);
        //Pair first = map.ceilingKey(new Pair(key, start));
        String first = myMap.floorKey(start);
        if (first == null) {
            return null;
        }
        //Pair last = map.floorKey(new Pair(key, finish));
        String last = myMap.ceilingKey(finish);
        if (last == null) {
            return null;
        }
        SortedMap<String, V> submap = myMap.subMap(first, last);
        for (String s : submap.keySet()) {
            if (myMap.containsKey(s)) {
                return myMap.get(s);
            }
        }
        return null;
    }

}
