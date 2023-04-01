package messiah.search.slca.generic.feed;

import usu.NodeId;

/**
 * Helper class that pairs a NodeId with other info. 
 *
 * @author Curtis Dyreson
 */
public class CachedNodeTriple implements Comparable {
    public NodeId nodeId;
    public short id;  // The id is an identifier to distinguish this from others?
    public short dimension;  // The dimension is the keyword ordinal

    public CachedNodeTriple(NodeId nodeId, short dimension, short id) {
        this.nodeId = nodeId;
        this.id = id;
        this.dimension = dimension;
    }
    
    @Override
    public int compareTo(Object other) {
        return compareTo((CachedNodeTriple) other);
    }

    /* 
    * Order by NodeId
    */
    public int compareTo(CachedNodeTriple other) {
        //System.out.println("CompareTo: " + lca.compareTo(other.lca) + " " + lca + " " + other.lca);
        return nodeId.compareTo(other.nodeId);
    }

}
