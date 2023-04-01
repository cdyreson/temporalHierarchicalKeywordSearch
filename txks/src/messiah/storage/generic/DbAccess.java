package messiah.storage.generic;

import messiah.storage.TypeSLCAFinder;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import messiah.Config;
import messiah.Path;
import messiah.PathInfo;
import usu.PathId;
import messiah.database.Database;

/**
 *
 * @author John Truong Ba Quan
 */
public class DbAccess {

    private TypeSLCAFinder typeSLCAFinder;

    public TypeSLCAFinder getTypeSLCAFinder() {
        return typeSLCAFinder;
    }

    private Database db;

    public Database getDb() {
        return db;
    }

    private TreeMap<PathId, Path> paths = new TreeMap<>();

    public Set<PathId> getPathKeys() {
        return this.paths.keySet();
    }

    public Collection<Path> getPaths() {
        return this.paths.values();
    }

    public Path getPath(PathId pathId) {
        return this.paths.get(pathId);
    }

    private HashMap<PathId, String[]> pathLabelMap = new HashMap<>();

    /**
     * Gets the label of a path at a specified level
     *
     * @param pathId The ID of the path
     * @param level The level of the label
     * @return
     */
    public String getLabel(PathId pathId, int level) {
        return pathLabelMap.get(pathId)[level];
    }

    public String getLabel(PathId pathId) {
        int level = paths.get(pathId).getInfo().getLevel();
        return getLabel(pathId, level);
    }

    private HashMap<PathId, HashMap<PathId, Integer>> pathLCALevel = new HashMap<>();

    public int getLCALevel(long pathId1, long pathId2) {
        return this.pathLCALevel.get(pathId1).get(pathId2);
    }

    private HashMap<PathId, Integer> pathEntityLevelMap;

    public int getPathEntityLevel(PathId pathId) {
        return pathEntityLevelMap.get(pathId);
    }

    public DbAccess(Database db) {
        this.db = db;
        //this.initialLoad();
    }

    /**
     * Loads some small data into the memory to speed up processing
     */
    private void initialLoad() {

        for (PathId pathId : this.db.pathIndex.keySet()) {
            PathInfo info = this.db.pathIndex.get(pathId);
            //System.out.println("pathid " + pathId);
            Path path = new Path(pathId, info.getPathExpr(), info.getLevel(), info.isRepeatable(),
                    info.getNumApp(), info.getParentAppRate());
            this.paths.put(pathId, path);
            //System.out.println("Curt: pathId " + pathId + " info " + path);
        }

        for (Path path : this.paths.values()) {
            String[] tokens = path.getInfo().getPathExpr().split("[.]");
            String[] labels = new String[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].length() > 0) {
                    labels[i] = tokens[i].substring(0, tokens[i].length() - 1);
                } else {
                    labels[i] = "";
                }
            }
            this.pathLabelMap.put(path.getPathId(), labels);
        }

        for (Path path1 : this.paths.values()) {
            HashMap<PathId, Integer> lcaLvlMap = new HashMap<>();
            this.pathLCALevel.put(path1.getPathId(), lcaLvlMap);
            for (Path path2 : this.paths.values()) {
                lcaLvlMap.put(path2.getPathId(), path1.getPathId().computeNCALevel(
                        path1.getInfo().getLevel(),
                        path2.getPathId(), path2.getInfo().getLevel()));
            }
        }

        this.pathEntityLevelMap = new HashMap<>(paths.size());
        for (Path path : this.paths.values()) {
            int level = path.getInfo().getLevel();
            while (level > 1) {
                PathId pathId = path.getPathId();
                //System.out.println("Curt: initialLoad pathId " + pathId + " level " + level);
                PathId ancestorPathId = pathId.getAncestor(--level);

                Path ancestorPath = this.getPath(ancestorPathId);
                //System.out.println("Curt: initialLoad ancId " + ancestorPathId + " info " + ancestorPath);
                if (ancestorPath == null) {
                    break;
                }
                if (ancestorPath.getInfo().isRepeatable()) {
                    break;
                }
                level--;
            }
            pathEntityLevelMap.put(path.getPathId(), level);
        }

        this.typeSLCAFinder = new TypeSLCAFinder(this.getPaths()/*, this.pathUtils*/);

    }
}
