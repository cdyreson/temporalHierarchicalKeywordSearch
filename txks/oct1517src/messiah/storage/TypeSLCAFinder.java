package messiah.storage;

import java.util.*;
import messiah.Path;
//import messiah.encoding.BinaryDeweyUtils;
import messiah.parse.PathExprUtilities;
import usu.PathId;

/**
 * This object maintains a data structure to efficiently compute
 * the SLCA between a path and paths of a term.
 * @author truongbaquan
 */
public final class TypeSLCAFinder {
    
    private HashMap<PathId,HashSet<String>> descTermMap;
    private LinkedHashMap<String,HashMap<PathId,Integer>> map;
    private TreeMap<String,ArrayList<Path>> pathBins;
        
    public TypeSLCAFinder(Collection<Path> paths /*, BinaryDeweyUtils typeUtils*/) {
        initialize(paths/*, typeUtils*/);
    }

    private LinkedHashMap<String,HashMap<PathId,Integer>> initialize(
            Collection<Path> paths/*, BinaryDeweyUtils typeUtils*/) {
        descTermMap = new HashMap<>(paths.size());
        for (Path path : paths) {
            PathId pathId = path.getPathId();
            descTermMap.put(pathId, new HashSet<String>());
            //System.out.println("Curt: pathId " + pathId + " info " + path.getInfo());
            for (int lvl = 1; lvl <= path.getInfo().getLevel(); lvl++) {
                PathId ancPathId = pathId.getAncestor(lvl);
                //System.out.println("Curt: ancPathId " + ancPathId + " " + lvl + " " + descTermMap.get(ancPathId));
                if (descTermMap.get(ancPathId) != null)
                  descTermMap.get(ancPathId).add(PathExprUtilities.getLastLabel(path.getInfo().getPathExpr()).toLowerCase());
            }
        }
        
        // group paths according to their last labels
        pathBins = new TreeMap<>();
        for (Path path : paths) {
            String lastLabel = PathExprUtilities.getLastLabel(path.getInfo().getPathExpr()).toLowerCase();
            ArrayList<Path> bin = pathBins.get(lastLabel);
            if (bin == null) {
                bin = new ArrayList<>();
                pathBins.put(lastLabel, bin);
            }
            bin.add(path);
        }

        map = new LinkedHashMap<>(pathBins.size());
        // for each group
        for (String term : pathBins.keySet()) {
            // find the set of paths with the same last label
            ArrayList<Path> bin = pathBins.get(term);
            // for each path in the dataset, find the LCA between it and the set of paths in bin
            HashMap<PathId,Integer> slcaDict = new HashMap<>(paths.size());
            for (Path path : paths) {
                int maxLvl = 0;
                for (Path path1 : bin) {
                    int slcaLvl = path.getPathId().computeNCALevel(path1.getPathId());
                    if (maxLvl < slcaLvl) maxLvl = slcaLvl;
                }
                slcaDict.put(path.getPathId(), maxLvl);
            }
            // store the computed results
            map.put(term, slcaDict);
        }
        return map;
    }
    
    public Set<String> getDescTermSet(PathId pathId) {
        return descTermMap.get(pathId);
    }
    
    public boolean isTypeLabel(String term) {
        return map.containsKey(term);
    }
    
    public ArrayList<Path> getPaths(String term) {
        return pathBins.get(term);
    }

    public int getSLCALvl(PathId pathId, String term) {
        HashMap<PathId,Integer> slcaDict = map.get(term);
        if (slcaDict == null) return 0;
        Integer slcaLvl = slcaDict.get(pathId);
        return (slcaLvl != null ? slcaLvl : 0);
    }
}
