/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messiah.TestAmani;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import messiah.Config;
import messiah.Main;
import messiah.parse.CurtParseTask;
import usu.NodeId;
import usu.dln.DLNNodeIdBinding;
import usu.dln.DLNPathIdBinding;

/**
 *
 * @author Amani Shatnawi
 */
public class tesParsing {
    
    /*
    public void testParser(String datasetName, File parsedFile) {
        //closeDB();
        //System.out.println("asdf " + Config.getDatasetFolder(datasetName) + " " + datasetName);
        new File(Config.getDatasetFolder(datasetName) + "/").mkdir();
        new File("temp" + "/" + datasetName).mkdir();
        try {
            curDataset = new BdbDataset(datasetName, new SortedNodeIdDLNBinding(), new SortedPathIdDLNBinding(), true);
            CurtParseTask task = new CurtParseTask(this, parsedFile);
            System.out.println("Curt : executing ");
            //task.doInBackground();
            resetIndexes();
            //task.doIt();
        } catch (DatabaseException | FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public static void main(String[] args) throws FileNotFoundException {
        try {
            BdbDataset bdb = new BdbDataset("curt", new SortedNodeIdDLNBinding(), new SortedPathIdDLNBinding(), false);
  
     }
           
        
   */
}
