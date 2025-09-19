package messiah.parse;

import com.fasterxml.jackson.core.JsonFactory;
//import com.fasterxml.jackson.core.JsonGenerator;
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
//import com.fasterxml.jackson.dataformat.xml.XmlFactory;
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
import messiah.parse.ParsedTimetampIntervalGenerator;
import org.apache.wink.json4j.utils.XML;
import usu.dln.DLNFactory;
import usu.dln.HistoryDLNFactory;
import usu.temporal.Time;
import usu.temporal.TimeAndLevelList;
import usu.temporal.TimeItem;
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
        // Create the stack again if needed
        ParsedTimetampIntervalGenerator parsedTimestampGenerator = new ParsedTimetampIntervalGenerator(); 
        try {          
            JsonToken t;
            // initializing
            for (ParserListener listener : listeners) {
                listener.startDocument();
            }
            Stack<String> nameStack = new Stack();
            //Stack<Boolean> arrayStack = new Stack();
            Stack<Boolean> previousStack = new Stack(); // Previous thing is a field name
            nameStack.push("root");
            previousStack.push(true);
            //arrayStack.push(false);
            String name = ""; // temporary string name
            int depth = 0;
            while ((t = this.jp.nextToken()) != null) {
                //System.out.println("Namestack size " + nameStack.size());
                //System.out.println("Prevouis size " + previousStack.size());
                if (count % 100000 == 0) System.out.println("Number of nodes stored " + count);
                if (this.maxNodes != 0 && count > this.maxNodes) break;
                //System.out.println("depht is " + depth);
                switch (t) {
                    case START_OBJECT:
                        name = nameStack.peek();
                        //System.out.println("start object" + name + " " + previousStack.peek());
                        
                        // Check if the thing before was a field name, if so then this is an object associated
                        // with the field name
                        //if (!previousStack.peek()) {
                        //    previousStack.push(false);
                        //}
                        // Only process if not a temporal thing

                        //arrayStack.push(false);
                        depth++;
                        if (!(name.equals("#timestamp") || name.equals("#time"))) {
                            count++;
                            for (ParserListener listener : listeners) {
                                listener.start(nameStack.peek(), false, false);
                            }
                        }
                        break;
                    case END_OBJECT:

                        // Only process if not special temporal elements
                        name = nameStack.peek();
                        //System.out.println("end object " + nameStack.peek() + " " + previousStack.peek());
                        if (previousStack.peek()) {
                            // pop name stack as we concluded the object associated with this field
                            nameStack.pop();  // Pop unless in an array
                            previousStack.pop();
                        }
  

                        //System.out.println("Popping " + nameStack.peek());
                        depth--;
                        if (name.equals("#timestamp")) {
                            ParsedTimetampIntervalGenerator.popTime();
                        }
                        if (!(name.equals("#timestamp") || name.equals("#time"))) {
                            for (ParserListener listener : listeners) {
                                if (depth == 0) {
                                    listener.endDocument();
                                } else {
                                    listener.end(false, false);
                                }
                            }
                        }
                        break;
                    case START_ARRAY:
                        //System.out.println("start array " + nameStack.peek() + " " + previousStack.peek());
                        //if (!previousStack.peek()) {
                            // Array associated with field name
                            previousStack.push(false);
                        //}
                        depth++;
                        //arrayStack.push(true);
 
                        break;
                    case END_ARRAY:

                        depth--;
                        //arrayStack.pop();
                        previousStack.pop();
                        //System.out.println("end array " + nameStack.peek() + " " + previousStack.peek());

                        if (previousStack.peek()) {
                            nameStack.pop();
                            previousStack.pop();
                        } // Pop from the name stack at the end of any kind of value

                        //System.out.println("end array " + nameStack.peek());
                        break;
                    case FIELD_NAME:
                        //depth++;
                        count++;
                        String fieldName = this.jp.getCurrentName();
                        previousStack.push(true);
                        //System.out.println("Pushing " + fieldName);
                        nameStack.push(fieldName);
                        //arrayStack.push(false);
                        //currentName = jp.getCurrentName();
                        //System.out.println("Start element " + fieldName);
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
                        // Only process if not special temporal elements
                        //System.out.println("Value is " + value);
                        name = nameStack.peek();
                        
                        if (name.equals("#time")) {
                            // Let's parse the time
                            // First determine if multiple levels
                            TimeAndLevelList timeList = new TimeAndLevelList();
                            //System.out.println("value is " + value);
                            String[] splits = value.split("\\|");
                            for (String time: splits) {
                                // Now split each time by a ,
                                //System.out.println("#time is " + time);
                                String[] parts = time.split(",");
                                int maxLevel = 100;
                                int minLevel = 0;
                                boolean isMoved = false;
                                String timeString = parts[0];
                                if (parts.length > 1) {
                                    // Have a time and a level
                                    String[] levels = parts[0].split("-");
                                    minLevel = Integer.parseInt(levels[0]);
                                    maxLevel = Integer.parseInt(levels[1]);
                                    isMoved = true;
                                    timeString = parts[1];
                                }
                                // Parse the time
                                //System.out.println("Timestring is " + timeString);
                                if (timeString.contains("null")) {
                                    timeList.add(null, minLevel);     
                                } else {
                                    String[] timeParts = timeString.split("-");
                                    String s = timeParts[0];
                                    String e = timeParts[1];
                                    //System.out.println("Time is |" + s + "| |" + e + "|");
                                    Time ct = new Time(Integer.parseInt(s.trim()), Integer.parseInt(e.trim()));
                                    timeList.add(ct, minLevel);
                                }                             
                            }
                              
                            ParsedTimetampIntervalGenerator.pushTime(new TimeItem(timeList));
                            //ParsedTimetampIntervalGenerator.currentTime = new Time(start, end);
                        }
                        if (!(name.equals("#timestamp") || name.equals("#time"))) {
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
                        }
                        //System.out.println("Popping " + nameStack.peek());
                        if (previousStack.peek()) {
                            // If arrayStack is false, then there was a field for this value, so pop it 
                            nameStack.pop();
                            previousStack.pop();
                        }  // Always pop at the end of a value
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
