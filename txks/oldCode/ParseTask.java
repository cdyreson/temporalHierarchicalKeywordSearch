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
import messiah.Main;
import messiah.Path;
import messiah.database.Database;
import messiah.parse.generic.History_ItemIdsIndexBuilder;
import messiah.parse.generic.ItemNodeMetaDataIndexBuilder;
import messiah.parse.generic.Item_HIstoryIdsIndexBuilder;
import messiah.parse.generic.KeywordIndexBuilder;
import messiah.parse.generic.NodeIndexBuilder;
import messiah.parse.generic.NodeMetaDataIndexBuilder;
import messiah.parse.generic.TermIndexBuilder;
import messiah.parse.generic.TermNodeIndexBuilder;
import messiah.parse.generic.TypeIndexBuilder;
import messiah.storage.generic.TermFreqManager;
import usu.PathIdFactory;
import usu.dln.DLNFactory;

/**
 * A task (SwingWorker) which parses an XML document and stores its data into
 * a Berkeley database.
 * @author Truong Ba Quan
 */
public class ParseTask extends SwingWorker<Void, Void> {
    public static final int PRE_PARSE_PERCENT = 10;
    public static final int PATH_SUMMARY_PERCENT = 15;
    public static final int PARSE_PERCENT = 60;
    public static final int INDEX_LIST_PERCENT = 70;
    public static final int TYPE_INDEX_LIST_PERCENT = 80;
    public static final int INVERTED_INDEX_LIST_PERCENT = 95;
    public static final int DONE = 100;
    public static final int[] progressPercent = new int[] {PRE_PARSE_PERCENT, PATH_SUMMARY_PERCENT,
                PARSE_PERCENT, PARSE_PERCENT, PARSE_PERCENT, 
                INDEX_LIST_PERCENT, TYPE_INDEX_LIST_PERCENT, INVERTED_INDEX_LIST_PERCENT, DONE};

    private Main controller;
    private final Database db;
    
    ConstantsReader constantsReader = new ConstantsReader();
    PathsReader pathsReader = new PathsReader();
    PathExpReader pathParser = null;

    private File inputFile;
    private File tempDir;


    public ParseTask(Main controller, Database db, File inputFile) throws FileNotFoundException {
        this.controller = controller;
        //this.db = controller.getCurDataset();
        this.db = db;
        // check whether input file exists
        if (inputFile.exists())
            this.inputFile = inputFile;
        else throw new FileNotFoundException();
        this.factory = fac;
        tempDir = new File("temp");
        if (!tempDir.exists()) tempDir.mkdir();
    }
    
