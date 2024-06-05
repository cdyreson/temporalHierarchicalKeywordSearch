package messiah.database.memory;

import java.util.*;
import messiah.KeywordInfo;
import messiah.database.MapWithDuplicates;
import usu.NodeId;
import messiah.NodeInfo;
import usu.PathId;
import messiah.PathInfo;

/**
 * The Database class provides operators on the underlying database, where the
 * database is assumed to be in memory (transient rather than persistent). In
 * this implementation, HashMap-based tables are used.
 *
 * @author Curtis
 */
public class Database extends messiah.database.Database {

    private messiah.database.berkeleydb.Database diskDB;
    protected Map<String, Map> openDbs = new HashMap();
    private boolean partialOpen = false;
    private boolean isTemporal = false;

    public Database(boolean readOnly, boolean isTemporal) {
        this.isTemporal = isTemporal;
        openDatabase();
    }

    /* Open the named schema, it is in a directory off of the 
     * database home directory.
     * Call this once to open the database.
     */
    public Database(String name, boolean readOnly, boolean isTemporal) {
        databaseName = name;
        this.isTemporal = isTemporal;
        openDatabase(databaseName);
    }

    /*
     * Open all of the database tables. Call this once to open the database.
     * @param - name of database (ignored for in-memory)
     */
    @Override
    public void openDatabase(String name) {
        // Ignore name
        openDatabase();
    }

    /*
     * Open all of the database tables. Call this once to open the database.
     */
    @Override
    public void openDatabase() {
        if (!alreadyOpen) {
            alreadyOpen = true;
            openDbs = new Hashtable();

            // Internal database table
            metadataTable = openTable("metadata", String.class, Integer.class);

            // Tables that help sequenced, nonsequenced, snapshot search
            // Note, we currently don't care about schema evolution
            pathNameIndex = openTable("pathNameIndex", String.class, PathId.class);
            pathIndex = openTable("pathIndex", PathId.class, PathInfo.class);
            typeIndex = openTable("typeIndex", PathId.class, TreeSet.class);
            keywordIndex = openTable("keywordIndex", String.class, KeywordInfo.class);
            keywordPathsIndex = openTable("keywordPathsIndex", String.class, Set.class);
            keywordNodesIndex = openTable("keywordNodesIndex", String.class, Map.class);
            
            // Snapshot document tables
            nodeIndex = openSortedTable("nodeIndex", NodeId.class, NodeInfo.class);

        }
    }

    /*
     * Copy some of the database tables to the BerkeleyDB database tables for
     * storage.  Used to manage the growing size of the BerkeleyDB tables.  Recreates
     * those tables.
     */
    public void cacheDiskDBHandle(messiah.database.berkeleydb.Database diskDB) {
        this.diskDB = diskDB;
    }

    public void printSizes() {
        System.out.println("metadataTable size " + metadataTable.size());

            // Tables that help sequenced, nonsequenced, snapshot search
        // Note, we currently don't care about schema evolution
        System.out.println("pathNameIndex size " + pathNameIndex.size());
        System.out.println("pathIndex size " + pathIndex.size());
        System.out.println("typeIndex size " + typeIndex.size());
        System.out.println("keywordIndex size " + keywordIndex.size());
        System.out.println("keywordPathsIndex size " + keywordPathsIndex.size());
        System.out.println("keywordNodesIndex size " + keywordNodesIndex.size());

        // Snapshot document tables
        System.out.println("nodeIndex size " + nodeIndex.size());
    }
    /*
     * Copy some of the database tables to the BerkeleyDB database tables for
     * storage.  Used to manage the growing size of the BerkeleyDB tables.  Recreates
     * those tables.
     */
    public void copyPartialDatabaseToDisk() {
        System.out.println("Start partial copy");
        printSizes();
        diskDB.keywordIndex.putAll(keywordIndex);
        diskDB.keywordNodesIndex.putAll(keywordNodesIndex);
        diskDB.keywordPathsIndex.putAll(keywordPathsIndex);
        diskDB.nodeIndex.putAll(nodeIndex);
        keywordIndex = null;
        keywordPathsIndex = null;
        keywordNodesIndex = null;
        nodeIndex = null;
        //System.gc();
        keywordIndex = openTable("keywordIndex", String.class, KeywordInfo.class);
        keywordPathsIndex = openTable("keywordPathsIndex", String.class, Set.class);
        keywordNodesIndex = openTable("keywordNodesIndex", String.class, Map.class);
        nodeIndex = openSortedTable("nodeIndex", NodeId.class, NodeInfo.class);
        System.out.println("End partial copy -- 1000000 elements done");
        if (false) {
            diskDB.closeDatabase();

            String s = diskDB.getName();
            diskDB = null;
            diskDB = new messiah.database.berkeleydb.Database(s,false,isTemporal);
            diskDB.openSplitTables(false);
            partialOpen = true;
        }
    }
    
