package messiah.database;

import java.util.*;

/**
 * The MapWithDuplicates class extends a Map with duplicate key
 * values. It does this by using a Map within a Map.
 * @author Curtis
 */
public interface MapWithDuplicates<K, V> extends Map<K, V> {

    /* Iterate over the values associated with this key */
    public Iterator iterator(K key);
    
    /* Remove this key value pair */
    //public void remove(K key, V value);

    /* Remove this key value pair */
    public V putIfAbsent(K key, V value);

}
