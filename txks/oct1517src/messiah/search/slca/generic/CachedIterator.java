/*
 * An CachedIterator is like an Iterator but extends it with special methods.
 * The CachedIterator caches values in a List and can reset the iteration to
 * the start of the list, or release the cached nodes.
 */
package messiah.search.slca.generic;

import java.util.*;
import usu.NodeId;

/**
 *
 * @author Curt
 */
public class CachedIterator implements Iterator<NodeId> {

    Iterator<NodeId> iter;
    NodeId nextLCA;
    NodeId lca;
    int level;
    List<NodeId> idList;
    boolean empty;

    public CachedIterator(Iterator<NodeId> iter) {
        this.iter = iter;
        nextLCA = null;
        idList = new ArrayList();
        empty = false;
    }

    public void advance(NodeId p) {
        //System.out.println("CACHEDITERATOR setParent start " + this + " " + p);
        if (empty) {
            return;
        }
        if (lca == null) {
            if (iter.hasNext()) {
                nextLCA = null;
                lca = iter.next().getAncestor(level);
            } else {
                empty = true;
                return;
            }
        } else {
            while (iter.hasNext()) {
                NodeId nodeId = iter.next();
                nextLCA = nodeId.getAncestor(level);
                if (!nextLCA.equals(lca)) {
                    return;
                }
                idList.add(nodeId);
            }
            if (!iter.hasNext()) {
                empty = true;
                return;
            }
        }

    }


    /*
     * Check to see if the cached list contains values, or the underlying feed
     * still needs values.
     */
    @Override
    public boolean hasNext() {
        return iter.hasNext();
    }

    public void reset() {
        //System.out.println("CachedIterator reset ");
    }

    @Override
    public NodeId next() {
        return iter.next();

    }

    @Override
    public void remove() {
    }
}
