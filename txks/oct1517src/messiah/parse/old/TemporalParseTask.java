package messiah.parse;

import java.io.*;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import messiah.Config;
import messiah.Main;
import messiah.Path;
import messiah.database.Database;
import messiah.parse.generic.KeywordIndexBuilder;
import messiah.parse.generic.NodeIndexBuilder;
import messiah.parse.generic.PathIndexBuilder;
import messiah.parse.generic.TermIndexBuilder;
import messiah.parse.generic.TermNodeIndexBuilder;
import messiah.parse.generic.TypeIndexBuilder;
import messiah.storage.generic.TermFreqManager;
import usu.PathIdFactory;
import usu.dln.DLNFactory;

/**
 * A task (SwingWorker) which parses an XML document and stores its data into a
 * Berkeley database.
 *
 * @author Truong Ba Quan
 */
public class TemporalParseTask extends CurtParseTask {

    public TemporalParseTask(Database db, File inputFile) throws FileNotFoundException {
        //super(controller, db, inputFile);
        super(db, inputFile);
    }

    public void createListeners() {
        NodeIdBuilder nodeIdBuilder = new NodeIdBuilder(new DLNFactory());
        PathIndexBuilder pathIndexBuilder = new PathIndexBuilder(db, new DLNFactory());

        NodeIndexBuilder nodeIndexBuilder = null;
        TypeIndexBuilder typeIndexBuilder = null;
        KeywordIndexBuilder keywordIndexBuilder = null;
        TermIndexBuilder termIndexBuilder = null;
        TermNodeIndexBuilder termNodeIndexBuilder = null;
        try {
            nodeIndexBuilder = new NodeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);

            //metaIndexBuilder= new NodeMetaDataIndexBuilder(db, nodeIdBuilder);

            typeIndexBuilder = new TypeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            //h_iIndexBuilder= new History_ItemIdsIndexBuilder(db, nodeIdBuilder, pathTracker);

            keywordIndexBuilder = new KeywordIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            termIndexBuilder = new TermIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            termNodeIndexBuilder = new TermNodeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            //i_hIndexBuilder = new Item_HIstoryIdsIndexBuilder(db, nodeIdBuilder, pathTracker);

        } catch (Exception e) {
            Logger.getLogger(CurtParseTask.class.getName()).log(Level.SEVERE, null, e);
        }

        //parse(nodeIdBuilder, pathTracker,
        //        nodeIndexBuilder, metaIndexBuilder, typeIndexBuilder, keywordIndexBuilder,
        //        termIndexBuilder, termNodeIndexBuilder, itemNodeIndexBuilder, h_iIndexBuilder, i_hIndexBuilder);
              //listeners.add(constantsReader);
        listeners.add(nodeIdBuilder);

        listeners.add(pathIndexBuilder);
        listeners.add(nodeIndexBuilder);
        listeners.add(typeIndexBuilder);
        listeners.add(keywordIndexBuilder);
        listeners.add(termIndexBuilder);
        listeners.add(termNodeIndexBuilder);


    }

}
