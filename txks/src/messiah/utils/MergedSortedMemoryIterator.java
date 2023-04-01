package messiah.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Iterate a sorted list of elements using MemoryIterator to know the last and next element.
 * The elements are iterated based on the global order of the elements.
 * @author truongbaquan
 */
public class MergedSortedMemoryIterator<T extends Comparable<T>> implements Iterator<T> {
    private final MemoryIterator<T>[] iters;
    private PriorityQueue<Entry> setOfFirst;
    private int lastIndex = -1;
    private T[] lastArray;
    private T[] nextArray;
    
    /**
     * Construct the iterator. The input lists are assumed to be sorted.
     * @param lists 
     */
    public MergedSortedMemoryIterator(Collection<T>[] lists) {
        this.iters = new MemoryIterator[lists.length];
        this.lastArray = (T[])Array.newInstance(Comparable.class, lists.length);
        this.nextArray = (T[])Array.newInstance(Comparable.class, lists.length);
        this.setOfFirst = new PriorityQueue<>(lists.length);
        for (int i = 0; i < lists.length; i++) {
            iters[i] = new MemoryIterator(lists[i]);
            if (iters[i].hasNext()) {
                T elem = iters[i].next();
                if (elem != null) {
                    this.setOfFirst.add(new Entry(elem, i));
                    this.nextArray[i] = elem;
                }
            }
        }
        
    }

    @Override
    public boolean hasNext() {
        return !this.setOfFirst.isEmpty();
    }

    @Override
    public T next() {
        Entry entry = this.setOfFirst.poll();
        this.lastIndex = entry.index;
        this.lastArray[lastIndex] = entry.elem;
        if (iters[lastIndex].hasNext()) {
            T elem = iters[lastIndex].next();
            if (elem != null) {
                this.setOfFirst.add(new Entry(elem, lastIndex));
            }
            this.nextArray[lastIndex] = elem;
        } else {
            this.nextArray[lastIndex] = null;
        }
        return entry.elem;
    }
    
    /**
     * Advance the iterator to at least the given target.
     * This method is useful to advance the iterator over a long range of values.
     * Notice that after advance, {@link MergedSortedMemoryIterator#getLastIndex() } is useless
     * @param target 
     */
    public void advance(T target) {
        this.setOfFirst.clear();
        for (int i = 0; i < iters.length; i++) {
            while (this.nextArray[i] != null && this.nextArray[i].compareTo(target) < 0) {
                this.nextArray[i] = iters[i].next();
                
            }
            if (this.nextArray[i] != null) {
                this.setOfFirst.add(new Entry(this.nextArray[i], i));
                this.lastArray[i] = iters[i].peekLast();
            }
        }
        lastIndex = -1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getLastIndex() {
        return lastIndex;
    }
    
    public T peekLast(int index) {
        return lastArray[index];
    }
    
    public T peekNext(int index) {
        return nextArray[index];
    }

    private class Entry implements Comparable<Entry> {
        T elem;
        int index;

        public Entry(T elem, int index) {
            this.elem = elem;
            this.index = index;
        }

        @Override
        public int compareTo(Entry o) {
            //System.out.println("Entry " + elem.getClass());
            return elem.compareTo(o.elem);
        }


    }
}
