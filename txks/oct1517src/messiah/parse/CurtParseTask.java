package messiah.parse;

import java.io.*;
import java.util.ArrayList;
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
import messiah.parse.generic.KeywordIndexBuilder;
import messiah.parse.generic.NodeIndexBuilder;
import messiah.parse.generic.PathIndexBuilder;
import messiah.parse.generic.TermIndexBuilder;
import messiah.parse.generic.TermNodeIndexBuilder;
import messiah.parse.generic.TypeIndexBuilder;
import messiah.storage.generic.TermFreqManager;
import usu.dln.DLNFactory;
//import usu.dln.HistoryDLNFactory;

/**
 * A task (SwingWorker) which parses an XML document and stores its data into a
 * Berkeley database.
 *
 * @author Truong Ba Quan, Curtis Dyreson
 */
public class CurtParseTask extends SwingWorker<Void, Void> {

    public static final int PRE_PARSE_PERCENT = 10;
    public static final int PATH_SUMMARY_PERCENT = 15;
    public static final int PARSE_PERCENT = 60;
    public static final int INDEX_LIST_PERCENT = 70;
    public static final int TYPE_INDEX_LIST_PERCENT = 80;
    public static final int INVERTED_INDEX_LIST_PERCENT = 95;
    public static final int DONE = 100;
    public static final int[] progressPercent = new int[]{PRE_PARSE_PERCENT, PATH_SUMMARY_PERCENT,
        PARSE_PERCENT, PARSE_PERCENT, PARSE_PERCENT,
        INDEX_LIST_PERCENT, TYPE_INDEX_LIST_PERCENT, INVERTED_INDEX_LIST_PERCENT, DONE};

    //protected final Main controller;
    protected final Database db;

    private File inputFile;
    protected XMLStreamReader reader;
    NodeIdBuilder<DLNFactory> nodeIdBuilder;
    ArrayList<ParserListener> listeners;

    public CurtParseTask(Database db) {
        this.db = db;
    }

