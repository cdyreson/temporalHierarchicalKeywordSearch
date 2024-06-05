package messiah.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import messiah.NodeInfo;
import messiah.database.Database;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import usu.NodeId;
import usu.dln.DLN;
import usu.dln.HistoryDLN;
import usu.dln.HistoryDLNFactory;
import usu.temporal.Time;

/**
 *
 * @author Amani Shatnawi
 */
public class RepresentationalParser {

    private final Database db;
    private final String META_NS = "m";
    private final ArrayList<ParserListener> listeners;
    private NodeIdBuilder<HistoryDLNFactory> historyNodeIdBuilder;
    XMLStreamReader reader;

    private final HistoryParseTask parser;
    private NodeId itemId;
    private Time time;
    boolean moveOp;
    List<NodeId> itemIds;

    public RepresentationalParser(Database db, File parsedFile) {
        this.moveOp = false;
        this.db = db;
        this.historyNodeIdBuilder = new NodeIdBuilder(new HistoryDLNFactory());
        try {
            // create the reader for reading XML document
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
            inputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
            inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);
            XMLStreamReader reader;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setValidating(false);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            FileInputStream file = new FileInputStream(parsedFile);
            InputStreamReader readerStream = new InputStreamReader(file);
            BufferedReader buff = new BufferedReader(readerStream, 1024);
            reader = inputFactory.createXMLStreamReader(buff);
        } catch (IllegalArgumentException | ParserConfigurationException | FileNotFoundException | XMLStreamException | DOMException ex) {
            System.out.print("Exception" + ex.toString());
        }
        this.parser = new HistoryParseTask(db, historyNodeIdBuilder, reader);
        this.time = new Time();
        this.listeners = new ArrayList<>();
    }

    public void setMoveOp(List<NodeId> list) {
        this.moveOp = true;
        this.itemIds = list;
    }

    public void setNodeIdValues(NodeId itemId, NodeId nodeId) {
        this.historyNodeIdBuilder.setId(nodeId);

    }

    public void startParser() {
        try {
            // create the reader for reading XML document

            while (reader.hasNext()) {
                // read next node
                int event = reader.next();

                // counter++;
                switch (event) {
                    case XMLStreamConstants.START_DOCUMENT:
                        // initializing
                        System.out.println("start..... ");
                        break;
                    case XMLStreamConstants.START_ELEMENT:
                        if (reader.getPrefix().equalsIgnoreCase(META_NS)) {
                            if (reader.getLocalName().equalsIgnoreCase("item")) {
                                for (ParserListener listener : listeners) {
                                    listener.start(reader.getLocalName(), false, false);
                                }
                            }

                            reader.next();
                            if (reader.getLocalName().equalsIgnoreCase("version")) {
                                reader.next();
                            }
                            if (reader.getLocalName().equalsIgnoreCase("timestamp")) {
                                reader.next();
                                time = new Time(reader.getText());
                            }

                        } else {
                            parser.parse(event);
                            addTime();
                            indexBuilder();
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        parser.parse(event);
                        addTime();
                        indexBuilder();
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        if (reader.getLocalName().equalsIgnoreCase("/item")
                                || reader.getLocalName().equalsIgnoreCase("/version")
                                || reader.getLocalName().equalsIgnoreCase("/content")
                                || reader.getLocalName().equalsIgnoreCase("/timestamp")) {
                            continue;
                        } else {
                            parser.parse(event);
                            addTime();
                            indexBuilder();
                        }
                        System.out.println("------------ end element -------------");
                        break;

                    default:
                        break;
                }
            }
            reader.close();

        } catch (IllegalArgumentException | XMLStreamException | DOMException ex) {
            System.out.print("Exception" + ex.toString());

        }
    }

    private void addTime() {
        NodeId id = historyNodeIdBuilder.getNodeId();

        /* Curt modified */
        /* Curt do we use, need to modify if representation parser ever used
        if (db.nodeIndex.containsKey(id)) {
            NodeInfo info = db.nodeIndex.get(id);
            db.nodeIndex.remove(id);
            HistoryDLN dln = new HistoryDLN((DLN) id, time);
            id = (NodeId) dln;
            db.nodeIndex.put(id, info);
        }
        */
    }

    public void createListeners() {
        parser.createListeners();
        //ITEMOUT this.itemNodeIdBuilder = new NodeIdBuilder(new ItemDLNFactory());
        //ITEMOUT this.itemIndexBuilder = new ItemNodeIndexBuilder(db, itemNodeIdBuilder, null);
        //ITEMOUT listeners.add(itemNodeIdBuilder);
        //ITEMOUT listeners.add(itemIndexBuilder);

    }

    private void indexBuilder() {
        //ITEMOUT db.historyIdToItemIdIndex.put(historyNodeIdBuilder.getNodeId(), itemId);
        List<NodeId> listOfHistoryNodes = new ArrayList<>();
        listOfHistoryNodes.add(historyNodeIdBuilder.getNodeId());
        //ITEMOUTdb.itemIdToHistoryIdIndex.put(itemId, listOfHistoryNodes);
        if (moveOp) {
            NodeId item_id = itemIds.get(0);
            //ITEMOUTif (db.itemNodeIndex.containsKey(item_id)) {
                //ITEMOUTList<NodeId> list = db.itemIdToHistoryIdIndex.get(item_id);
                //ITEMOUTlist.add(historyNodeIdBuilder.getNodeId());
                //ITEMOUTdb.itemIdToHistoryIdIndex.remove(item_id);
                //ITEMOUTdb.itemIdToHistoryIdIndex.put(item_id, list);
            //ITEMOUT}
        }

    }
}
