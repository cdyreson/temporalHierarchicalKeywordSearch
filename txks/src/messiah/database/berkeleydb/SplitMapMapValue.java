/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messiah.database.berkeleydb;

import com.sleepycat.bind.EntryBinding;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import usu.NodeId;
import usu.PathId;

/**
 *
 * @author curt
 * @param <K>
 * @param <V> is a Set class type
 */
public class SplitMapMapValue<K, V> extends SplitMap<K, V> implements Map<K, V> {

    /**
     * The map needs a Database (or actually just the metadataTable)
     *
     * @param db
     * @param name
     * @param tupleBinding
     * @param classType
     */
    public SplitMapMapValue(int maxNumberOfElements, Database db, String name, Class keyClass, Class valueClass) {
        super(maxNumberOfElements, db, name, keyClass, valueClass);
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
        Map m = null;
        for (int i = 0; i <= currentIndex; i++) {
            //System.out.println("Getting map value");
            V value = maps[i].get(key);
            if (value != null) {
                if (m == null) {
                    m = (Map) value;
                                        //System.out.println("first " + m.keySet());
                } else {
                    
                    m.putAll((Map) value);
                    //Map m1 = (Map) value;
                    //for (Object q : m1.keySet()) {
                        //PathId p = (PathId)q;
                    //    List<NodeId> a = (List<NodeId>)(m1.get(q));
                        
                        
                    //}
                    System.out.println("putall " + m.keySet());
                }
            }
        }
        return (V) m;
    }

}
