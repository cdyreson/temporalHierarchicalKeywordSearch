package usu;

import messiah.NodeInfo;

/**
 * Encapsulates a data node
 * @author John Truong Ba Quan
 */
public class Node implements Comparable<Node> {
    private NodeId id;

    public NodeId getId() {
        return id;
    }
    
    private NodeInfo info;

    public NodeInfo getInfo() {
        return info;
    }
    
    public Node(NodeId id, NodeInfo info) {
        this.id = id;
        this.info = info;
    }

    @Override
    public String toString() {
        return "Node{" + "id=" + id + ", info=" + info + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public int compareTo(Node o) {
        return this.id.compareTo(o.id);
    }
}
