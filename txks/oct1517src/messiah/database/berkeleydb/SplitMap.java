/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messiah.database.berkeleydb;

import com.sleepycat.bind.EntryBinding;
import java.util.Collection;
import java.util.Comparator;
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
public class SplitMap<K, V> implements Map<K, V> {

    Database db;
    int currentIndex;
    Map<K, V>[] maps;
    int maxIndex;
    Class keyClass;
    Class valueClass;
    String name;
    String indexName;
    int count = 0;
    int maxNumberOfElements;

    /**
     * The map needs a Database (or actually just the metadataTable)
     *
     * @param db
     * @param name
     * @param tupleBinding
     * @param classType
     */
    public SplitMap(int maxNumberOfElements, Database db, String name, Class keyClass, Class valueClass) {
        this.db = db;
        this.keyClass = keyClass;
        this.valueClass = valueClass;
        this.name = name;
        this.indexName = name + "Index";
        this.maxNumberOfElements = maxNumberOfElements;
        openSplits();
    }

    public void openSplits() {
        if (db.metadataTable.containsKey(indexName)) {
            currentIndex = db.metadataTable.get(indexName);
            maps = new Map[currentIndex + 1];
            maxIndex = currentIndex;
            // Open existing tables
            for (int i = 0; i <= currentIndex; i++) {
                maps[i] = db.openTable(name + i, keyClass, valueClass);
            }
        } else {
            currentIndex = 0;
            //System.out.println("metadata put " + indexName + " " + currentIndex);
            db.metadataTable.put(indexName, currentIndex);
            // Guess at most 100
            maxIndex = 100;
            maps = new Map[maxIndex + 1];
            maps[0] = db.openTable(name + 0, keyClass, valueClass);
        }
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
     * Get looks through all of the tables before returning null (not found)
     *
     * @param o = The key
     */
    @Override
    public V get(Object o) {
        K key = (K) o;
        V value;

        value = maps[currentIndex].get(key);
        if (value != null) {
            return value;
        }

        for (int i = 0; i < currentIndex; i++) {
            value = maps[i].get(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object o) {
        K key = (K) o;
        V value = maps[currentIndex].get(key);
        if (value != null) {
            return true;
        }

        for (int i = 0; i < currentIndex; i++) {
            value = maps[i].get(key);
            if (value != null) {
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
        if (count++ >= maxNumberOfElements) {
            addNewSplit();
            count = 0;
        }
        return maps[currentIndex].put(k, v);
    }

    /* Create a new split table */
    private void addNewSplit() {
        // Store current map
        //db.closeADatabase(name + currentIndex); /* do not store current map */
        //maps[currentIndex] = null;
        // Allocate a new map and initalize this
        currentIndex++;
        maps[currentIndex] = db.openTable(name + currentIndex, keyClass, valueClass);
        maxIndex++;
        //System.out.println("metadata put " + indexName + " " + currentIndex);
        db.metadataTable.put(indexName, currentIndex);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        // Store current map
        if (map.size() > 0) {
            maps[currentIndex].putAll(map);
            //db.closeADatabase(name + currentIndex);
            maps[currentIndex] = null;
            // Allocate a new map and initalize this
            currentIndex++;
            maps[currentIndex] = db.openTable(name + currentIndex, keyClass, valueClass);
            maxIndex++;
            //System.out.println("metadata put " + indexName + " " + currentIndex);
            db.metadataTable.put(indexName, currentIndex);
        }
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
