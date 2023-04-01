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
import messiah.Config;

/**
 *
 * @author curt
 * @param <K>
 * @param <V>
 */
public class CachedMap<K, V> implements Map<K, V> {

    Map<K, V> memoryMap;
    Map<K, V> dbMap;
    boolean empty;

    /**
     * The map needs a Database (or actually just the metadataTable)
     *
     * @param dbMap - BerkeleyDB table to serve as disk-resident table
     */
    public CachedMap(Map<K, V> dbMap) {
        this.dbMap = dbMap;
        this.memoryMap = new HashMap();
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

        if (empty) return null;
        //System.out.println("here " );
//if (false) {  // Doesn't work to avoid lookup in previously cached values
            // Check the disk map
            value = dbMap.get(key);
            if (value != null) {
                return value;
            }
//}
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
        V value; 
        if (memoryMap.containsKey(key)) {
            return true;
        }

        if (empty) return false;
        
 //if (false) {  // Doesn't work to avoid lookup in previously cached values
        // Check the db map
        value = dbMap.get(key);
        if (value != null) {
            return true;
        }
 //}
        // Not found
        return false;
    }

    /*
     * Commit this map by writing any values in memory to disk,
     * also clear the memory map
     */
    public void commitAndClear() {
        //System.out.println("CachedMap commit and clear");
        if (memoryMap.size() > 0) {
            commit();
            memoryMap = new HashMap();
        }
    }

    /*
     * Commit this map by writing any values in memory to disk
     */
    public void commit() {
        //System.out.println("CachedMap commit");
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
    public Set<K> keySet() {
        throw new UnsupportedOperationException("SplitMap keySet not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("SplitMap values not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("SplitMap entrySet not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("SplitMap size not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("SplitMap isEmpty not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsValue(Object o) {
        throw new UnsupportedOperationException("SplitMap containsValue not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V remove(Object o) {
        throw new UnsupportedOperationException("SplitMap remove not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("SplitMap clear not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
