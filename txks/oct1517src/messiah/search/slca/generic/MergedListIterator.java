/*
 * This is the critical class. What we want to do is create one list that we
 * can traverse in sorted order from the merging of iterators of several
 * lists.
 */
package messiah.search.slca.generic;

import java.util.*;
import usu.NodeId;

/**
 *
 * @param <T> - some kind of NodeId
 * @author Curt
 */
public class MergedListIterator implements Iterator<NodeId> {
    PriorityQueue<QueueEntry> heap;
    //SortedMap<T, Iterator<T>> feeds;
    List<Iterator<NodeId>> feedList;
    boolean empty = false;
    NodeId lca;
    int lcaLevel;
    int count;
    
    // The controller advances to the next LCA
    Iterator<NodeId> controller;

    public MergedListIterator(int level) {
        //feeds = new TreeMap();
        heap = new PriorityQueue();
        feedList = new ArrayList();
        lcaLevel = level;
        count = 0;
    }

    public void addFeed(Iterator<NodeId> feed) {
        // Iterator is empty, will not generate anything
        if (empty) {
            return;
        }

        // Check if this feed has something, if not merged iterator is empty
        if (!feed.hasNext()) {
            empty = true;
            return;
        }

        // It is OK, add it
        System.out.println("Adding feed");
        System.out.println("feed " + feed.hasNext());
        //feeds.put(feed.next(), feed);
        if (count == 0) {
            // This is the first feed
            
        }
        NodeId nodeId = feed.next();
        if (lca == null) {
            // This is the first iterator
            lca = nodeId.getAncestor(lcaLevel);
        } else {
            NodeId testLCA = nodeId.getAncestor(lcaLevel);
            
        }
        heap.add(new QueueEntry(nodeId, lca, feed));
    }

    /* 
     * Does this iterator have a next value?
     */
    @Override
    public boolean hasNext() {
        System.out.println("MergedListIterator hasnext ");
        if (empty) {
            return false;
        }
        return true;
    }

    @Override
    public NodeId next() {
        System.out.println("MergedListIterator next");
        if (empty) {
            // Should never get here, asking an empty iterator to produce
            System.err.println("Bad feed in merged iterator");
            return null;
        }

        //T firstKey = feeds.firstKey();
        QueueEntry<NodeId> q = heap.poll();       
        NodeId firstKey = q.elem;
        //Iterator<T> feed = feeds.get(firstKey);
        Iterator<NodeId> feed = q.iter;
        if (feed.hasNext()) {
            NodeId id = feed.next();
            //feeds.put(id, feed);
            q.elem = id;
            heap.add(q);
        } else {
            empty = true;
        }
        //feeds.remove(firstKey);
        return firstKey;
    }

    /* 
     * Remove is not implemented
     */
    @Override
    public void remove() {
    }

    private class QueueEntry<T extends Comparable> implements Comparable {

        public Iterator<T> iter;
        public T elem;
        public T lca;

        public QueueEntry(T elem, T lca, Iterator<T> iter) {
            this.iter = iter;
            this.elem = elem;
            this.lca = lca;
        }

        public int compareTo(Object other) {
            return compareTo((QueueEntry) other);
        }

        public int compareTo(QueueEntry other) {
            return elem.compareTo(other.elem);
        }
    }
}
