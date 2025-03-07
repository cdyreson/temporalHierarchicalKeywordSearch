package messiah.database.berkeleydb;

// Standard java packages includes
import java.util.*;
import java.io.*;

import messiah.Config;

import messiah.KeywordInfo;
// import usu.dln.HistoryDLNNodeIdBinding;
import messiah.NodeInfo;
import usu.NodeId;
import usu.PathId;
import usu.dln.DLNNodeIdBinding;

/**
 * The MegeSplitTables class merges tables that have been split.
 *
 * @author Curtis
 */
public class MergeSplitTables extends Database {

    public MergeSplitTables(boolean isReadOnly, boolean isTemporal) {
        super(isReadOnly, isTemporal);
        openNewTables();
    }

    /* 
     Constructor, opens the database as well
     */
    public MergeSplitTables(String name, boolean isReadOnly, boolean isTemporal) {
        super(name, isReadOnly, isTemporal);
        openNewTables();
    }

    /*
     * Merge all of the partial tables.
     */
    public void openNewTables() {

        Map<String, KeywordInfo> keywordIndex2;
        Map<String, Set<PathId>> keywordPathsIndex2;
        Map<String, Map<PathId, List<NodeId>>> keywordNodesIndex2;
        SortedMap<NodeId, NodeInfo> nodeIndex2;

        keywordIndex2 = openTable("keywordIndex", String.class, KeywordInfo.class);
        keywordPathsIndex2 = openTable("keywordPathsIndex", String.class, Set.class);
        keywordNodesIndex2 = openTable("keywordNodesIndex", String.class, Map.class);
        nodeIndex2 = openSortedTable("nodeIndex", new DLNNodeIdBinding(), NodeInfo.class);
        ((SplitCachedMap) keywordIndex).mergeTable(keywordIndex2);
        ((SplitCachedMap) keywordPathsIndex).mergeTable(keywordPathsIndex2);
        ((SplitCachedMap) keywordNodesIndex).mergeTable(keywordNodesIndex2);
        ((SplitCachedSortedMap) nodeIndex).mergeTable(nodeIndex2);
        System.out.println("Tables merged");
        this.closeDatabase();

    }

    public static void main(String[] args) {
        System.out.println("opening " + Config.DB_FOLDER_STRING + args[0]);
        MergeSplitTables temp = new MergeSplitTables(Config.DB_FOLDER_STRING + args[0], false, false);
        temp.closeDatabase();
    }

}
