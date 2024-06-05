/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usu.dataModification;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import messiah.NodeInfo;
import messiah.database.Database;
import messiah.parse.RepresentationalParser;
import org.w3c.dom.DOMException;
import usu.NodeId;
import usu.dln.DLN;
import usu.dln.HistoryDLN;
import usu.temporal.Time;

/**
 *
 * @author Amani Shatnawi
 */
public class DataModification {

    private final Database db;
    RepresentationalParser tempParse;
    private final int maxTime = -1;
    public List<NodeId> ItemIdsList;
    boolean moveOp;
    SortedMap map;

    DataModification(Database dbp) {
        this.db = dbp;
        ItemIdsList = new ArrayList<>();
        moveOp = false;
    }
    
     public void fillMap(NodeId nodeId) {
        map = db.nodeIndex.tailMap(nodeId);
        //ITEMOUT map = db.historyNodeIndex.tailMap(nodeId);
    }

    private boolean isContains(NodeId nodeId) {
        if (map.isEmpty() && map == null) {
            fillMap(nodeId);
        }
        Set entrySet = map.entrySet();
        DLN[] entrySetArray = (DLN[]) entrySet.toArray();

       for (DLN entrySetObj : entrySetArray) {
            if (entrySetObj.equals(nodeId)) {
               nodeId = (NodeId)entrySetObj;
                return true;
            }
        }
        return false;
    }

    private int figureOutMaxTime(NodeId nodeId, int maxTime) {

        HistoryDLN dln;
        Time time;
        if (isContains(nodeId) && !((HistoryDLN)nodeId).getTime().isUntilChanged()) {
            dln = (HistoryDLN) (nodeId);
            time = dln.getTime();
            if (maxTime < time.getEndTime()) {
                maxTime = time.getEndTime();
            }

            for (NodeId child = nodeId.newChild();
                    isContains(child);
                    child = child.nextSibling()) {
                figureOutMaxTime(child, maxTime);

            }
        }
        return maxTime;
    }

    public boolean deleteNode(NodeId nodeId, int endTime) {
        boolean result = false;
        // Check that node exists
        if (isContains(nodeId) && ((HistoryDLN)nodeId).getTime().isUntilChanged()) {
            // set the time
            NodeInfo info = db.nodeIndex.get((DLN)nodeId);
            db.nodeIndex.remove(nodeId);
            Time t = info.getTime();
            t.setEnd(endTime);
            info.setTimestamp(t);
            db.nodeIndex.put(nodeId, info);
            result = true;
            // Walk over children
            for (NodeId child = nodeId.newChild();
                    db.nodeIndex.containsKey(child);
                    child = child.nextSibling()) {
                deleteNode(child, endTime);
            }
        }

        return result;
    }

    public boolean addNode(NodeId parentId, String inputFileName) throws FileNotFoundException {
        boolean result = false;
        tempParse = new RepresentationalParser(this.db, new File(inputFileName));
        if (moveOp) {
            tempParse.setMoveOp(ItemIdsList);
        }
        if (isContains(parentId)) {
            NodeId itemId;
            //ITEMOUT itemId = db.historyIdToItemIdIndex.get(parentId);
            //ITEMOUT tempParse.setNodeIdValues(itemId, parentId);

            tempParse.startParser();
            moveOp=false;
            result = true;
        }

        return result;
    }

    public boolean moveNode(NodeId parentId, NodeId nodeId) throws FileNotFoundException, XMLStreamException, IOException {
        boolean deleteNodeTobeMoved = false;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            FileOutputStream fos = new FileOutputStream(new File("input.xml"));

            XMLOutputFactory factory = XMLOutputFactory.newFactory();
            XMLStreamWriter writer = factory.createXMLStreamWriter(new OutputStreamWriter(baos));
            writer.writeStartDocument();
            deleteNodeTobeMoved = deleteNodeTobeMoved(nodeId, figureOutMaxTime(nodeId, maxTime), writer, baos, fos);
            writer.writeEndDocument();
            writer.flush();
            baos.writeTo(fos);
            baos.reset();
            baos.close();
            writer.close();
        } catch (DOMException ex) {
        }

        // Add node
        boolean addNod = addNode(parentId, "input.xml");

        return (deleteNodeTobeMoved || addNod);
    }

    private boolean deleteNodeTobeMoved(NodeId nodeId, int endTime, XMLStreamWriter writer, ByteArrayOutputStream baos, FileOutputStream fos) throws XMLStreamException, IOException {
        boolean result = false;
        // Check that node exists
        if (db.nodeIndex.containsKey(nodeId)) {
            //add item id to list
            //ITEMOUT NodeId nId = db.historyIdToItemIdIndex.get(nodeId);
            //ITEMOUT boolean add = ItemIdsList.add(nId);
            // set the time
            NodeInfo info = db.nodeIndex.get(nodeId);
            db.nodeIndex.remove(nodeId);
            Time t = ((HistoryDLN) nodeId).getTime();
            if (((HistoryDLN)nodeId).getTime().isUntilChanged()) {
                t.setEnd(endTime);
            }
            HistoryDLN dln = new HistoryDLN((DLN) nodeId, t);
            nodeId = (NodeId) dln;
            db.nodeIndex.put(nodeId, info);
            writer.writeStartElement("m:item");
            writer.writeStartElement("m:version");
            writer.writeStartElement("m:timestamp");
            writer.writeCharacters(Integer.toString(endTime) + "-uc");
            writer.writeEndElement();
            writer.writeStartElement("m:content");
            writer.writeStartElement(info.getData());
            result = true;
            // Walk over children
            for (NodeId child = nodeId.newChild();
                    db.nodeIndex.containsKey(child);
                    child = child.nextSibling()) {
                deleteNode(child, endTime);
                writer.writeEndElement();
                writer.writeEndElement();
                writer.writeEndElement();
                writer.writeEndElement();
                writer.flush();
                baos.writeTo(fos);
                baos.reset();

            }
        }

        return result;
    }

}