    /**
     * The main parsing process which traverses XML nodes sequentially
     * and publish their data to the given listeners
     * @param listeners 
     */
    private void parse(ParserListener... listeners) {
        System.out.println("Parsing...");
        try {
            // create the reader for reading XML document
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
            inputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new BufferedReader(new InputStreamReader((new FileInputStream(this.inputFile))), 1024));
            // traverse all XML nodes
            while (reader.hasNext()) {
                // read next node
                int event = reader.next();
                switch (event) {
                    case XMLStreamConstants.START_DOCUMENT:
                        // initializing
                        for (ParserListener listener : listeners)
                            listener.startDocument();
                        break;
                    case XMLStreamConstants.START_ELEMENT:
                        // read the start tag of an element
                        for (ParserListener listener : listeners) 
                            listener.start(reader.getLocalName(), false, false);
                        // read attributes (attributes are read before element value)
                        for (int i = 0; i < reader.getAttributeCount(); i++) {
                            // attribute tag start
                            for (ParserListener listener : listeners) 
                                listener.start(reader.getAttributeLocalName(i), true, false);
                            // attribute value start
                            for (ParserListener listener : listeners) 
                                listener.start(reader.getAttributeValue(i), true, true);
                            // attribute value end
                            for (ParserListener listener : listeners) 
                                listener.end(true, false);
                            // attribute tag end
                            for (ParserListener listener : listeners) 
                                listener.end(true, true);                          
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:

                        if (!reader.isWhiteSpace()) {
                            for (ParserListener listener : listeners)
                                listener.start(reader.getText(), false, true);
                            for (ParserListener listener : listeners)
                                listener.end(false, true);
                        }
                                
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        // end element
                        // System.out.println("End element");
                        for (ParserListener listener : listeners)
                            listener.end(false, false);
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                        // closing up
                        //                        System.out.println("End Doc");
                        for (ParserListener listener : listeners)
                            listener.endDocument();
                        break;
                    default:// do nothing
                        //System.out.println("doing nothing");
                }
                reader.close();
                //System.out.println("done parsing");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParseTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(ParseTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ParseTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public Void doInBackground() throws Exception {
        System.out.println("Start parsing");
        // Traverse the tree recursively to compute maximum depth and fanout (level order)
        // of the document tree along with list of paths
        parse(constantsReader, pathsReader);
        System.out.println("Get constants and paths");
        this.setProgress(PRE_PARSE_PERCENT);

        pathParser = new PathExpReader();
        
        pathParser.parse(pathsReader.getPathExprs(), pathsReader.getRepeatablePathExprs(), new DLNFactory());

        System.out.println("Parse paths");

        for (Path path : (Collection<Path>)pathParser.getPathsSortedByPathExpr()) {
            System.out.println("doing " + path);
            db.pathIndex.put(path.getPathId(), path.getInfo());
        }

        System.out.println("Store constants and paths");

        this.setProgress(PATH_SUMMARY_PERCENT);

        NodeIdBuilder nodeIdBuilder = new NodeIdBuilder(this.constantsReader.getMaxDepth(), this.constantsReader.getLevelOrders(), new DLNFactory());
        PathTracker pathTracker = new PathTracker(pathParser);

        NodeIndexBuilder nodeIndexBuilder = null;
        NodeMetaDataIndexBuilder metaIndexBuilder = null;
        TypeIndexBuilder typeIndexBuilder = null;
        KeywordIndexBuilder keywordIndexBuilder = null;
        TermIndexBuilder termIndexBuilder = null;
        TermNodeIndexBuilder termNodeIndexBuilder = null;
        ItemNodeMetaDataIndexBuilder itemNodeIndexBuilder = null;
        History_ItemIdsIndexBuilder h_iIndexBuilder=null;
        Item_HIstoryIdsIndexBuilder i_hIndexBuilder=null;
        try {
            nodeIndexBuilder = new NodeIndexBuilder(db, nodeIdBuilder, pathTracker);
            
            //metaIndexBuilder= new NodeMetaDataIndexBuilder(db, nodeIdBuilder);
            
           itemNodeIndexBuilder = new ItemNodeMetaDataIndexBuilder(db, nodeIdBuilder);

            typeIndexBuilder = new TypeIndexBuilder(db, nodeIdBuilder, pathTracker);
            //h_iIndexBuilder= new History_ItemIdsIndexBuilder(db, nodeIdBuilder, pathTracker);

            keywordIndexBuilder = new KeywordIndexBuilder(db, nodeIdBuilder, pathTracker);
            termIndexBuilder = new TermIndexBuilder(db, nodeIdBuilder, pathTracker);
            termNodeIndexBuilder = new TermNodeIndexBuilder(db, nodeIdBuilder, pathTracker);
            //i_hIndexBuilder = new Item_HIstoryIdsIndexBuilder(db, nodeIdBuilder, pathTracker);
            
        } catch (Exception e) {
            Logger.getLogger(ParseTask.class.getName()).log(Level.SEVERE, null, e);
        }

        //parse(nodeIdBuilder, pathTracker,
        //        nodeIndexBuilder, metaIndexBuilder, typeIndexBuilder, keywordIndexBuilder,
        //        termIndexBuilder, termNodeIndexBuilder, itemNodeIndexBuilder, h_iIndexBuilder, i_hIndexBuilder);
        parse(nodeIdBuilder, pathTracker,
                nodeIndexBuilder, typeIndexBuilder, keywordIndexBuilder,
                termIndexBuilder, termNodeIndexBuilder);
        
        System.out.println("Store all indices");
        
        TermFreqManager.readFromBdb(db);
        //TermFreqManager.writeToCsv(new File(Config.getFreqFile(db.getDatasetName())));
        TermFreqManager.writeToCsv(new File(db.getDatasetName()+ ".csv"));
        this.setProgress(DONE);
        return null;
    }

    @Override
    public void done() {
        
    }
 
}