    public CurtParseTask(/*Main controller, */Database db, File inputFile) throws FileNotFoundException {
        //this.controller = controller;
        this.db = db;
        // check whether input file exists
        if (inputFile.exists()) {
            this.inputFile = inputFile;
        } else {
            System.out.println("Input file not found " + inputFile);
            throw new FileNotFoundException();
        }
        this.nodeIdBuilder = new NodeIdBuilder(new DLNFactory());

        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
            inputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
            inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);
            reader = inputFactory.createXMLStreamReader(new BufferedReader(new InputStreamReader((new FileInputStream(this.inputFile))), 1024));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CurtParseTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(CurtParseTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CurtParseTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CurtParseTask(/*Main controller, */Database db, NodeIdBuilder<DLNFactory> nodeBuilder, XMLStreamReader reader) {
        //this.controller = controller;
        this.db = db;
        this.nodeIdBuilder = nodeBuilder;
        this.reader = reader;
    }

    /**
     * The main parsing process which traverses XML nodes sequentially and
     * publish their data to the given listeners
     */
    public void parse() {
        System.out.println("CurtParseTask Parsing...");
        try {
            // create the reader for reading XML document
            // traverse all XML nodes
            long time1 = System.currentTimeMillis(); 
            while (reader.hasNext()) {
                // read next node
                int event = reader.next();
                parse(event);
                reader.close();
            }
            long time2 = System.currentTimeMillis(); 
            time2 = time2 - time1;
            System.out.println("Time taken " + time2);
            
        } catch (XMLStreamException ex) {
            Logger.getLogger(CurtParseTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CurtParseTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    static int count = 0;

    /**
     * The main parsing process which traverses XML nodes sequentially and
     * publish their data to the given listeners
     *
     * @param listeners
     */
    public void parse(int event) {
        //System.out.println("Parsing...");
        
        /*
         if (count == 3000000) {
         //if (count == 2000) {
         db.copyPartialDatabaseToDisk();
         count=0;
         }
        */
         
        switch (event) {
            case XMLStreamConstants.START_DOCUMENT:
                // initializing
                for (ParserListener listener : listeners) {
                    listener.startDocument();
                }
                break;
            case XMLStreamConstants.START_ELEMENT:
                count++;
                //System.out.println("Start element " + count);
                // read the start tag of an element
                for (ParserListener listener : listeners) {
                    listener.start(reader.getLocalName(), false, false);
                }
                // read attributes (attributes are read before element value)
                for (int i = 0; i < reader.getAttributeCount(); i++) {
                    // attribute tag start
                    for (ParserListener listener : listeners) {
                        listener.start(reader.getAttributeLocalName(i), true, false);
                    }
                    // attribute value start
                    for (ParserListener listener : listeners) {
                        listener.start(reader.getAttributeValue(i), true, true);
                    }
                    // attribute value end
                    for (ParserListener listener : listeners) {
                        listener.end(true, false);
                    }
                    // attribute tag end
                    for (ParserListener listener : listeners) {
                        listener.end(true, true);
                    }
                }
                break;
            case XMLStreamConstants.CHARACTERS:

                if (!reader.isWhiteSpace()) {
                    for (ParserListener listener : listeners) {
                        listener.start(reader.getText(), false, true);
                    }
                    for (ParserListener listener : listeners) {
                        listener.end(false, true);
                    }
                }

                break;
            case XMLStreamConstants.END_ELEMENT:
                // end element
                //System.out.println("End element");
                for (ParserListener listener : listeners) {
                    listener.end(false, false);
                }
                break;
            case XMLStreamConstants.END_DOCUMENT:
                // closing up
                //                        System.out.println("End Doc");
                for (ParserListener listener : listeners) {
                    listener.endDocument();
                }
                break;
            default:// do nothing
        }

    }

    public void createListeners() {

        listeners = new ArrayList();
        NodeIndexBuilder nodeIndexBuilder = null;
        PathIndexBuilder pathIndexBuilder = null;
        TypeIndexBuilder typeIndexBuilder = null;
        KeywordIndexBuilder keywordIndexBuilder = null;
        TermIndexBuilder termIndexBuilder = null;
        TermNodeIndexBuilder termNodeIndexBuilder = null;
        //ConstantsReader constantsReader = new ConstantsReader();
        try {
            pathIndexBuilder = new PathIndexBuilder(db, new DLNFactory());
            nodeIndexBuilder = new NodeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            typeIndexBuilder = new TypeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            keywordIndexBuilder = new KeywordIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            termIndexBuilder = new TermIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            termNodeIndexBuilder = new TermNodeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
        } catch (Exception e) {
            Logger.getLogger(CurtParseTask.class.getName()).log(Level.SEVERE, null, e);
        }
        //listeners.add(constantsReader);
        listeners.add(nodeIdBuilder);

        listeners.add(pathIndexBuilder);
        listeners.add(nodeIndexBuilder);
        listeners.add(typeIndexBuilder);
        listeners.add(keywordIndexBuilder);
        
        //listeners.add(termIndexBuilder);
        //listeners.add(termNodeIndexBuilder);

    }

    public void parseThisThing() {
        System.out.println("CurtParseTask: Start parsing");
        // Traverse the tree recursively to compute maximum depth and fanout (level order)
        // of the document tree along with list of paths
        createListeners();

        parse();

        System.out.println("Store all indices");

        //TermFreqManager.readFromBdb(db);
        //TermFreqManager.writeToCsv(new File(db.getDatasetName() + ".csv"));
    }

    @Override
    public Void doInBackground() throws Exception {
        System.out.println("CurtParseTask: Start parsing");
        // Traverse the tree recursively to compute maximum depth and fanout (level order)
        // of the document tree along with list of paths
        createListeners();

        this.setProgress(PRE_PARSE_PERCENT);

        this.setProgress(PATH_SUMMARY_PERCENT);

        parse();

        System.out.println("Store all indices");

        //TermFreqManager.readFromBdb(db);
        //TermFreqManager.writeToCsv(new File(db.getDatasetName() + ".csv"));
        this.setProgress(DONE);
        return null;
    }

    @Override
    public void done() {

    }

}
