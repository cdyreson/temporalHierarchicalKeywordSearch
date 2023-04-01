package messiah;

//import messiah.numbering.Version;
//import messiah.numbering.TDLN;
import java.io.Serializable;
import usu.PathId;

/**
 * Encapsulates information about nodes such as path info (path id) and value
 * (optional)
 *
 * @author Truong Ba Quan
 */
public class NodeInfo implements Serializable {

    private PathId pathId;
    private String data; // either label or value
    private int level;

    public NodeInfo(PathId pathId, String data, int level) {
        this.pathId = pathId;
        this.data = data;
        this.level = level;
    }

    public PathId getPathId() {
        return pathId;
    }

    public final String getData() {
        return data;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "NodeInfo{" + "pathId=" + pathId + ", data=" + data + ", level=" + level + '}';
    }

}
