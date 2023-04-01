package messiah.parse;

import messiah.parse.old.ConstantsReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import messiah.database.Database;
import messiah.parse.generic.KeywordIndexBuilder;
import messiah.parse.generic.NodeIndexBuilder;
import messiah.parse.generic.PathIndexBuilder;
import messiah.parse.generic.TermIndexBuilder;
import messiah.parse.generic.TermNodeIndexBuilder;
import messiah.parse.generic.TypeIndexBuilder;
import usu.dln.DLNFactory;
import usu.dln.HistoryDLNFactory;

/**
 * A task (SwingWorker) which parses an XML document and stores its data into a
 * Berkeley database.
 *
 * @author Truong Ba Quan, Amani Shatnawi
 */
public class ParseTask extends SwingWorker<Void, Void> {

    protected Database db;

    // ConstantsReader constantsReader = new ConstantsReader();
    ArrayList<ParserListener> listeners;
    NodeIdBuilder<HistoryDLNFactory> nodeIdBuilder;
    private File inputFile;

    public ParseTask(Database db, NodeIdBuilder nodeBuilder) {
        this.listeners = new ArrayList<>();
        this.db = db;
        this.nodeIdBuilder = nodeBuilder;
    }

    public ParseTask(Database db, File inputFile) throws FileNotFoundException {
        this.db = db;
        this.nodeIdBuilder= new NodeIdBuilder(new HistoryDLNFactory());
        // check whether input file exists
        if (inputFile.exists()) {
            this.inputFile = inputFile;
        } else {
            throw new FileNotFoundException();
        }
    }

    public void doTheParsing() throws FileNotFoundException {
        int event;
        createListeners();
        try {
            // create the reader for reading XML document
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
            inputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
            inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new BufferedReader(new InputStreamReader((new FileInputStream(this.inputFile))), 1024));
            // traverse all XML nodes
            while (reader.hasNext()) {
                event = reader.next();
                parse(event, reader);

            }
            reader.close();
        } catch (XMLStreamException ex) {
            Logger.getLogger(ParseTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ParseTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The main parsing process which traverses XML nodes sequentially and
     * publish their data to the given listeners
     *
     * @param event
     * @param reader
     */
    public void parse(int event, XMLStreamReader reader) {
        System.out.println("Parsing...");
        try {

            // read next node
            switch (event) {
                case XMLStreamConstants.START_DOCUMENT:
                    // initializing
                    for (ParserListener listener : listeners) {
                        listener.startDocument();
                    }
                    break;
                case XMLStreamConstants.START_ELEMENT:
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
                    // System.out.println("End element");
                    for (ParserListener listener : listeners) {
                        listener.end(false, false);
                    }
                    break;
                case XMLStreamConstants.END_DOCUMENT:
                    // closing up
                    for (ParserListener listener : listeners) {
                        listener.endDocument();
                    }
                    break;
                default:// do nothing
                }
        } catch (Exception ex) {
            Logger.getLogger(ParseTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createListeners() {
        NodeIndexBuilder nodeIndexBuilder = null;
        PathIndexBuilder pathIndexBuilder = null;
        TypeIndexBuilder typeIndexBuilder = null;
        KeywordIndexBuilder keywordIndexBuilder = null;
        TermIndexBuilder termIndexBuilder = null;
        TermNodeIndexBuilder termNodeIndexBuilder = null;
        try {
            pathIndexBuilder = new PathIndexBuilder(db, new DLNFactory());
            nodeIndexBuilder = new NodeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            typeIndexBuilder = new TypeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            keywordIndexBuilder = new KeywordIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            termIndexBuilder = new TermIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            termNodeIndexBuilder = new TermNodeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
        } catch (Exception e) {
            Logger.getLogger(ParseTask.class.getName()).log(Level.SEVERE, null, e);
        }

        listeners.add(nodeIdBuilder);
        listeners.add(pathIndexBuilder);
        listeners.add(nodeIndexBuilder);
        listeners.add(typeIndexBuilder);
        listeners.add(keywordIndexBuilder);
        listeners.add(termIndexBuilder);
        listeners.add(termNodeIndexBuilder);

    }

    @Override
    public Void doInBackground() throws Exception {
       /* System.out.println("Start parsing");
        // Traverse the tree recursively to compute maximum depth and fanout (level order)
        // of the document tree along with list of paths
        //parse(constantsReader, pathsReader);
        System.out.println("Get constants and paths");
        this.setProgress(PRE_PARSE_PERCENT);

        pathParser = new PathExpReader();
        pathParser.parse(pathsReader.getPathExprs(), pathsReader.getRepeatablePathExprs(), new DLNFactory());

        System.out.println("Parse paths");

        for (Path path : (Collection<Path>) pathParser.getPathsSortedByPathExpr()) {
            System.out.println("doing " + path);
            db.pathIndex.put(path.getPathId(), path.getInfo());
        }

        System.out.println("Store constants and paths");

        this.setProgress(PATH_SUMMARY_PERCENT);

        this.doTheParsing();

        System.out.println("Store all indices");

        TermFreqManager.readFromBdb(db);
        //TermFreqManager.writeToCsv(new File(Config.getFreqFile(db.getDatasetName())));
        TermFreqManager.writeToCsv(new File(db.getDatasetName() + ".csv"));
        this.setProgress(DONE);*/
        return null;

    }

    @Override
    public void done() {

    }

}
