package messiah;

import usu.PathId;
import usu.PathIdFactory;
import usu.dln.DLNFactory;

/**
 *
 * @author John Truong Ba Quan
 */
public class Path implements Comparable<Path> {
    private PathId pathId;
    private static final PathIdFactory factory = new DLNFactory();

    public PathId getPathId() {
        return pathId;
    }
    
    private PathInfo info = null;

    public PathInfo getInfo() {
        return info;
    }

    public Path() {
        this.pathId = factory.rootId();
        this.info = null;
    }
    
    public Path(PathId pathId, String pathExpr, int level, boolean isRepeatable, int numApp, double parentAppRate) {
        this.pathId = pathId;
        this.info = new PathInfo(pathExpr, level, isRepeatable, numApp, parentAppRate);
    }

    @Override
    public String toString() {
        return "Path{" + "pathId=" + pathId + ", info=" + info + '}';
    }

    @Override
    public int compareTo(Path o) {
        return pathId.compareTo(o.pathId);
    }
}
