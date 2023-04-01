package messiah.storage.generic;

import messiah.storage.*;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.*;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import messiah.KeywordInfo;
import messiah.TermNodeKey;
import usu.PathId;
import usu.NodeId;
import java.util.TreeMap;

/**
 * A comparable index key consisting of a term and a node (represented as nodeId)
 * @author truongbaquan
 */
public class DumpTermIndex {

    public static void main(String[] args) {
        try {
            messiah.database.Database bdb = new messiah.database.berkeleydb.Database(true, false);
            Map<String,SortedMap<NodeId,PathId>> map = bdb.termIndex;
            for (String key : map.keySet()) {
                System.out.println("key = " + key);
                Map<NodeId,PathId> m = map.get(key);
                if (m != null) {
                    for (NodeId n : m.keySet()) {
                        System.out.println("   NodeId " + n + " pathId " + m.get(n));
                    }
                }
            } 
//            String query = "XML";
//            Set<NodePathId> nodePathIds = map.get(query).getNodeIds();
//            System.out.println("nodePathIds.size() = " + nodePathIds.size());
//            Map<Long,Counter> pathIdMap = new TreeMap<Long,Counter>();
//            for (NodePathId nodePathId : nodePathIds) {
//                Counter counter = pathIdMap.get(nodePathId.pathId);
//                if (counter == null) {
//                    counter = new Counter();
//                    pathIdMap.put(nodePathId.pathId, counter);
//                }
//                counter.increment();
//            }
//            for (Long pathId : pathIdMap.keySet()) {
//                System.out.println(bdb.getPathMap().get(pathId).getPathExpr()
//                        + " : " + pathIdMap.get(pathId).getValue());
//            }
            
//            NodeIndex nodeIndex = new NodeIndex(bdb);
//            Map<Long,NodeInfo> nodeMap = nodeIndex.wrapSortedMap(false);
            
//            Main controller = new Main();
//            controller.loadDataset("shakespeare");
//            FullSLCAFinder algo = new FullSLCAFinder(controller.getCurIndex(), index, nodeIndex, typeIndex);
//            Set<Long> slcas = algo.getSLCA(new String[] {"hamlet"});
//            System.out.println("slca.size() = " + slcas.size());
//            for (Long slca : slcas) {
//                System.out.print(slca + " ");
//            }
//            System.out.println();
//            for (NodePathId nodePathId : nodePathIds) {
//                if (!slcas.contains(nodePathId.nodeId))
//                    System.out.println(nodePathId.nodeId);
//            }
            
        } catch (DatabaseException  ex) {
            Logger.getLogger(DumpTermIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
