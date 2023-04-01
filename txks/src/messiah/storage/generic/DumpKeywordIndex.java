 package messiah.storage.generic;

import messiah.storage.*;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.DatabaseException;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import messiah.Config;
import messiah.KeywordInfo;
import usu.NodeId;
import usu.NodePathId;
import usu.dln.DLNNodeIdBinding;
import usu.dln.DLNPathIdBinding;
import messiah.database.Database;

/**
 *
 * @author truongbaquan
 */
public class DumpKeywordIndex  {
    
    public static void main(String[] args) {
        try {
            Database bdb = new messiah.database.berkeleydb.Database(true, false);
            Map<String,KeywordInfo> map = bdb.keywordIndex;
            System.out.println("map.size() = " + map.size());
            for (String key : map.keySet()) {
                System.out.println("key = " + key + " --> ");
                for (NodePathId nodeId : map.get(key).getNodeIds()) {
                    System.out.println(" nodeId " + nodeId.nodeId);
                    System.out.println(" pathId " + nodeId.pathId);
                }
            }
           
        } catch (DatabaseException ex) {
            Logger.getLogger(DumpKeywordIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
