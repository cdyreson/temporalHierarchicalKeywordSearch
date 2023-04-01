package messiah.storage.generic;

import messiah.storage.*;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.*;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import messiah.TermNodeKey;
import usu.PathId;
import java.util.SortedMap;

/**
 * A comparable index key consisting of a term and a node (represented as nodeId)
 * @author truongbaquan
 */
public class DumpTermNodeIndex {
   
    public static void main(String[] args) {
        try {
            messiah.database.Database db = new messiah.database.berkeleydb.Database(true, false);
            SortedMap<TermNodeKey, PathId> map = db.termNodeIndex;
            for (TermNodeKey key : map.keySet()) {
                System.out.println("key = " + key + " --> " + map.get(key));
            } 

        } catch (DatabaseException ex) {
            Logger.getLogger(DumpTermNodeIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
