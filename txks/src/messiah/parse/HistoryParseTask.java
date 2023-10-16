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
import messiah.parse.generic.TypeIndexBuilder;
import usu.dln.DLNFactory;
import usu.dln.HistoryDLNFactory;

/**
 * Parse, creating the history and item documents from a snapshot XML document
 *
 * @author Curtis Dyreson
 */
public class HistoryParseTask extends CurtParseTask {

    public HistoryParseTask(Database db, File inputFile, IntervalGenerator i, int maxNodes) throws FileNotFoundException {
        super(db, inputFile, maxNodes);
        HistoryDLNFactory h = new HistoryDLNFactory();
        this.nodeIdBuilder = new TemporalNodeIdBuilder(h, i);
    }

    public HistoryParseTask(Database db, NodeIdBuilder nodeBuilder, XMLStreamReader reader)  {
        super(db, nodeBuilder, reader);
    }
    
    public void createListeners() {
        listeners = new ArrayList(5);
        NodeIndexBuilder nodeIndexBuilder = null;
        PathIndexBuilder pathIndexBuilder = null;
        TypeIndexBuilder typeIndexBuilder = null;
        KeywordIndexBuilder keywordIndexBuilder = null;
        try {
            pathIndexBuilder = new PathIndexBuilder(db, new DLNFactory());
            nodeIndexBuilder = new NodeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            typeIndexBuilder = new TypeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            keywordIndexBuilder = new KeywordIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);

        } catch (Exception e) {
            Logger.getLogger(CurtParseTask.class.getName()).log(Level.SEVERE, null, e);
        }
        listeners.add(nodeIdBuilder);
        listeners.add(pathIndexBuilder);     
        listeners.add(nodeIndexBuilder);
        listeners.add(typeIndexBuilder);
        listeners.add(keywordIndexBuilder);  
    }

}
