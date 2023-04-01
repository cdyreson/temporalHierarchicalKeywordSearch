/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messiah.database.berkeleydb;

import com.sleepycat.bind.EntryBinding;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import messiah.search.slca.generic.feed.CachingFeedGroup;
import usu.NodeId;
import usu.PathId;

/**
 *
 * @author curt
 */
//public class SplitMapKeywordNodeIndex<String, Map<PathId,List<NodeId>>> extends SplitMap<String, Map<PathId,List<NodeId>>> implements Map<String, Map<PathId,List<NodeId>>> {
public class SplitCachedMapKeywordNodeIndex extends SplitCachedMap<String, Map<PathId, List<NodeId>>> implements Map<String, Map<PathId, List<NodeId>>> {

    /**
     * The map needs a Database (or actually just the metadataTable)
     *
     * @param db
     * @param name
     * @param tupleBinding
     * @param classType
     */
    public SplitCachedMapKeywordNodeIndex(int maxNumberOfElements, boolean isReadOnly, Database db, String name, Class keyClass, Class valueClass) {
        super(maxNumberOfElements, isReadOnly, db, name, keyClass, valueClass);
    }

    /**
     * Get looks through all of the tables before returning null (not found)
     *
     * @param o = The key
     * @return A set of values
     */
    @Override
    public Map<PathId, List<NodeId>> get(Object o) {
        String key = (String) o;
        Map<PathId, List<NodeId>> m = null;
        if (!isReadOnly) {
            m = memoryMap.get(key);
            return m;
        }
        //System.out.println("Got here error!");
        for (int i = 0; i <= currentIndex; i++) {
            //System.out.println("Getting map value");
            Map<PathId, List<NodeId>> value = maps[i].get(key);
            if (value != null) {
                if (m == null) {
                    m = (Map) value;
                    //System.out.println("first " + m.keySet());
                } else {

                    //m.putAll((Map) value);
                    Map<PathId, List<NodeId>> m1 = value;
                    for (PathId p : m1.keySet()) {
                        List<NodeId> a = m1.get(p);
                        if (m.containsKey(p)) {
                            m.get(p).addAll(a);
                        } else {
                            m.put(p, a);
                        }
                    }
                    //System.out.println("putall " + m.keySet());
                }
            }
        }
        return m;
    }

    public void addIters(CachingFeedGroup group, short dimension, String key) {
        Map<PathId, List<NodeId>> m = null;
        //SplitIterator iters = new SplitIterator(currentIndex);
        short count = 0;
        for (int i = 0; i <= currentIndex; i++) {
            //System.out.println("Getting map value");
            Map<PathId, List<NodeId>> value = maps[i].get(key);
            if (value != null) {
                for (PathId p : value.keySet()) {
                    Iterator<NodeId> iter = value.get(p).iterator();
                    System.out.println("Adding " + key + " " + p + " " + iter.hasNext());
                    group.addFeed(dimension, count++, iter, p, key);
                }
            }
        }
    }

    public Iterator<NodeId> getIters(String key, PathId pathId) {
        Map<PathId, List<NodeId>> m = null;
        SplitIterator iters = new SplitIterator(currentIndex);
        for (int i = 0; i <= currentIndex; i++) {
            //System.out.println("Getting map value");
            Map<PathId, List<NodeId>> value = maps[i].get(key);
            if (value != null) {
                if (value.containsKey(pathId)) {
                    iters.add(value.get(pathId).iterator());
                }
            }
        }
        return iters;
    }

}
