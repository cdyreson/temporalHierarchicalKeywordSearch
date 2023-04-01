package messiah.search.slca.generic.feed;

import java.util.Iterator;
import usu.NodeId;

/**
 * Helper class that pairs a NodeId with an Iterator and other info. 
 * The Iterator is the feed for with the NodeId.
 *
 * @author Curtis Dyreson
 */
public class CachedNodeInfo implements Comparable {

    public Iterator<NodeId> iter;
    public NodeId nodeId;
    public short id;  // Count of this "type"
    public short dimension;  // Ordinal in list of keywords

    public CachedNodeInfo(NodeId nodeId, Iterator<NodeId> iter, short id, short dimension) {
        this.iter = iter;
        this.nodeId = nodeId;
        this.id = id;
        this.dimension = dimension;
    }

    @Override
    public int compareTo(Object other) {
        return compareTo((CachedNodeInfo) other);
    }

    /* 
    * Order by NodeId
    */
    public int compareTo(CachedNodeInfo other) {
        return nodeId.compareTo(other.nodeId);
    }

}
