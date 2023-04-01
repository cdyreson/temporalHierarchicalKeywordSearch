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

/**
 *
 * @author curt
 * @param <K>
 * @param <V> is a Set class type
 */
public class SplitCachedMapSetValue<K, V> extends SplitCachedMap<K, V> implements Map<K, V> {

    /**
     * The map needs a Database (or actually just the metadataTable)
     *
     * @param db
     * @param name
     * @param tupleBinding
     * @param classType
     */
    public SplitCachedMapSetValue(int maxNumberOfElements, boolean isReadOnly, Database db, String name, Class keyClass, Class valueClass) {
        super(maxNumberOfElements, isReadOnly, db, name, keyClass, valueClass);
    }

    /**
     * Get looks through all of the tables before returning null (not found)
     *
     * @param o = The key
     * @return A set of values
     */
    @Override
    public V get(Object o) {
        K key = (K) o;
        Set s = null;
        if (!isReadOnly) {
            V value = memoryMap.get(key);
            return value;
        }
        //System.out.println("Got here error!");
        for (int i = 0; i <= currentIndex; i++) {
            V value = maps[i].get(key);
            if (value != null) {
                if (s == null) {
                    s = (Set) value;
                } else {
                    s.addAll((Set) value);
                }
            }
        }
        return (V) s;
    }

}
