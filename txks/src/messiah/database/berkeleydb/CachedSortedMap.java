/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messiah.database.berkeleydb;

import com.sleepycat.bind.EntryBinding;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import messiah.Config;

/**
 *
 * @author curt
 * @param <K>
 * @param <V>
 */
public class CachedSortedMap<K extends Comparable, V> implements SortedMap<K, V> {

    SortedMap<K, V> memoryMap;
    SortedMap<K, V> dbMap;
    boolean empty;

    /**
     * The map needs a Database (or actually just the metadataTable)
     *
     * @param db
     * @param name
     * @param tupleBinding
     * @param classType
     */
    public CachedSortedMap(SortedMap<K, V> dbMap) {
        this.dbMap = dbMap;
        this.memoryMap = new TreeMap();
        empty = true;
    }

    /*
     * Get looks through all of the tables before returning null (not found)
     *
     * @param o = The key
     */
    @Override
    public V get(Object o) {
        K key = (K) o;
        V value;

        // Check the memory map
        value = memoryMap.get(key);
        if (value != null) {
            return value;
        }

        if (empty) {
            return null;
        }

        // Check the disk map
        value = dbMap.get(key);
        if (value != null) {
            return value;
        }

        // Not found
        return null;
    }


    /*
     * Check all of the tables before returning null (not found)
     *
     * @param o = The key
     */
    @Override
    public boolean containsKey(Object o) {
        K key = (K) o;
        // Check the memory map first
        V value = memoryMap.get(key);
        if (value != null) {
            return true;
        }

        if (empty) {
            return false;
        }

        // Check the db map
        value = dbMap.get(key);
        if (value != null) {
            return true;
        }

        // Not found
        return false;
    }

    /*
     * Commit this map by writing any values in memory to disk,
     * also clear the memory map
     */
    public void commitAndClear() {
        //System.out.println("CachedSortedMap commit and clear");
        if (memoryMap.size() > 0) {
            commit();
            memoryMap = new TreeMap();
        }
    }

    /*
     * Commit this map by writing any values in memory to disk
     */
    public void commit() {
        //System.out.println("CachedSortedMap commit");
        if (memoryMap.size() > 0) {
            dbMap.putAll(memoryMap);
        }
        empty = false;
    }

    /*
     * Add k, v pair to the memory map
     */
    @Override
    public V put(K k, V v) {
        //System.out.println("Count " + count);
        if (memoryMap.size() >= Config.CACHE_MAP_SIZE) {
            commitAndClear();
        }
        return memoryMap.put(k, v);
    }

    /*
     * Put all copies into the memory map
     *
     * @param Map<K,V> map = The map of all the values
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        // Store current map
        memoryMap.putAll(map);
    }

    @Override
    public SortedMap<K, V> subMap(K k, K k1) {
        // First find the head key
        // Try the current map
        //System.out.println("Submap looking for  " + k + " " + k1 + " " + maps[currentIndex].firstKey() + " " + maps[currentIndex].lastKey());
        K min = memoryMap.firstKey();
        //K maxNode = maps[currentIndex].lastKey();
        if (min != null) {
            if (k.compareTo(min) < 1) {
                return memoryMap.subMap(k, k1);
            }
        }
        if (memoryMap.containsKey(k)) {
            //System.out.println("Submap contained key " + k);
            return memoryMap.subMap(k, k1);
        }
        // Not in current map, try others

        //min = maps[currentIndex].firstKey();
        K max = dbMap.lastKey();
        //System.out.println("Submap looking for  " + max.getClass() + " " + k.getClass() + " " + k + " " + k1 + " " + maps[i].firstKey() + " " + maps[i].lastKey());
        if (k.compareTo(max) < 1) {
            // Found map with starting node
            if (k1.compareTo(max) < 1) {
                return dbMap.subMap(k, k1);
            } else {
                // Look for map containing other key
                SortedMap<K, V> m = dbMap.tailMap(k);

                max = dbMap.lastKey();
                if (k1.compareTo(max) < 1) {
                    m.putAll(dbMap.headMap(k1));
                    //System.out.println("Submap finally contained key " + k1);
                    return m;
                } else {
                    m.putAll(dbMap);
                }

                return m;

            }
        }
        throw new UnsupportedOperationException("CachedSortedMap subMap did not find keys."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Comparator<? super K> comparator() {
        throw new UnsupportedOperationException("CachedSortedMap comparator not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SortedMap<K, V> headMap(K k) {
        throw new UnsupportedOperationException("CachedSortedMap headMap not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SortedMap<K, V> tailMap(K k) {
        throw new UnsupportedOperationException("CachedSortedMap tailMap not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public K firstKey() {
        throw new UnsupportedOperationException("CachedSortedMap firstKey not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public K lastKey() {
        throw new UnsupportedOperationException("CachedSortedMap lastKey not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("CachedSortedMap keySet not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("CachedSortedMap values not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("CachedSortedMap entrySet not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("CachedSortedMap size not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("CachedSortedMap isEmpty not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsValue(Object o) {
        throw new UnsupportedOperationException("CachedSortedMap containsValue not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V remove(Object o) {
        throw new UnsupportedOperationException("CachedSortedMap remove not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("CachedSortedMap clear not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
