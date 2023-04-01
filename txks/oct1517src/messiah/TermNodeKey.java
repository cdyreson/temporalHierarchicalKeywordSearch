package messiah;

import java.io.Serializable;
import java.util.Objects;
import usu.NodeId;

/**
 *
 * @author truongbaquan
 */
public class TermNodeKey implements Serializable, Comparable {
    
    private final String term;

    /**
     * Get the value of term
     *
     * @return the value of term
     */
    public String getTerm() {
        return term;
    }

    private final NodeId nodeId;

    /**
     * Get the value of nodeId
     *
     * @return the value of nodeId
     */
    public NodeId getNodeId() {
        return nodeId;
    }

    public TermNodeKey(String term, NodeId nodeId) {
        this.term = term;
        this.nodeId = nodeId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TermNodeKey other = (TermNodeKey) obj;
        if (!Objects.equals(this.term, other.term)) {
            return false;
        }
        if (this.nodeId != other.nodeId) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return nodeId.hashCode();
    }

    public int compareTo(TermNodeKey o) {
        int strCompareTo = this.term.compareTo(o.term);
        if (strCompareTo != 0)
            return strCompareTo;
        else {
            return this.nodeId.compareTo(o.nodeId);
        }
    }

    @Override
    public String toString() {
        return "TermNodeKey{" + "term=" + term + ", nodeId=" + nodeId + '}';
    }

    @Override
    public int compareTo(Object o) {
        TermNodeKey other = (TermNodeKey)o;
        int strCompareTo = this.term.compareTo(other.term);
        if (strCompareTo != 0)
            return strCompareTo;
        else {
            return this.nodeId.compareTo(other.nodeId);
        }
    }
}
