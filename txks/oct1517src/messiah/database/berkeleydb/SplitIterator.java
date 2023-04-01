/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messiah.database.berkeleydb;

import java.util.Iterator;
import usu.NodeId;

/**
 *
 * @author Curtis Dyreson
 */
public class SplitIterator implements Iterator<NodeId> {
    Iterator<NodeId>[] iters;
    int current = 0;
    int maxIndex = 0;
    Iterator<NodeId> currentIter;
    boolean empty = true;

    public SplitIterator(int index) {
        iters = new Iterator[index];
        current = 0;
        maxIndex = index;
    }

    public void add(Iterator<NodeId> iter) {
        iters[current++] = iter;
        if (empty) {
            currentIter = iters[0];
            empty = false;
        }
    }

    @Override
    public boolean hasNext() {
        if (empty) return false;
        if (iters[current].hasNext()) {
            return true;
        } else {
            current++;
            if (current > maxIndex) {
                empty = true;
            }
            return hasNext();
        }
    }

    @Override
    public NodeId next() {
        if (empty) return null;
        return iters[current].next();
    }

}
