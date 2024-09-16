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
public class SortedSplitMap<K extends Comparable, V> implements SortedMap<K, V> {

    Database db;
    int currentIndex;
    SortedMap<K, V>[] maps;
    int maxIndex;
    EntryBinding tupleBinding;
    Class classType;
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
    public SortedSplitMap(int maxNumberOfElements, Database db, String name, EntryBinding tupleBinding, Class classType) {
        this.db = db;
        this.tupleBinding = tupleBinding;
        this.classType = classType;
        this.name = name;
        this.indexName = name + "Index";
        this.maxNumberOfElements = maxNumberOfElements;
        openSplits();
    }

    private void openSplits() {
        if (db.metadataTable.containsKey(indexName)) {
            currentIndex = db.metadataTable.get(indexName);
            maps = new SortedMap[currentIndex + 1];
            maxIndex = currentIndex;
            // Open existing tables
            for (int i = 0; i <= currentIndex; i++) {
                maps[i] = db.openSortedTable(name + i, tupleBinding, classType);
            }
        } else {
            currentIndex = 0;
            //System.out.println("metadata put " + indexName + " " + currentIndex);
            db.metadataTable.put(indexName, currentIndex);
            // Guess at most 100
            maxIndex = 100;
            maps = new SortedMap[maxIndex + 1];
            maps[0] = db.openSortedTable(name + 0, tupleBinding, classType);
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
        V value = maps[currentIndex].get(key);
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
        //db.closeADatabase(name + currentIndex);
        //maps[currentIndex] = null;
        // Allocate a new map and initalize this
        currentIndex++;
        maps[currentIndex] = db.openSortedTable(name + currentIndex, tupleBinding, classType);
        maxIndex++;
        //System.out.println("metadata put " + indexName + " " + currentIndex);
        db.metadataTable.put(indexName, currentIndex);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        // Store current map
        //System.out.println("Putting all " + name + currentIndex);
        if (map.size() > 0) {
            maps[currentIndex].putAll(map);
            //System.out.println("Done putting all " + name + currentIndex);
            db.closeADatabase(name + currentIndex);
            maps[currentIndex] = null;

            // Allocate a new map and initalize this
            currentIndex++;
            maps[currentIndex] = db.openSortedTable(name + currentIndex, tupleBinding, classType);
            maxIndex++;
            //System.out.println("metadata put " + indexName + " " + currentIndex);
            db.metadataTable.put(indexName, currentIndex);
        }
    }

    @Override
    public SortedMap<K, V> subMap(K k, K k1) {
        // First find the head key
        // Try the current map
        //System.out.println("Submap looking for  " + k + " " + k1 + " " + maps[currentIndex].firstKey() + " " + maps[currentIndex].lastKey());
        K min = maps[currentIndex].firstKey();
        //K maxNode = maps[currentIndex].lastKey();
        if (min != null) {
            if (k.compareTo(min) < 1) {
                return maps[currentIndex].subMap(k, k1);
            }
        }
        if (maps[currentIndex].containsKey(k)) {
            //System.out.println("Submap contained key " + k);
            return maps[currentIndex].subMap(k, k1);
        }
        // Not in current map, try others
        for (int i = 0; i < currentIndex; i++) {
            //min = maps[currentIndex].firstKey();
            K max = maps[i].lastKey();
            //System.out.println("Submap looking for  " + max.getClass() + " " + k.getClass() + " " + k + " " + k1 + " " + maps[i].firstKey() + " " + maps[i].lastKey());
            if (k.compareTo(max) < 1) {
                // Found map with starting node
                if (k1.compareTo(max) < 1) {
                    return maps[i].subMap(k, k1);
                } else {
                    // Look for map containing other key
                    SortedMap<K, V> m = maps[i].tailMap(k);
                    for (int j = i + 1; j < currentIndex; j++) {
                        max = maps[j].lastKey();
                        if (k1.compareTo(max) < 1) {
                            m.putAll(maps[j].headMap(k1));
                            //System.out.println("Submap finally contained key " + k1);
                            return m;
                        } else {
                            m.putAll(maps[j]);
                        }
                    }
                    return m;
                }
            }
            
            /*
             System.out.println("Submap looking for  " + k + " " + k1 + " " + maps[i].firstKey() + " " + maps[i].lastKey());

             System.out.println("tailmaps key " + maps[i].tailMap(k).firstKey() + " " + maps[i].tailMap(k1).firstKey());

             if (maps[i].containsKey(k)) {
             if (maps[i].containsKey(k1)) {
             System.out.println("Submap contained key " + k1);
             return maps[i].subMap(k, k1);
             } else {
             // Look for map containing other key
             SortedMap<K, V> m = maps[i].tailMap(k);
             for (int j = i + 1; j <= currentIndex; j++) {
             if (maps[j].containsKey(k1)) {
             m.putAll(maps[j].headMap(k1));
             System.out.println("Submap finally contained key " + k1);
             return m;
             } else {
             m.putAll(maps[j]);
             }
             }
             }
             }
             */
        }
        throw new UnsupportedOperationException("SortedSplitMap subMap did not find keys."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Comparator<? super K> comparator() {
        throw new UnsupportedOperationException("SortedSplitMap comparator not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SortedMap<K, V> headMap(K k) {
        throw new UnsupportedOperationException("SortedSplitMap headMap not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SortedMap<K, V> tailMap(K k) {
        throw new UnsupportedOperationException("SortedSplitMap tailMap not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public K firstKey() {
        throw new UnsupportedOperationException("SortedSplitMap firstKey not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public K lastKey() {
        throw new UnsupportedOperationException("SortedSplitMap lastKey not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("SortedSplitMap keySet not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("SortedSplitMap values not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("SortedSplitMap entrySet not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("SortedSplitMap size not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("SortedSplitMap isEmpty not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsValue(Object o) {
        throw new UnsupportedOperationException("SortedSplitMap containsValue not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V remove(Object o) {
        throw new UnsupportedOperationException("SortedSplitMap remove not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("SortedSplitMap clear not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
