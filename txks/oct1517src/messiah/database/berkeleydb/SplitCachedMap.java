/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messiah.database.berkeleydb;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import messiah.Config;

/**
 *
 * @author curt
 * @param <K>
 * @param <V>
 */
public class SplitCachedMap<K, V> implements Map<K, V> {

    Database db;
    int currentIndex;
    Map<K, V>[] maps;
    Map<K, V> memoryMap;
    Class keyClass;
    Class valueClass;
    String name;
    String indexName;
    int count = 0;
    boolean isReadOnly;
    int maxNumberOfElements;

    /**
     * The map needs a Database (or actually just the metadataTable)
     *
     * @param db
     * @param name
     * @param tupleBinding
     * @param classType
     */
    public SplitCachedMap(int maxNumberOfElements, boolean isReadOnly, Database db, String name, Class keyClass, Class valueClass) {
        this.db = db;
        this.keyClass = keyClass;
        this.valueClass = valueClass;
        this.name = name;
        this.indexName = name + "Index";
        this.isReadOnly = isReadOnly;
        this.maxNumberOfElements = maxNumberOfElements;
        openSplits();
    }

    public void openSplits() {
        if (db.metadataTable.containsKey(indexName) || isReadOnly) {
            //System.out.println("Opening previous splits splitCachedMap " + db.metadataTable + " " + indexName + " " + db.metadataTable.keySet());
            currentIndex = db.metadataTable.get(indexName);
            maps = new Map[currentIndex + 1];
            // Open existing tables
            for (int i = 0; i <= currentIndex; i++) {
                maps[i] = db.openTable(name + i, keyClass, valueClass);
            }
        } else {
            //System.out.println("Opening single split splitCachedMap");
            currentIndex = 0;
            db.metadataTable.put(indexName, currentIndex);
            maps = new Map[Config.MAX_SPLITS];
            maps[0] = db.openTable(name + 0, keyClass, valueClass);
            memoryMap = new HashMap();
        }
    }

    /*
     * Commit this map by writing any values in memory to disk,
     * also clear the memory map
     */
    public void commitAndClear() {
        //System.out.println("CachedMap commit and clear " + name);
        if (memoryMap.size() > 0) {
            commit();
            memoryMap = new HashMap();
        }
    }

    /*
     * Commit this map by writing any values in memory to disk
     */
    public void commit() {
        //System.out.println("SplitCachedMap commit " + name);
        if (memoryMap.size() > 0) {
            maps[currentIndex].putAll(memoryMap);
        }
        //empty = false;
    }

    /*
     * Called only when closing the split tables.
     */
    public void mergeTable(Map<K, V> map) {
        System.out.println("mergeTable " + currentIndex);
        // First close the current table
        db.closeADatabase(indexName + currentIndex);
        // Next reopen the tables
        openSplits();
        // Iterate through splits
        for (int i = 0; i <= currentIndex; i++) {
            System.out.println("Merging " + maps[i].size() + " i " + i);
            map.putAll(maps[i]);
        }
    }

    /**
     * Get looks through all of the tables before returning null (not found).
     * Important: it only returns the first value found, so if there are keys
     * split into multiple split tables, this method should be overwritten in a
     * subclass.
     *
     * @param o = The key
     */
    @Override
    public V get(Object o) {
        K key = (K) o;
        V value;

        if (!isReadOnly) {
            value = memoryMap.get(key);
            return value;
        }

        for (int i = 0; i <= currentIndex; i++) {
            value = maps[i].get(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * ContainsKey looks through all of the tables before returning null (not
     * found).
     *
     * @param o = The key
     */
    @Override
    public boolean containsKey(Object o) {
        K key = (K) o;

        // Check if it is in the memory map
        if (!isReadOnly) {
            return memoryMap.containsKey(key);
        }

        // Check the disk-resident maps
        for (int i = 0; i <= currentIndex; i++) {
            if (maps[i].containsKey(key)) {
                return true;
            }
        }

        return false;
    }

    /*
     * Add k, v pair to the current map
     */
    @Override
    public V put(K k, V v) {
        //System.out.println("put " + name + memoryMap.size());
        if (memoryMap.size() >= maxNumberOfElements) {
            addNewSplit();
        }
        return memoryMap.put(k, v);
    }

    /* Create a new split table */
    private void addNewSplit() {
        // Store current map
        //System.out.println("Adding new split");
        maps[currentIndex].putAll(memoryMap);
        db.closeADatabase(name + currentIndex); /* do not store current map */

        maps[currentIndex] = null;
        // Allocate a new map and initalize this
        currentIndex++;
        maps[currentIndex] = db.openTable(name + currentIndex, keyClass, valueClass);
        //System.out.println("metadata put " + indexName + " " + currentIndex);
        db.metadataTable.put(indexName, currentIndex);
        memoryMap = new HashMap();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
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
        return memoryMap.size();
        //throw new UnsupportedOperationException("SplitMap size not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
