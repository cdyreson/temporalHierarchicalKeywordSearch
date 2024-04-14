/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messiah.database.mapdb;

import java.util.Collection;
import org.mapdb.HTreeMap;
import java.util.Map;
import java.util.Set;
import messiah.Config;
import org.mapdb.DB;
import org.mapdb.Serializer;

/**
 *
 * @author curt
 * @param <K>
 * @param <V>
 */
public class DBMap<K, V> implements Map<K, V> {

    DB db;
    int currentIndex;
    Map<K, V> map;
    Serializer keySerializer;
    Serializer valueSerializer;
    String name;
    int count = 0;

    /**
     * The map needs a Database (or actually just the metadataTable)
     *
     * @param db
     * @param name
     * @param tupleBinding
     * @param classType
     */
    public DBMap(
            DB db, 
            String name, 
            Serializer keySerializer, Serializer valueSerializer) {
        this.db = db;
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
        this.name = name;
        //HTreeMap h; 
        this.map = db
                .hashMap(name)
                .keySerializer(keySerializer)
                .valueSerializer(valueSerializer)
                .createOrOpen();
    }



    /*
     * Commit this map by writing any values in memory to disk
     */
    public void commit() {

    }


    /**
     * Get
     *
     * @param o = The key
     */
    @Override
    public V get(Object o) {
        K key = (K) o;

        return this.map.get(key);
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

        return this.map.containsKey(key);
    }

    /*
     * Add k, v pair to the current map
     */
    @Override
    public V put(K k, V v) {
        return this.map.put(k, v);
    }

    
    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        this.map.putAll(map);
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
        return this.map.size();
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
