package messiah.parse.generic;

import messiah.parse.*;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import messiah.Path;
import messiah.utils.Counter;
import messiah.utils.Stopwatch;
import usu.NodeId;
import usu.PathId;
import messiah.database.Database;

/**
 * This object is in charge of building the type index
 *
 * @author John Truong Ba Quan
 */
public class TypeIndexBuilder implements ParserListener {

    private final NodeIdBuilder nodeIdBuilder;
    private final PathIndexBuilder pathIndexBuilder;
    private final Database db;
    private boolean processingValue = false; // indicating whether the current node is value or not

    public TypeIndexBuilder(Database db, NodeIdBuilder nodeIdBuilder, PathIndexBuilder pathIndexBuilder) {
        this.nodeIdBuilder = nodeIdBuilder;
                this.pathIndexBuilder = pathIndexBuilder;
        this.db = db;
    }

    @Override
    public void startDocument() {
    }

    @Override
    public void endDocument() {
    }

    @Override
    public void start(String str, boolean isAttribute, boolean isValue) {
        if (!isValue) {

            PathId pathId = pathIndexBuilder.getCurrentPathId();
            //System.out.println("Curt: pathTrackerId " + pathId + " " + pathId.debug() + " " + pathId.getClass());
            if (!db.typeIndex.containsKey(pathId)) {
                db.typeIndex.put(pathId, new TreeSet<>());
            }
            TreeSet<NodeId> nodeIds = db.typeIndex.get(pathId);
            nodeIds.add(this.nodeIdBuilder.getNodeId());
            //System.out.println("Curt: pathTrackerId " + nodeIds.size());
            db.typeIndex.put(pathId, nodeIds);
        } else {
            processingValue = true;
        }
    }

    @Override
    public void end(boolean isAttribute, boolean isValue) {
        if (processingValue) {
            processingValue = false;
        }
    }

}
