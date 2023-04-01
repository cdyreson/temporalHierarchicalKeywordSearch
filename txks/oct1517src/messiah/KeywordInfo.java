package messiah;

import usu.NodePathId;
import usu.NodeId;
import usu.PathId;
import java.io.Serializable;
import java.util.Collection;
import java.util.TreeSet;

/**
 * KeywordInfo is a set the set of paths, nodePathIds, and items, 
 * associated with a keyword.
 * @author Truong Ba Quan and Curtis
 */
public class KeywordInfo implements Serializable {
    private TreeSet<PathId> pathIds;
    private final TreeSet<NodePathId> nodeIds = new TreeSet<NodePathId>();
    //private TreeSet<NodeId> itemIds;
    
    /*
    public int getNumberOfPathIds() {
        return (pathIds == null) ? 0 : pathIds.size();
    }
    */

    public TreeSet<PathId> getPathIds() {
        return (pathIds == null) ? new TreeSet<PathId>() : this.pathIds;
    }

    public boolean addPathId(PathId pathId) {
        if (pathIds == null) pathIds = new TreeSet<PathId>();
        return this.pathIds.add(pathId);
    }
    
    public boolean addAllPathIds(Collection<PathId> pathIds) {
        if (pathIds == null || pathIds.isEmpty()) return false;
        else {
            if (this.pathIds == null) this.pathIds = new TreeSet<PathId>();
            return this.pathIds.addAll(pathIds);
        }
    }
    
    public TreeSet<NodePathId> getNodeIds() {
        return this.nodeIds;
    }

    public boolean addNodeId(NodeId nodeId, PathId pathId) {
        return this.nodeIds.add(new NodePathId(nodeId, pathId));
    }

    /*
    public KeywordInfo merge(KeywordInfo other) {
        if (other.getNumberOfPathIds() > 0)
            this.addAllPathIds(other.getPathIds());
        this.nodeIds.addAll(other.getNodeIds());
        return this;
    }
    */
}
