package messiah.parse.generic;

import messiah.parse.*;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import messiah.Config;
import messiah.TermNodeKey;
import messiah.utils.Stopwatch;
import messiah.utils.TimedMsg;
import usu.NodeId;
import usu.PathId;
import messiah.database.Database;


/**
 * The builder to build the {@link TermNodeIndex}.
 * @author truongbaquan
 */
public class TermNodeIndexBuilder implements ParserListener {

    private Database db;
    private final NodeIdBuilder nodeIdBuilder;
    private final PathIndexBuilder pathIndexBuilder;
    
    StopwordFilter stopwordFilter = new StopwordFilter();
    
    public TermNodeIndexBuilder(Database db, NodeIdBuilder nodeIdBuilder, PathIndexBuilder pathIndexBuilder) {
        this.db = db;
        this.nodeIdBuilder = nodeIdBuilder;
        this.pathIndexBuilder = pathIndexBuilder;
    }
        
    @Override
    public void startDocument() {}

    @Override
    public void endDocument() {
    }

    @Override
    public void start(String str, boolean isAttribute, boolean isValue) {
        String[] tokens;
        if (isValue) // data value
            tokens = str.split(Config.TOKENS_DELIMITER);
        else // element/attribute label
            tokens = new String[] {str}; // no need to split        
        
        for (String token : tokens) {
            if (token == null) continue;
            token = token.trim().toLowerCase();
            if (!token.isEmpty() && // not empty token
                    (!isValue || !stopwordFilter.isStopword(token))) { // label token or non-stopword value token
                this.store(token, nodeIdBuilder.getNodeId(), pathIndexBuilder.getCurrentPathId());
            }
        }
    }

    @Override
    public void end(boolean isAttribute, boolean isValue) {} // nothing to do


        public void store(String term, NodeId nodeId, PathId pathId) {
            TermNodeKey key = new TermNodeKey(term, nodeId);
            db.termNodeIndex.put(key, pathId);
        }

}
