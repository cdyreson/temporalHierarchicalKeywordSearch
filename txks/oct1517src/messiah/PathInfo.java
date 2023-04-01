package messiah;

import java.io.Serializable;

/**
 *
 * @author John Truong Ba Quan
 */
public class PathInfo implements Serializable {
    private String pathExpr = null;

    public String getPathExpr() {
        return pathExpr;
    }
    
    private int level = 0;

    public int getLevel() {
        return level;
    }
    
    private boolean repeatable = false;

    public boolean isRepeatable() {
        return repeatable;
    }
    
    private final int numApp;

    public int getNumApp() {
        return numApp;
    }
    
    private final double parentAppRate;

    public double getParentAppRate() {
        return parentAppRate;
    }
    
    public PathInfo(String pathExpr, int level, boolean isRepeatable, int numApp, double parentAppRate) {
        this.pathExpr = pathExpr;
        this.level = level;
        this.repeatable = isRepeatable;
        this.numApp = numApp;
        this.parentAppRate = parentAppRate;
    }

    @Override
    public String toString() {
        return "PathInfo{" + "pathExpr=" + pathExpr + ", level=" + level + ", isRepeatable=" + repeatable + '}';
    }
    
}
