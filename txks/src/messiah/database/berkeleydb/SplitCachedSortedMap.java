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
import java.util.TreeMap;
import messiah.Config;

/**
 *
 * @author curt
 * @param <K>
 * @param <V>
 */
public class SplitCachedSortedMap<K extends Comparable, V> implements SortedMap<K, V> {

    Database db;
    int currentIndex;
    SortedMap<K, V>[] maps;
    SortedMap<K, V> memoryMap;
    EntryBinding tupleBinding;
    Class classType;
    String name;
    String indexName;
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
    public SplitCachedSortedMap(int maxNumberOfElements, boolean isReadOnly, Database db, String name, EntryBinding tupleBinding, Class classType) {
        this.db = db;
        this.tupleBinding = tupleBinding;
        this.classType = classType;
        this.name = name;
        this.indexName = name + "Index";
        this.isReadOnly = isReadOnly;
        this.maxNumberOfElements = maxNumberOfElements;
        openSplits();
    }
 
    private void openSplits() {
        if (isReadOnly || db.metadataTable.containsKey(indexName)) {
            // System.out.println("Opening many split splitSortedCachedMap " + indexName);
            currentIndex = db.metadataTable.get(indexName);
            maps = new SortedMap[currentIndex + 1];
            // Open existing tables
            for (int i = 0; i <= currentIndex; i++) {
                maps[i] = db.openSortedTable(name + i, tupleBinding, classType);
            }
        } else {
            //System.out.println("Opening single split splitSortedCachedMap " + name);
            currentIndex = 0;
            //System.out.println("metadata put " + indexName + " " + currentIndex);
            db.metadataTable.put(indexName, currentIndex);
            maps = new SortedMap[Config.MAX_SPLITS];
            maps[0] = db.openSortedTable(name + 0, tupleBinding, classType);
            memoryMap = new TreeMap();
        }
    }
    
    /*
     * Commit this map by writing any values in memory to disk,
     * also clear the memory map
     */
    public void commitAndClear() {
        //System.out.println("SplitCachedSortedMap commit and clear "  + name);
        if (memoryMap.size() > 0) {
            commit();
            memoryMap = new TreeMap();
        }
    }

    /*
     * Commit this map by writing any values in memory to disk
     */
    public void commit() {
        //System.out.println("SplitCachedSortedMap commit " + name);
        if (memoryMap.size() > 0) {
            maps[currentIndex].putAll(memoryMap);
        }
        //empty = false;
    }
    
    /*
     * Called only when closing the split tables.
     */
    public void mergeTable(SortedMap<K, V> map) {
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
     * split into multiple split tables, this method should be overwritten in 
     * a subclass.
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
     * ContainsKey looks through all of the tables before returning null (not found).  
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
            if (maps[i].containsKey(key)) return true;
        }

        return false;
    }

    /*
     * Add k, v pair to the current memory map, may force a new split
     */
    @Override
    public V put(K k, V v) {
        //System.out.println("putting " + memoryMap.size());
        if (memoryMap.size() >= maxNumberOfElements) {
            addNewSplit();
        }
        return memoryMap.put(k, v);
    }

    /* Create a new split table */
    private void addNewSplit() {
        // Store current map
        System.out.println("Adding new split sorted");
        maps[currentIndex].putAll(memoryMap);
        db.closeADatabase(name + currentIndex);
        maps[currentIndex] = null;
        // Allocate a new map and initalize this
        currentIndex++;
        maps[currentIndex] = db.openSortedTable(name + currentIndex, tupleBinding, classType);
        //System.out.println("metadata put " + indexName + " " + currentIndex);
        db.metadataTable.put(indexName, currentIndex);
        memoryMap = new TreeMap();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        memoryMap.putAll(map);
    }
    
    public void putAll(SortedMap<? extends K, ? extends V> map) {
        memoryMap.putAll(map);
    }

    @Override
    public SortedMap<K, V> subMap(K k, K k1) {
        // First find the head key
        // Try the current map
        //System.out.println("Submap looking for  " + k + " " + k1 + " " + maps[currentIndex].firstKey() + " " + maps[currentIndex].lastKey());
        int minCount = currentIndex;
        while (minCount > 0) {
            K min = maps[minCount].firstKey();
            //K max = maps[count].lastKey();
            //System.out.println(" k is " + k + " " + min + " " + k.compareTo(min));
            if (k.compareTo(min) >= 0) {
                break;
                //return maps[minCount].subMap(k, k1);
            }
            minCount--;
        }
        
        int maxCount = currentIndex;
        while (maxCount > 0) {
            K max = maps[minCount].lastKey();
            //System.out.println(" maxcount " + maxCount + " " + k1.compareTo(max) + " " + max);
            //K max = maps[count].lastKey();
            if (k1.compareTo(max) >= 0) {
                break;
                //return maps[minCount].subMap(k, k1);
            }
            maxCount--;
        }
        
        if (minCount == maxCount) {
            return maps[minCount].subMap(k, k1);
        } else {
            // Look for map containing other key
            //System.out.println("here asdf " + minCount + " " + k + " " + maxCount);
            SortedMap<K, V> m = maps[minCount].tailMap(k);
            for (int j = minCount + 1; j <= maxCount; j++) {
                if (j == maxCount) {
                    m.putAll(maps[j].headMap(k1));
                    //System.out.println("Submap finally contained key " + k1);
                    return m;
                } else {
                    m.putAll(maps[j]);
                }
            }
            return m;
        }
            
        //throw new UnsupportedOperationException("SortedSplitMap subMap did not find keys."); //To change body of generated methods, choose Tools | Templates.
    }

        public SortedMap<K, V> subMapOld(K k, K k1) {
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
        } else {
            //System.out.println("Submap did not contain key " + k + " hashcode " + k.hashCode());
            for (K key: maps[currentIndex].keySet()) {
                System.out.println(" key is " + key + " hash " + key.hashCode());
            }
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
                            // System.out.println("Submap finally contained key " + k1);
                            return m;
                        } else {
                            m.putAll(maps[j]);
                        }
                    }
                    return m;
                }
            }
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
