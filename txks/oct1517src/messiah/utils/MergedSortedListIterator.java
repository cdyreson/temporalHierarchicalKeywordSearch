package messiah.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;

/**
 *
 * @author truongbaquan
 */
public class MergedSortedListIterator<T extends Comparable<T>> implements Iterator<T> {

    private Iterator<T> iter;

    public MergedSortedListIterator(SortedSet<T>[] inputSets) {        
        // sort the input sets by their first element
        TreeMap<T,SortedSet<T>> map = new TreeMap();
        for (SortedSet<T> sortedSet : inputSets) {
            if (!sortedSet.isEmpty())
                map.put(sortedSet.first(), sortedSet);
        }
        Collection<SortedSet<T>> firstSortedSet = map.values();

        boolean parallel = checkParallel(firstSortedSet);
        
        if (parallel)
            iter = new ParallelIterator(firstSortedSet);
        else
            iter = new SequentialIterator(firstSortedSet);
    }
    
    /**
     * Checks whether the list of sorted sets are sequential or parallel. Sequential means the 
     * last element of the previous set is always smaller than the first element of the next set.
     * @param sets  The list of sorted sets. They are assumed to be sorted by their first element.
     * @return      True if parallel; false if sequential.
     */
    private boolean checkParallel(Collection<SortedSet<T>> sets) {
        SortedSet<T> prev = null;
        for (SortedSet<T> cur : sets) {
            if (prev != null && prev.last().compareTo(cur.first()) > 0)
                return true; // parallel
            prev = cur;
        }
        return false; // sequential
    }
    
    public boolean hasNext() {
        return iter.hasNext();
    }

    public T next() {
        return iter.next();
    }

    public void remove() {
        iter.remove();
    }
    
    private class SequentialIterator<T> implements Iterator<T> {
        
        private Iterator<SortedSet<T>> iter;
        private Iterator<T> subIter;

        /**
         * 
         * @param sets  Assumed to be sorted by first element and non-empty
         */
        public SequentialIterator(Collection<SortedSet<T>> sets) {
            this.iter = sets.iterator();
            SortedSet<T> set = iter.next();
            if (set != null)
                this.subIter = iter.next().iterator();
        }

        public boolean hasNext() {
            return subIter != null && (subIter.hasNext() || iter.hasNext());
        }

        public T next() {
            if (subIter == null) return null;
            else if (subIter.hasNext()) return subIter.next();
            else {
                SortedSet<T> set = iter.next();
                if (set != null)
                    this.subIter = iter.next().iterator();
                return subIter.next();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }       
    }
    
    private class ParallelIterator<T> implements Iterator<T> {

        private TreeMap<T, Iterator<T>> setOfFirst;
        
        /**
         * 
         * @param sets  Assumed to be sorted by first element and non-empty
         */
        public ParallelIterator(Collection<SortedSet<T>> sets) {
            this.setOfFirst = new TreeMap<T, Iterator<T>>();
            for (SortedSet<T> sortedSet : sets) {
                Iterator<T> iter = sortedSet.iterator();
                T first = iter.next();
                if (first != null)
                    this.setOfFirst.put(first, iter);
            }
        }

        public boolean hasNext() {
            return !this.setOfFirst.isEmpty();
        }

        public T next() {
            Entry<T,Iterator<T>> entry = this.setOfFirst.pollFirstEntry();
            Iterator<T> iter = entry.getValue();
            T elem = iter.next();
            if (elem != null)
                this.setOfFirst.put(elem, iter);
            return entry.getKey();
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
}