    /*
     * Copy all of the database tables to the BerkeleyDB database tables for
     * storage.
     */
    
    public void copyDatabaseToDisk() {
        if (partialOpen) {
            diskDB.openNonSplitTables(false);
        }
        System.out.println("Copying to disk " + diskDB + " " + this);

        printSizes();
        diskDB.keywordNodesIndex.putAll(keywordNodesIndex);
        keywordNodesIndex = null;
        //System.gc();
        diskDB.keywordPathsIndex.putAll(keywordPathsIndex);
        System.out.println("NodeIndex start copy to disk");
        diskDB.nodeIndex.putAll(nodeIndex);
        System.out.println("NodeIndex copied to disk");
        keywordPathsIndex = null;
        nodeIndex = null;
        //System.gc();
        System.out.println("metadata copy");
        diskDB.metadataTable.putAll(metadataTable);
        System.out.println("termindex copy");
        System.out.println("termNodeIndex copy");
        //diskDB.termNodeIndex.putAll(termNodeIndex);
        System.out.println("pathName copy");
        diskDB.pathNameIndex.putAll(pathNameIndex);
        System.out.println("pathIndex copy");

        diskDB.pathIndex.putAll(pathIndex);
        System.out.println("typeINdex copy");
        diskDB.typeIndex.putAll(typeIndex);
        System.out.println("keywordIndex copy");
        diskDB.keywordIndex.putAll(keywordIndex);
        System.out.println("Done Copying to disk");
        //ITEMOUT diskDB.itemNodeIndex.putAll(itemNodeIndex);
        //ITEMOUT diskDB.historyNodeIndex.putAll(historyNodeIndex);
        //ITEMOUT diskDB.historyIdToItemIdIndex.putAll(historyIdToItemIdIndex);
        //ITEMOUT diskDB.itemIdToHistoryIdIndex.putAll(itemIdToHistoryIdIndex);
        //diskDB.node2Index.putAll(node2Index);
        //ITEMOUT diskDB.historyToItemIndex.putAll(historyToItemIndex);
        //ITEMOUT diskDB.itemToHistoryIndex.putAll(itemToHistoryIndex);
        //diskDB.itemMetaDataIndex.putAll(itemMetaDataIndex);
        //diskDB.metaDataIndex.putAll(metaDataIndex);
    }

    /*
     * This method commits the data tables, and the database environment.
     * For the in-memory implementation it does nothing.
     */
    @Override
    public void commitDatabase() {
    }

    /*
     * This method closes all of the open tables, and the database environment.
     * For the in-memory implementation it does nothing.
     */
    @Override
    public void closeDatabase() {
        //System.out.println("closing db " + alreadyOpen);
    }

    /*
     * Open an iterator for a table. @param String name - Table name @param
     * EntryBinding e - BerekeleyDB class binding @param Class c - Java class
     * info, BerekeleyDB binding will be created. @returns TableIterator -
     * constructed iterator
     */
    @Override
    public Iterator openIterator(String name, Class a, Class c) {
        if (!openDbs.containsKey(name)) {
            openTable(name, a, c);
        }
        TreeMap t = (TreeMap) openDbs.get(name);
        return t.navigableKeySet().iterator();
    }

    /*
     * Open a table. @param String name - Table name @param Class b for key -
     * ignored @param Class c - Java class info for data, ignored @returns Map -
     * constructed Map
     */
    @Override
    public MapWithDuplicates openTableWithDuplicates(String name, Class e, Class c) {
        return new HashMapWithDuplicates();
    }

    /*
     * Open a table. @param String name - Table name @param Class b for key -
     * ignored @param Class c - Java class info for data, ignored @returns Map -
     * constructed Map
     */
    @Override
    public SortedMap openSortedTable(String name, Class e, Class c) {
        //System.out.println("opening sorted " + name);
        SortedMap h = new TreeMap();
        openDbs.put(name, h);
        return h;
    }

    /*
     * Open a table. @param String name - Table name @param Class b for key -
     * ignored @param Class c - Java class info for data, ignored @returns Map -
     * constructed Map
     */
    @Override
    public Map openTable(String name, Class e, Class c) {
        //System.out.println("opening " + name);
        HashMap h = new HashMap();
        openDbs.put(name, h);
        return h;
    }
}
