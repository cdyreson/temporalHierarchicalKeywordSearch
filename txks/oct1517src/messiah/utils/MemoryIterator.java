package messiah.utils;

import java.util.Collection;
import java.util.Iterator;

/**
 * An iterator that can memorize the last element and the next element
 * @author truongbaquan
 */
public class MemoryIterator<T> implements Iterator<T> {
    
    private final Iterator<T> subIter;
    private T last = null;
    private T next = null;
    
    public MemoryIterator(Collection<T> collection) {
        subIter = collection.iterator();
        if (subIter.hasNext()) next = subIter.next();
    }

    @Override
    public boolean hasNext() {
        return !(next == null);
    }

    @Override
    public T next() {
        last = next;
        next = subIter.hasNext() ? subIter.next() : null;
        return last;
    }
    
    public T peekNext() {
        return next;
    }
    
    public T peekLast() {
        return last;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
