package messiah.parse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import static com.fasterxml.jackson.core.JsonToken.END_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_FALSE;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
import static com.fasterxml.jackson.core.JsonToken.VALUE_TRUE;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Stack;
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
import messiah.parse.generic.TypeIndexBuilder;
import org.apache.wink.json4j.utils.XML;
import usu.dln.DLNFactory;
import usu.dln.HistoryDLNFactory;
//import usu.dln.HistoryDLNFactory;

/**
 * A task (SwingWorker) which parses an XML document and stores its data into a
 * Berkeley database.
 *
 * @author Truong Ba Quan, Curtis Dyreson
 */
public class JSONParseTask extends SwingWorker<Void, Void> {

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
    int maxNodes = 0;
    private JsonParser jp;

    public JSONParseTask(Database db) {
        this.db = db;
    }
    
    public JSONParseTask(/*Main controller, */Database db, File inputFile, int maxNodes) throws FileNotFoundException {
        //this.controller = controller;
        this.db = db;
        this.maxNodes = maxNodes;
        // check whether input file exists
        if (inputFile.exists()) {
            this.inputFile = inputFile;
        } else {
            System.out.println("Input file not found " + inputFile);
            throw new FileNotFoundException();
        }
        this.nodeIdBuilder = new NodeIdBuilder(new HistoryDLNFactory());

        try {
            JsonFactory jf = new JsonFactory();
            this.jp = jf.createParser(new FileInputStream(this.inputFile));
            

        } catch (FileNotFoundException ex) {
            Logger.getLogger(JSONParseTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(JSONParseTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     private int innerParse() {
        //System.out.println("maxNodes are " + this.maxNodes);
         int count = 0;
        try {          
            JsonToken t;
            // initializing
            for (ParserListener listener : listeners) {
                listener.startDocument();
            }
            Stack<String> nameStack = new Stack();
            Stack<Boolean> arrayStack = new Stack();
            //String currentName = "root";
            nameStack.push("root");
            arrayStack.push(false);
            // Introduce a root element
            /*
            for (ParserListener listener : listeners) {
                listener.start("root", false, false);
            }
            */
            int depth = 0;
            while ((t = this.jp.nextToken()) != null) {
                if (this.maxNodes != 0 && count > this.maxNodes) break;
                //System.out.println("depht is " + depth);
                switch (t) {
                    case START_OBJECT:
                        count++;
                        //System.out.println("start object");
                        for (ParserListener listener : listeners) {
                            listener.start(nameStack.peek(), false, false);
                        } 
                        arrayStack.push(false);
                        depth++;
                        break;
                    case END_OBJECT:
                        //System.out.println("end object");
                        depth--;
                        arrayStack.pop();
                        if (arrayStack.peek()) {
                            
                        }
                        for (ParserListener listener : listeners) {
                            if (depth == 0) {
                                listener.endDocument();
                            } else {
                                listener.end(false, false);
                            } 
                        }
                        //nameStack.pop();
                        break;
                    case START_ARRAY:
                        depth++;
                        //System.out.println("start array");
                        break;
                    case END_ARRAY:
                        depth--;
                        //System.out.println("end array");
                        /*
                        arrayStack.pop();
                        nameStack.pop();
                        //nameStack.pop();
                        for (ParserListener listener : listeners) {
                            if (depth == 0) {
                                listener.endDocument();
                            } else {
                                listener.end(false, false);
                            }
                        }
                        previousIsFieldName = false;
                        */
                        break;
                    case FIELD_NAME:
                        //depth++;
                        count++;
                        String name = this.jp.getCurrentName();
                        //System.out.println("field name " + name);
                        nameStack.push(name);
                        //currentName = jp.getCurrentName();
                        //System.out.println("Start element " + reader.getLocalName());
                        // read the start tag of an element
                        break;
                    case VALUE_NUMBER_INT:
                    case VALUE_NUMBER_FLOAT:
                    case VALUE_STRING:
                    case VALUE_FALSE:
                    case VALUE_TRUE:
                    case VALUE_NULL:
                        count++;
                        String value = "";
                        switch (t) {
                            case VALUE_FALSE:
                                value = "false";
                                break;
                            case VALUE_TRUE:
                                value = "true";
                                break;
                            case VALUE_NULL:
                                value = "true";
                                break;
                            default:
                                value = this.jp.getText();
                                break;
                        }
                        //depth--;
                        //System.out.println("field value " + value);
                        for (ParserListener listener : listeners) {
                            listener.start(nameStack.peek(), false, false);
                        }
                        for (ParserListener listener : listeners) {
                            listener.start(value, false, true);
                        }
                        for (ParserListener listener : listeners) {
                            listener.end(false, true);
                        }
                        for (ParserListener listener : listeners) {
                            if (depth == 0) {
                                listener.endDocument();
                            } else {
                                listener.end(false, false);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            if (depth > 0) {
                for (ParserListener listener : listeners) {
                    listener.endDocument();
                }
            }

        } catch (IOException e) {
            Logger.getLogger(JSONParseTask.class.getName()).log(Level.SEVERE, null, e);
            System.err.println(" exception in JSON parsing " + e);
            System.exit(-1);
        }

        return count;
    }
    private void innerParseOld() {
        /*
    }
                   while ((t = jp.nextToken()) != null) {
                switch (t) {
                    case START_OBJECT:
                        xg.writeStartObject();
                        break;
                    case END_OBJECT:
                        xg.writeEndObject();
                        break;
                    case START_ARRAY:
                        xg.writeStartArray();
                        break;
                    case END_ARRAY:
                        xg.writeEndArray();
                        break;
                    case FIELD_NAME:
                        xg.writeFieldName(jp.getCurrentName());
                        break;
                    case VALUE_STRING:
                        xg.writeString(jp.getText());
                        break;
                    case VALUE_FALSE:
                        xg.writeBoolean(false);
                        break;
                    case VALUE_TRUE:
                        xg.writeBoolean(true);
                        break;
                    case VALUE_NULL:
                        xg.writeString("");
                    // some tokens missing here
                        break;
                    default:
                        break;
                }
                */
        try {
            /*
            JsonFactory jf = new JsonFactory();
            JsonParser jp = jf.createParser("foo");
            XmlFactory xf = new XmlFactory();
            //DataOutput out;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JsonGenerator xg = xf.createGenerator(out);

            xg.writeFieldName("root"); // need a root element

            */
            
            JsonToken t;
            // initializing
            for (ParserListener listener : listeners) {
                listener.startDocument();
            }
            Stack<String> nameStack = new Stack();
            Stack<Boolean> arrayStack = new Stack();
            //String currentName = "root";
            nameStack.push("root");
            arrayStack.push(false);
            // Introduce a root element
            for (ParserListener listener : listeners) {
                listener.start("root", false, false);
            }
            int depth = 0;
            boolean previousIsFieldName = false;
            while ((t = this.jp.nextToken()) != null) {
                System.out.println("depht is " + depth);
                switch (t) {
                    case START_OBJECT:
                        System.out.println("start object");
                        //nameStack.push(currentName);
                        if (arrayStack.peek()) {
                            System.out.println(" in context " + nameStack.peek());
                            if (previousIsFieldName) {
                                for (ParserListener listener : listeners) {
                                    listener.start(nameStack.peek(), false, false);
                                }
                            }
                        }
                        arrayStack.push(false);
                        depth++;
                        previousIsFieldName = false;
                        break;
                    case END_OBJECT:
                        System.out.println("end object");
                        depth--;
                        arrayStack.pop();
                        if (arrayStack.peek()) {
                            
                        }
                        for (ParserListener listener : listeners) {
                            if (depth == 0) {
                                listener.endDocument();
                            } else {
                                listener.end(false, false);
                            } 
                        }
                        //nameStack.pop();
                        previousIsFieldName = false;
                        break;
                    case START_ARRAY:
                        depth++;
                        System.out.println("start array");
                        if (previousIsFieldName) {
                            arrayStack.push(true);
                            nameStack.push(nameStack.peek());
                        }
                        //nameStack.push(currentName);
                        previousIsFieldName = false;
                        break;
                    case END_ARRAY:
                        depth--;
                        System.out.println("end array");
                        arrayStack.pop();
                        nameStack.pop();
                        //nameStack.pop();
                        for (ParserListener listener : listeners) {
                            if (depth == 0) {
                                listener.endDocument();
                            } else {
                                listener.end(false, false);
                            }
                        }
                        previousIsFieldName = false;
                        break;
                    case FIELD_NAME:
                        //depth++;
                        String name = this.jp.getCurrentName();
                        System.out.println("field name " + name);
                        nameStack.push(name);
                        //currentName = jp.getCurrentName();
                        //System.out.println("Start element " + reader.getLocalName());
                        // read the start tag of an element
                        for (ParserListener listener : listeners) {
                            listener.start(name, false, false);
                        }
                        previousIsFieldName = true;
                        break;
                    case VALUE_NUMBER_INT:
                    case VALUE_NUMBER_FLOAT:
                    case VALUE_STRING:
                        //depth--;
                        String text = this.jp.getText();
                        //String[] words = text.split("\\s+");
                        System.out.println("field value " + this.jp.getText());
                        //if (!reader.isWhiteSpace()) {
                        if (previousIsFieldName) {
                            nameStack.pop();
                        } else {
                            for (ParserListener listener : listeners) {
                                listener.start(nameStack.peek(), false, false);
                            }
                        }

                        //for (String word : words) {
                            for (ParserListener listener : listeners) {
                                listener.start(text, false, true);
                            }
                            for (ParserListener listener : listeners) {
                                listener.end(false, true);
                            }
                            for (ParserListener listener : listeners) {
                                if (depth == 0) {
                                    listener.endDocument();
                                } else {
                                    listener.end(false, false);
                                }
                            }
                        //}

                        //}
                        break;
                    case VALUE_FALSE:
                        //depth--;
                        nameStack.pop();
                        for (ParserListener listener : listeners) {
                            listener.start("false", false, true);
                        }
                        for (ParserListener listener : listeners) {
                            listener.end(false, true);
                        }
                        for (ParserListener listener : listeners) {
                            if (depth == 0) {
                                listener.endDocument();
                            } else {
                                listener.end(false, false);
                            }
                        }
                        break;
                    case VALUE_TRUE:
                        //depth--;
                        nameStack.pop();                        
                        for (ParserListener listener : listeners) {
                            listener.start("true", false, true);
                        }
                        for (ParserListener listener : listeners) {
                            listener.end(false, true);
                        }
                        for (ParserListener listener : listeners) {
                            if (depth == 0) {
                                listener.endDocument();
                            } else {
                                listener.end(false, false);
                            }
                        }
                        break;
                    case VALUE_NULL:
                        //depth--;
                        nameStack.pop();                        
                        for (ParserListener listener : listeners) {
                            listener.start("", false, true);
                        }
                        for (ParserListener listener : listeners) {
                            listener.end(false, true);
                        }
                        for (ParserListener listener : listeners) {
                            if (depth == 0) {
                                listener.endDocument();
                            } else {
                                listener.end(false, false);
                            }
                        }
                        // some tokens missing here
                        break;
                    default:
                        break;
                }
            }
            if (depth > 0) {
                for (ParserListener listener : listeners) {
                    listener.endDocument();
                }
            }

        } catch (IOException e) {
            Logger.getLogger(JSONParseTask.class.getName()).log(Level.SEVERE, null, e);
            System.err.println(" exception in JSON parsing " + e);
            System.exit(-1);
        }

    }

    public JSONParseTask(/*Main controller, */String old, Database db, File inputFile, int maxNodes) throws FileNotFoundException {
        //this.controller = controller;
        this.db = db;
        this.maxNodes = maxNodes;
        // check whether input file exists
        if (inputFile.exists()) {
            this.inputFile = inputFile;
        } else {
            System.out.println("Input file not found " + inputFile);
            throw new FileNotFoundException();
        }
        this.nodeIdBuilder = new NodeIdBuilder(new HistoryDLNFactory());

        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
            inputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
            inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);
            XML json = new XML();
            //PipedOutputStream out = new PipedOutputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            json.toXml(new FileInputStream(this.inputFile), out);
            //PipedInputStream in = new PipedInputStream(out);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            reader = inputFactory.createXMLStreamReader(
                       new BufferedReader(new InputStreamReader(in), 1024)
            );
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JSONParseTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(JSONParseTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(JSONParseTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JSONParseTask(/*Main controller, */Database db, NodeIdBuilder<DLNFactory> nodeBuilder, XMLStreamReader reader) {
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
        // System.out.println("JSON CurtParseTask Parsing...");
        int count = 0;
        try {
            long time1 = System.currentTimeMillis();
            count = innerParse();
            /*
            while (reader.hasNext()) {
                // read next node
                int event = reader.next();
                parse(event);
                count++;
                reader.close();
                if (this.maxNodes != 0 && count > this.maxNodes) break;
            }
            */
            long time2 = System.currentTimeMillis(); 
            time2 = time2 - time1;
            System.out.println("Time taken " + time2);
            System.out.println("Total nodes " + count);
            
        } catch (Exception ex) {
            Logger.getLogger(JSONParseTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The main parsing process which traverses XML nodes sequentially and
     * publish their data to the given listeners
     *
     * @param listeners
     */
    public void parse(int event) {
        //System.out.println("Parsing...");
        switch (event) {
            case XMLStreamConstants.START_DOCUMENT:
                // initializing
                for (ParserListener listener : listeners) {
                    listener.startDocument();
                }
                break;
            case XMLStreamConstants.START_ELEMENT:
                //System.out.println("Start element " + reader.getLocalName());
                // read the start tag of an element
                for (ParserListener listener : listeners) {
                    listener.start(reader.getLocalName(), false, false);
                }
                // read attributes (attributes are read before element value)
                for (int i = 0; i < reader.getAttributeCount(); i++) {
                    // attribute tag start
                                  //System.out.println("Start attribute " + reader.getAttributeLocalName(i) + " = " + reader.getAttributeValue(i));
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
        try {
            pathIndexBuilder = new PathIndexBuilder(db, new DLNFactory());
            nodeIndexBuilder = new NodeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            typeIndexBuilder = new TypeIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
            keywordIndexBuilder = new KeywordIndexBuilder(db, nodeIdBuilder, pathIndexBuilder);
        } catch (Exception e) {
            Logger.getLogger(JSONParseTask.class.getName()).log(Level.SEVERE, null, e);
        }
        listeners.add(nodeIdBuilder);
        listeners.add(pathIndexBuilder);
        listeners.add(nodeIndexBuilder);
        listeners.add(typeIndexBuilder);
        listeners.add(keywordIndexBuilder);
    }

    public void parseThisThing() {
        // System.out.println("CurtParseTask: Start parsing");
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
        // System.out.println("CurtParseTask: Start parsing");
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
