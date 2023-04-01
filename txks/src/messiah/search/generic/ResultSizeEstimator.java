package messiah.search.generic;

//import messiah.search.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import messiah.KeywordInfo;
import usu.NodePathId;
import messiah.Path;
import messiah.search.slca.generic.BinarySLCAFinder;
import messiah.storage.generic.DbAccess;
import usu.PathId;
import usu.PathIdFactory;
import usu.dln.DLNFactory;
import messiah.database.Database;

/**
 *
 * @author truongbaquan
 */
public class ResultSizeEstimator {
    
    private final DbAccess db;
    private final Map<String, KeywordInfo> keywordMap;
    private final BinarySLCAFinder binarySLCAFinder;

    public ResultSizeEstimator(DbAccess db, Database database) {
        this.db = db;
        this.keywordMap = database.keywordIndex;
        this.binarySLCAFinder = new BinarySLCAFinder(new DLNFactory());
    }

    public double estimate(String[] query) {
        KeywordInfo[] infos = new KeywordInfo[query.length];
        for (int i = 0; i < query.length; i++) {
            infos[i] = keywordMap.get(query[i]);
            if (infos[i] == null) return 0;
        }
        TreeSet<PathId>[] typeSets = new TreeSet[infos.length];
        for (int i = 0; i < infos.length; i++) {
            KeywordInfo info = infos[i];
            TreeSet<PathId> typeSet = new TreeSet<>(info.getPathIds());
            for (NodePathId nodePathID : info.getNodeIds()) {
                typeSet.add(nodePathID.pathId);
            }
            typeSets[i] = typeSet;
        }
        Set<PathId> pathLcaIds = this.binarySLCAFinder.getSLCA(typeSets);
        double estValue = 0;
        for (PathId pathLcaId : pathLcaIds) {
            double prob = 1;
            for (int i = 0; i < typeSets.length; i++) {
                double oneKeywordProb = 1;
                KeywordInfo info = infos[i];
                for (PathId typeId : info.getPathIds()) {
                    if (pathLcaId.isAncestorOrDescendant(typeId)) {
                        oneKeywordProb *= 1 - computeProbability(pathLcaId, typeId);
                    }
                }
                for (NodePathId nodePathId : info.getNodeIds()) {
                    if (pathLcaId.isAncestorOrDescendant(nodePathId.pathId)) {
                        oneKeywordProb *= 1 - (1.0 / db.getPath(nodePathId.pathId).getInfo().getNumApp()) * computeProbability(pathLcaId, nodePathId.pathId);
                    }
                }
                oneKeywordProb = 1 - oneKeywordProb;
                prob *= oneKeywordProb;
            }
            //System.out.println("Curt: resultsizeestimator " + pathLcaId + " map " + db.getPath(pathLcaId));
            //for (Path p : db.getPaths()) {
            //    System.out.println("Curt: p " + p);
            //}
            //for (PathId p : pathLcaIds) {
            //    System.out.println("Curt: claid " + p);
            //}
            
            Path pathLca = db.getPath(pathLcaId);
            estValue += pathLca.getInfo().getNumApp() * prob;
        }
        return estValue;
    }

    private double computeProbability(PathId ancestorTypeId, PathId descendantTypeId) {
        double prob = 1;
        PathId typeId = descendantTypeId;
        while (typeId.isNotEqualTo(ancestorTypeId)) {
            Path path = db.getPath(typeId);
            prob *= path.getInfo().getParentAppRate();
            if (path.getInfo().getLevel() - 1 == -2) {
                throw new IllegalStateException("Something wrong here!!!!!");
            }
            typeId = typeId.getAncestor(path.getInfo().getLevel() - 1);
        }
        return prob;
    }
    
}
