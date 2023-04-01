/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usu.update;

import com.sleepycat.collections.StoredSortedMap;
import static com.sleepycat.je.utilint.LoggerUtils.info;
import static java.awt.SystemColor.info;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import messiah.NodeInfo;
import messiah.utils.Stopwatch;
import usu.NodeId;
import usu.PathId;
import messiah.database.Database;



/**
 *
 * @author Amani Shatnawi
 */
public class AddNode {
    
        PathId PathId;
        String nodeValue;
        NodeId nodeId;
        private Map<NodeId, NodeInfo> tempIndex = new TreeMap<>();

    
    public AddNode(Database db, String str, PathId pathId, NodeId id){
       this.nodeValue = str;
        this.PathId = pathId;
       this.nodeId = id.newChild();
    }
    
           
       
       // NodeIndexBuilder
         public void stor(){
               NodeInfo info = new NodeInfo(PathId, nodeValue, nodeId.getLevel());
                      tempIndex.put(nodeId, info);

         }
         
         
                

    
}
