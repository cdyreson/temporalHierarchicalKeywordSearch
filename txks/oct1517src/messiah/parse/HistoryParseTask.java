package messiah.parse;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamReader;
//import messiah.Main;
import messiah.database.Database;
//import messiah.parse.generic.HistoryNodeIndexBuilder;
//import messiah.parse.generic.ItemIndexBuilder;
import messiah.parse.generic.KeywordIndexBuilder;
import messiah.parse.generic.NodeIndexBuilder;
import messiah.parse.generic.PathIndexBuilder;
import messiah.parse.generic.PrefixIndexBuilder;
import messiah.parse.generic.TermIndexBuilder;
import messiah.parse.generic.TermNodeIndexBuilder;
import messiah.parse.generic.TypeIndexBuilder;
import usu.dln.DLNFactory;
import usu.dln.HistoryDLNFactory;

/**
 * Parse, creating the history and item documents from a snapshot XML document
 *
 * @author Curtis Dyreson
 */
public class HistoryParseTask extends CurtParseTask {

    public HistoryParseTask(/*Main controller, */ Database db, File inputFile) throws FileNotFoundException {
        //super(controller, db, inputFile);
        super(db, inputFile);
        this.nodeIdBuilder = new NodeIdBuilder(new HistoryDLNFactory());
    }

    public HistoryParseTask(/*Main controller, */ Database db, NodeIdBuilder nodeBuilder, XMLStreamReader reader)  {
        //super(controller, db, inputFile);
        super(db, nodeBuilder, reader);
    }
    
    /*
    public void parse() {
        super.parse();
    }
    */
    
    public void createListeners() {
         //HistoryNodeIndexBuilder historyNodeIndexBuilder = null;
        listeners = new ArrayList(6);
        NodeIndexBuilder nodeIndexBuilder = null;
        PathIndexBuilder pathIndexBuilder = null;
        TypeIndexBuilder typeIndexBuilder = null;
        //ItemIndexBuilder itemIndexBuilder = null;
        KeywordIndexBuilder keywordIndexBuilder = null;
        TermIndexBuilder termIndexBuilder = null;
        TermNodeIndexBuilder termNodeIndexBuilder = null;
        PrefixIndexBuilder prefixIndexBuilder = null;
        try {
            pathIndexBuilder = new PathIndexBuilder(db, new DLNFactory());
            nodeIndexBuilder = new NodeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
                       //historyNodeIndexBuilder = new HistoryNodeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);

            //itemIndexBuilder = new ItemIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            typeIndexBuilder = new TypeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            keywordIndexBuilder = new KeywordIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            termIndexBuilder = new TermIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            termNodeIndexBuilder = new TermNodeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            prefixIndexBuilder = new PrefixIndexBuilder(db);

        } catch (Exception e) {
            Logger.getLogger(CurtParseTask.class.getName()).log(Level.SEVERE, null, e);
        }

             // listeners.add(constantsReader);
        listeners.add(nodeIdBuilder);

        listeners.add(pathIndexBuilder);
        
        listeners.add(nodeIndexBuilder);
       
                //listeners.add(itemIndexBuilder);

        listeners.add(typeIndexBuilder);

        listeners.add(keywordIndexBuilder);
        
        listeners.add(prefixIndexBuilder);

        //listeners.add(termIndexBuilder);
        //listeners.add(termNodeIndexBuilder);
        
    }

}
