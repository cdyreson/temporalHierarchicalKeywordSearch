package usu;

import java.io.Serializable;

/**
 *
 * @author truongbaquan, modified Curtis
 */
public class NodePathId implements Serializable, Comparable<NodePathId> {   
    public NodeId nodeId;  
    public PathId pathId;

    public NodePathId(NodeId nodeId, PathId pathId) {
        this.nodeId = nodeId;
        this.pathId = pathId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NodePathId other = (NodePathId) obj;
        if (!this.pathId.equals(other.pathId)) return false;
        return !this.nodeId.equals(other.nodeId);
    }

    @Override
    public int hashCode() {
        return nodeId.hashCode() + pathId.hashCode();
    }

    public int compareTo(NodePathId o) {
//        if (this.nodeId < o.nodeId) return -1;
//        else if (this.nodeId > o.nodeId) return 1;
//        else return 0;
        int i = this.pathId.compareTo(o.pathId);
        if (i != 0) return i;
        return this.nodeId.compareTo(o.nodeId);
    }
    
}
