package messiah;

//import messiah.numbering.Version;
//import messiah.numbering.TDLN;
import java.io.Serializable;
import usu.PathId;
import usu.temporal.Time;

/**
 * Encapsulates information about nodes such as path info (path id) and value
 * (optional)
 *
 * @author Truong Ba Quan
 */
public class NodeInfo implements Serializable {

    protected transient static final Time ALL_OF_TIME = new Time();
    private PathId pathId;
    private String data; // either label or value
    private int level;
    private Time timestamp = ALL_OF_TIME;

    public NodeInfo(PathId pathId, String data, int level) {
        this.pathId = pathId;
        this.data = data;
        this.level = level;
        this.timestamp = ALL_OF_TIME;
    }
    
    public NodeInfo(PathId pathId, String data, int level, Time time) {
        this.pathId = pathId;
        this.data = data;
        this.level = level;
        this.timestamp = time;
    }

    public PathId getPathId() {
        return pathId;
    }
    
    public void setTimestamp(Time time) {
        this.timestamp = time;
    }

    public Time getTime() {
        return this.timestamp;
    }
    
    public final String getData() {
        return data;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "NodeInfo{" + "pathId=" + pathId + ", data=" + data + ", level=" + level + ", timestamp=" + timestamp + '}';
    }

}
