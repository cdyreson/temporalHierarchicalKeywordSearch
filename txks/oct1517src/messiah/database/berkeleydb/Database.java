package messiah.database.berkeleydb;

// Standard java packages includes
import java.util.*;
import java.io.*;

// BerkeleyDB includes
import com.sleepycat.je.Environment;
import com.sleepycat.je.CheckpointConfig;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.DatabaseConfig;
//imports for BerkleyDb bindings 
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.EntityBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.Durability;
import messiah.Config;
//import com.sleepycat.je.StatsConfig;

import messiah.KeywordInfo;
import messiah.database.MapWithDuplicates;
import messiah.database.DBError;
//import usu.dln.DLNNodeIdBinding;
import usu.dln.HistoryDLNNodeIdBinding;
import messiah.NodeInfo;
import messiah.TermNodeKey;
import usu.NodeId;
import usu.PathId;
//import usu.metaData.NodeMetaData;
import messiah.PathInfo;
import usu.dln.DLN;
import usu.dln.DLNFactory;
import usu.dln.DLNNodeIdBinding;
import usu.dln.HistoryDLN;
import usu.dln.HistoryDLNFactory;
import usu.level.Branch;
import usu.level.PrefixPair;
import usu.temporal.Time;
//import usu.dln.HistoryDLN;

/**
 * The Database class provides operators on the underlying database. In this
 * implementation, BerkeleyDB tables are used. Eventually this class will become
 * abstract and implemented by several database products.
 *
 * @author Curtis
 */
public class Database extends messiah.database.Database {

    /*
     * BerkeleyDB environment and database variables
     */
    protected Environment myEnv;
    protected DatabaseConfig myDbConfig;
    protected StoredClassCatalog javaCatalog;
    protected com.sleepycat.je.Database catalogDb;

    // Name of the catalog internal to the database
    protected final static String javaCatalogName = "catalog";
    protected boolean isReadOnly;
    protected boolean isTemporal;

    /*
     * File handles of all of the open databases, so they can be closed, and
     * opened only once.
     */
    protected Map<String, com.sleepycat.je.Database> openDbs;

    public Database(boolean isReadOnly, boolean isTemporal) {
        databaseName = Config.DB_FOLDER_STRING;
        this.isReadOnly = isReadOnly;
        this.isTemporal = isTemporal;
        this.openDatabase(databaseName);
    }

    /* Open the named schema, it is in a directory off of the 
     * database home directory.
     * Call this once to open the database.
     */
    public Database(String name, boolean isReadOnly, boolean isTemporal) {
        //System.out.println("opening berkeleydb " + isReadOnly);
        databaseName = name;
        this.isReadOnly = isReadOnly;
        this.isTemporal = isTemporal;
        this.openDatabase(databaseName);
    }

    public String getName() {
        return databaseName;
    }

    /*
     * Open all of the database tables. Call this once to open the database.
     */
    private void openTables(boolean isReadOnly) {
        if (!alreadyOpen) {
            openNonSplitTables(isReadOnly);
            openSplitTables(isReadOnly);
        }
    }

    /*
     * Open all of the database tables. Call this once to open the database.
     */
    public void commitCachedTables() {
        commitSplitTables();
        commitNonSplitTables();
    }

    /*
     * Open all of the database tables. Call this once to open the database.
     */
    public void commitSplitTables() {
        if (!alreadyOpen) {
            //System.out.println("berkeley.database: committing cache tables");
            if (isReadOnly) {
            } else {
                //System.out.println("doing metadataTable");
                CachedMap x = (CachedMap) metadataTable;
                x.commit();
                //System.out.println("doing keywordIndex");
                SplitCachedMap c = (SplitCachedMap) keywordIndex;
                c.commit();
                //System.out.println("doing keywordPathsIndex");
                c = (SplitCachedMap) keywordPathsIndex;
                c.commit();
                //System.out.println("doing keywordNodesIndex");
                c = (SplitCachedMap) keywordNodesIndex;
                c.commit();

                //System.out.println("doing branchIndex");
                c = (SplitCachedMap) branchIndex;
                c.commit();

                //System.out.println("doing prefixIndex");
                c = (SplitCachedMap) prefixIndex;
                c.commit();
                //System.out.println("doing nodesIndex");
                SplitCachedSortedMap d = (SplitCachedSortedMap) nodeIndex;
                d.commit();
            }

        }
    }

    /*
     * Close the split tables if needed
     */
 /*
    public void closeSplitTables() {
        Map<String, KeywordInfo> keywordIndex2 = openTable("keywordIndex", String.class, KeywordInfo.class);
        Map<String, Set<PathId>> keywordPathsIndex2 = openTable("keywordPathsIndex", String.class, Set.class);
        Map keywordNodesIndex2 = openTable("keywordNodesIndex", String.class, Map.class);
        Map branchIndex2 = openTable("branchIndex", String.class, List.class);
        Map prefixIndex2 = openTable("prefixIndex", String.class, List.class);
        SortedMap nodeIndex2 = openSortedTable((isTemporal)? "temporalNodeIndex" : "nodeIndex", (isTemporal)? new HistoryDLNNodeIdBinding() : new DLNNodeIdBinding(), NodeInfo.class);
  

        System.out.println("merge keywordIndex");
        ((SplitMap) keywordIndex).mergeTable(keywordIndex2);
        System.out.println("merge keywordPathsIndex");
        ((SplitMap) keywordPathsIndex).mergeTable(keywordPathsIndex2);
        System.out.println("merge keywordNodesIndex");
        ((SplitMap) keywordNodesIndex).mergeTable(keywordNodesIndex2);
        System.out.println("merge branchIndex");
        ((SplitMap) branchIndex).mergeTable(branchIndex2);
        System.out.println("merge prefixIndex");
        ((SplitMap) prefixIndex).mergeTable(prefixIndex2);
        System.out.println("merge nodeIndex");
        ((SortedSplitMap) nodeIndex).mergeTable(nodeIndex2);

    }
     */
    
    private void visitTreeUpdateTimestamp(DLN nodeId, Database temporalDB, int rangeMax, int intervalMax, int maxBegin, int maxEnd) {
        if (nodeIndex.containsKey(nodeId)) {
            NodeInfo nodeInfo = nodeIndex.get(nodeId);
            
        }
    }
    
    /*
     * Copy this nonTemporal database to a temporal database by adding timestamps
     * and converting DLNs to TemporalDLNs.
     * @param - name of the database
     */
    public void copyNonTemporaltoTemporalDatabase(Database temporalDB, int rangeMax, int intervalMax) {
        Random randomRange = new Random();
        Random randomInterval = new Random();
        DLNFactory factory = new DLNFactory();
        HistoryDLNFactory historyFactory = new HistoryDLNFactory();
        NodeId rnodeId = factory.rootId();
        NodeId historyNodeId = historyFactory.rootId();
        /*
        while (true) {
            // Update the information for this node
            NodeInfo nodeInfo = nodeIndex.get(nodeId);
            temporalDB.
            
        }
        */
        
        for (String key : keywordNodesIndex.keySet()) {
            Map<PathId, List<NodeId>> map = keywordNodesIndex.get(key);
            for (PathId pathId : map.keySet()) {
                List<NodeId> a = map.get(pathId);
                List<NodeId> b = new ArrayList();
                for (NodeId nodeId : a) {
                    int begin = randomRange.nextInt(rangeMax);
                    int end = Math.min(begin, begin + randomInterval.nextInt(intervalMax));
                    NodeId temporalNodeId = new HistoryDLN((DLN) nodeId, new Time(begin, end));
                    b.add(temporalNodeId);
                    // Update the node index as well
                    NodeInfo nodeInfo = nodeIndex.get(nodeId);
                    temporalDB.nodeIndex.put(temporalNodeId, nodeInfo);
                }
            }
        }
        temporalDB.keywordIndex.putAll(keywordIndex);
        temporalDB.keywordPathsIndex.putAll(keywordPathsIndex);
        temporalDB.branchIndex.putAll(branchIndex);
        temporalDB.prefixIndex.putAll(prefixIndex);
        temporalDB.termIndex.putAll(termIndex);
        temporalDB.termNodeIndex.putAll(termNodeIndex);
        temporalDB.pathNameIndex.putAll(pathNameIndex);
        temporalDB.typeIndex.putAll(typeIndex);
        temporalDB.pathIndex.putAll(pathIndex);
    }

    /*
     * Open all of the database tables. Call this once to open the database.
     */
    public void openSplitTables(boolean isReadOnly) {
        if (!alreadyOpen) {
            if (isReadOnly) {
                metadataTable = openTable("metadata", String.class, Integer.class);

                /*
                 keywordIndex = openTable("keywordIndex", String.class, KeywordInfo.class);
                 keywordPathsIndex = openTable("keywordPathsIndex", String.class, Set.class);
                 keywordNodesIndex = openTable("keywordNodesIndex", String.class, Map.class);
                 nodeIndex = openSortedTable("nodeIndex", new HistoryDLNNodeIdBinding(), NodeInfo.class);
                 */
                keywordIndex = new SplitCachedMap<String, KeywordInfo>(Config.KEYWORD_INDEX_SPLIT_TABLE_SIZE, true, this, "keywordIndex", String.class, KeywordInfo.class);
                keywordPathsIndex = new SplitCachedMapSetValue<String, Set<PathId>>(Config.KEYWORD_INDEX_SPLIT_TABLE_SIZE, true, this, "keywordPathsIndex", String.class, Set.class);
                keywordNodesIndex = new SplitCachedMapKeywordNodeIndex(Config.KEYWORD_INDEX_SPLIT_TABLE_SIZE, true, this, "keywordNodesIndex", String.class, Map.class);
                branchIndex = new SplitCachedMap<String, List<Branch>>(Config.KEYWORD_INDEX_SPLIT_TABLE_SIZE, true, this, "branchIndex", String.class, List.class);
                prefixIndex = new SplitCachedMap<String, List<PrefixPair>>(Config.KEYWORD_INDEX_SPLIT_TABLE_SIZE, true, this, "prefixIndex", String.class, List.class);
                nodeIndex = new SplitCachedSortedMap<NodeId, NodeInfo>(Config.NODE_INDEX_SPLIT_TABLE_SIZE, true, this, (isTemporal) ? "temporalNodeIndex" : "nodeIndex", (isTemporal) ? new HistoryDLNNodeIdBinding() : new DLNNodeIdBinding(), NodeInfo.class);

            } else {
                // Internal database table
                //System.out.println("DB is on disk");
                metadataTable = new CachedMap(openTable("metadata", String.class, Integer.class));

                /*
                 keywordIndex = new CachedMap<String, KeywordInfo>(openTable("keywordIndex", String.class, KeywordInfo.class));
                 keywordPathsIndex = new CachedMap<String, Set<PathId>>(openTable("keywordPathsIndex", String.class, Set.class));
                 keywordNodesIndex = new CachedMap(openTable("keywordNodesIndex", String.class, Map.class));
                 nodeIndex = new CachedSortedMap<NodeId, NodeInfo>(openSortedTable("nodeIndex", new HistoryDLNNodeIdBinding(), NodeInfo.class));
                 */
                keywordIndex = new SplitCachedMap<String, KeywordInfo>(Config.KEYWORD_INDEX_SPLIT_TABLE_SIZE, false, this, "keywordIndex", String.class, KeywordInfo.class);
                keywordPathsIndex = new SplitCachedMapSetValue<String, Set<PathId>>(Config.KEYWORD_INDEX_SPLIT_TABLE_SIZE, false, this, "keywordPathsIndex", String.class, Set.class);
                keywordNodesIndex = new SplitCachedMapKeywordNodeIndex(Config.KEYWORD_INDEX_SPLIT_TABLE_SIZE, false, this, "keywordNodesIndex", String.class, Map.class);
                branchIndex = new SplitCachedMap<String, List<Branch>>(Config.KEYWORD_INDEX_SPLIT_TABLE_SIZE, false, this, "branchIndex", String.class, List.class);
                prefixIndex = new SplitCachedMap<String, List<PrefixPair>>(Config.KEYWORD_INDEX_SPLIT_TABLE_SIZE, false, this, "prefixIndex", String.class, List.class);
                nodeIndex = new SplitCachedSortedMap<NodeId, NodeInfo>(Config.NODE_INDEX_SPLIT_TABLE_SIZE, false, this, (isTemporal) ? "temporalNodeIndex" : "nodeIndex", (isTemporal) ? new HistoryDLNNodeIdBinding() : new DLNNodeIdBinding(), NodeInfo.class);

                /*
                 keywordIndex = new SplitMap<String, KeywordInfo>(this, "keywordIndex", String.class, KeywordInfo.class);
                 keywordPathsIndex = new SplitMapSetValue<String, Set<PathId>>(this, "keywordPathsIndex", String.class, Set.class);
                 keywordNodesIndex = new SplitMapKeywordNodeIndex(this, "keywordNodesIndex", String.class, Map.class);
                 nodeIndex = new SortedSplitMap<NodeId, NodeInfo>(this, "nodeIndex", new HistoryDLNNodeIdBinding(), NodeInfo.class);
                 */
            }
        }
    }

    /*
     * Open all of the database tables. Call this once to open the database.
     */
    public void openNonSplitTables(boolean isReadOnly) {
        if (!alreadyOpen) {
            if (isReadOnly) {
                //System.out.println("Nonsplit is normal");
                termIndex = openTable("termIndex", String.class, TreeMap.class);
                termNodeIndex = openSortedTable("termNodeIndex", TermNodeKey.class, PathId.class);
                pathNameIndex = openTable("pathNameIndex", String.class, PathId.class);
                pathIndex = openTable("pathIndex", PathId.class, PathInfo.class);
                typeIndex = openTable("typeIndex", PathId.class, TreeSet.class);
            } else {
                //System.out.println("Nonsplit is cached");
                termIndex = new CachedMap(openTable("termIndex", String.class, TreeMap.class));
                termNodeIndex = new CachedSortedMap(openSortedTable("termNodeIndex", TermNodeKey.class, PathId.class));
                pathNameIndex = new CachedMap(openTable("pathNameIndex", String.class, PathId.class));
                pathIndex = new CachedMap(openTable("pathIndex", PathId.class, PathInfo.class));
                typeIndex = new CachedMap(openTable("typeIndex", PathId.class, TreeSet.class));
            }
        }
    }

    /*
     * Open all of the database tables. Call this once to open the database.
     */
    public void commitNonSplitTables() {
        //System.out.println("committing nonsplit");
        if (!alreadyOpen) {
            if (isReadOnly) {
                //System.out.println("Nonsplit is normal");
                /*
                 termIndex = openTable("termIndex", String.class, TreeMap.class);
                 termNodeIndex = openSortedTable("termNodeIndex", TermNodeKey.class, PathId.class);
                 pathNameIndex = openTable("pathNameIndex", String.class, PathId.class);
                 pathIndex = openTable("pathIndex", PathId.class, PathInfo.class);
                 typeIndex = openTable("typeIndex", PathId.class, TreeSet.class);
                 */
            } else {
                //System.out.println("Nonsplit is cached");           
                CachedMap c = (CachedMap) termIndex;
                c.commit();
                c = (CachedMap) pathIndex;
                c.commit();
                c = (CachedMap) pathNameIndex;
                c.commit();
                c = (CachedMap) typeIndex;
                c.commit();
                CachedSortedMap d = (CachedSortedMap) termNodeIndex;
                d.commit();

            }
        }
    }

    /* This method commits the open tables, and the database
     * environment.
     */
    @Override
    public void commitDatabase() {
        if (!alreadyOpen) {
            return;
        }
        String s = "default";
        try {
            myEnv.checkpoint(new CheckpointConfig());
        } catch (Exception e) {
            DBError.Error("Committing database " + databaseName + s + "\nError: " + e);
        }
    }

    /* This method closes all of the open tables, and the database
     * environment.
     */
    @Override
    public void closeDatabase() {
        if (!alreadyOpen) {
            return;
        }

        alreadyOpen = false;
        String s = "default";
        try {

            if (!isReadOnly) {
                //closeSplitTables();
                commitCachedTables();
            }
            //System.out.println("Closing " + openDbs.keySet().size() + " tables");
            // Iterate through the open tables
            for (String dbName : openDbs.keySet()) {
                s = dbName;
                com.sleepycat.je.Database db = openDbs.get(dbName);
                //System.out.println("closing " + dbName + " " + db.count());
                db.close();
            }
            // We need to close the database catalog

            s = "catalog";  // Set for error message only
            catalogDb.close();

            // Close the environment
            s = " environment";  // Set for error message only
            //myEnv.sync();
            // Closing the environment gives errors about open DBs, so turn off
            //myEnv.close();
            javaCatalog = null;
            myEnv = null;
        } catch (Exception e) {
            DBError.Error("Closing database " + databaseName + " " + s + "\nError: " + e);
        }
    }

    /* This method closes all of the open tables, and the database
     * environment.
     */
    public void closeADatabase(String name) {
        if (!alreadyOpen) {
            return;
        }

        try {
            // Iterate through the open tables
            //System.out.println("closing " + name);
            com.sleepycat.je.Database db = openDbs.get(name);
            db.close();
            openDbs.remove(name);

        } catch (Exception e) {
            DBError.Error("Closing database " + databaseName + "\nError: " + e);
        }
    }

    /* Close the database */
    @Override
    protected void finalize() throws Throwable {
        try {
            closeDatabase();        // close open files
        } finally {
            super.finalize();
        }
    }

    /*
     * Set the database directory.
     */
    public void setDbHome(String directory) {
        databaseName = directory;
    }

    /*
     * Set the database directory.
     */
    public String getDbHome() {
        return databaseName;
    }

    /*
     * Open the Database. This method manages all of the BerkeleyDB
     * initialization code, etc. to open tables.
     */
    public void setAllowDuplicates() {
        myDbConfig.setSortedDuplicates(true);
    }

    public void setNoDuplicates() {
        myDbConfig.setSortedDuplicates(false);
    }

    /*
     * Open the Database. This method manages all of the BerkeleyDB
     * initialization code, etc. to open tables.
     * @param - name of the database
     */
    @Override
    public void openDatabase(String name) {
        databaseName = name;
        openDatabase();
    }


    /*
     * Open the Database. This method manages all of the BerkeleyDB
     * initialization code, etc. to open tables.
     */
    @Override
    public void openDatabase() {
        if (!alreadyOpen) {
            openDbs = new HashMap();
            // Establish some configurations for the DB environment
            try {
                // Test if myEnv is already initialized
                if (myEnv == null) {
                    //Durability defaultDurability
                    //        = new Durability(Durability.SyncPolicy.NO_SYNC,
                    //               null, // unused by non-HA applications. 
                    //               null);   // unused by non-HA applications.  
                    EnvironmentConfig myEnvConfig = new EnvironmentConfig();
                    myEnvConfig.setTransactional(false);
                    //myEnvConfig.setDurability(defaultDurability);
                    myEnvConfig.setAllowCreate(true /*!isReadOnly*/);
                    myEnvConfig.setCacheSize(Config.CACHE_SIZE);
                    //myEnvConfig.setLocking(false);
                    //myEnvConfig.setCachePercent(90); //100mb

                    // Open the environment in the specified directory
                    myEnv = new Environment(new File(databaseName), myEnvConfig);

                    // Now set up the database configuration
                    myDbConfig = new DatabaseConfig();

                    myDbConfig.setDeferredWrite(true);
                    myDbConfig.setTransactional(false);
                    myDbConfig.setAllowCreate(true /*!isReadOnly*/);
                    //myDbConfig.setBtreeComparator(MyComparator.class);
                    //System.out.println("Database: Cache size " + myEnv.getConfig().getCachePercent() + " " + myEnv.getConfig().getCacheSize());
                    // Open the database's catalog if not already opened

                    //StatsConfig config = new StatsConfig();
                    //config.setClear(true);
                    //System.err.println(myEnv.getStats(config));
                    if (javaCatalog == null) {
                        // Open the Java class catalog for serialization
                        catalogDb = myEnv.openDatabase(null, javaCatalogName, myDbConfig);
                        javaCatalog = new StoredClassCatalog(catalogDb);
                    }
                    openTables(isReadOnly);
                    // Set the Flag to indicate we've already opened these guys.
                    alreadyOpen = true;
                }
            } catch (Exception e) {
                e.printStackTrace(new PrintStream(System.err));
                DBError.Error("Opening database " + databaseName + "\nError: " + e);
                System.exit(-1);
            }
        }
    }

    /*
     * Open an iterator for a table.
     *
     * @param String name - Table name @param EntryBinding e - BerekeleyDB class
     * binding @param Class c - Java class info, BerekeleyDB binding will be
     * created. @returns TableIterator - constructed iterator
     */
    public Iterator openIterator(String name, EntryBinding e, Class c) {
        return openIterator(name, e, new SerialBinding(javaCatalog, c));
    }

    /*
     * Open an iterator for a table.
     *
     * @param String name - Table name @param Class c - Java class info,
     * BerekeleyDB binding will be created. @param EntryBinding e - BerekeleyDB
     * class binding @returns TableIterator - constructed iterator
     */
    public Iterator openIterator(String name, Class c, EntryBinding e) {
        return openIterator(name, new SerialBinding(javaCatalog, c), e);
    }

    /*
     * Open an iterator for a table.
     *
     * @param String name - Table name @param Class c1 - Java class info,
     * BerekeleyDB binding will be created. @param Class c2 - Java class info,
     * BerekeleyDB binding will be created. @returns TableIterator - constructed
     * iterator
     */
    @Override
    public Iterator openIterator(String name, Class c1, Class c2) {
        return openIterator(name, new SerialBinding(javaCatalog, c1), new SerialBinding(javaCatalog, c2));
    }

    /*
     * Open an iterator for a table. @param String name - Table name @param
     * EntryBinding e - BerekeleyDB class binding @param Class c - Java class
     * info, BerekeleyDB binding will be created. @returns TableIterator -
     * constructed iterator
     */
    public Iterator openIterator(String name, EntryBinding k, EntryBinding b) {
        //System.out.println("BERKELEYDB DATABASE openeing iterator for " + name);
        if (!openDbs.containsKey(name)) {
            openTable(name, k, b);
        }
        //return ((Map) openMaps.get(name)).keySet().iterator();
        return ((Map) openDbs.get(name)).keySet().iterator();
    }

    /*
     * Open a table.
     *
     * @param String name - Table name @param EntryBinding e for key -
     * BerkeleyDB binding info @param Class c - Java class info for data,
     * BerekeleyDB binding will be created. @returns Map - constructed Map
     */
    public Map openTable(String name, EntryBinding e, Class c) {
        return openTable(name, e, new SerialBinding(javaCatalog, c));
    }

    /*
     * Open a table.
     *
     * @param String name - Table name @param Class c - Java class info for key,
     * BerekeleyDB binding will be created. @param EntryBinding e1 - BerkeleyDB
     * binding info for data @returns Map - constructed Map
     */
    public Map openTable(String name, Class c, EntryBinding e) {
        return openTable(name, new SerialBinding(javaCatalog, c), e);
    }

    /*
     * Open a table.
     *
     * @param String name - Table name @param Class c1 - Java class info for
     * key, BerekeleyDB binding will be created. @param Class c2 - Java class
     * info for data, BerekeleyDB binding will be created. @returns Map -
     * constructed Map
     */
    @Override
    public Map openTable(String name, Class c1, Class c2) {
        return openTable(name, new SerialBinding(javaCatalog, c1), new SerialBinding(javaCatalog, c2));
    }

    /*
     * Open a table with a sort order defined on the keys.
     *
     * @param String name - Table name @param Class c1 - Java class info for
     * key, BerekeleyDB binding will be created. @param Class c2 - Java class
     * info for data, BerekeleyDB binding will be created. @returns Map -
     * constructed Map
     */
    @Override
    public SortedMap openSortedTable(String name, Class c1, Class c2) {
        return openSortedTable(name, new SerialBinding(javaCatalog, c1), new SerialBinding(javaCatalog, c2));
    }

    /*
     * Open a table with a sort order defined on the keys.
     *
     * @param String name - Table name @param Class c1 - Java class info for
     * key, BerekeleyDB binding will be created. @param Class c2 - Java class
     * info for data, BerekeleyDB binding will be created. @returns Map -
     * constructed Map
     */
    public SortedMap openSortedTable(String name, EntryBinding e1, Class c2) {
        return openSortedTable(name, e1, new SerialBinding(javaCatalog, c2));
    }

    /*
     * Open a table.
     *
     * @param String name - Table name @param EntryBinding e1 - BerkeleyDB
     * binding info for key @param EntryBinding e2 - BerkeleyDB binding info for
     * data @param duplicates - Are duplicates allowed? @returns Map -
     * constructed Map
     */
    public Map openTable(String name, EntryBinding e1, EntryBinding e2) {
        com.sleepycat.je.Database tableDb = null;

        try {
            //System.out.println("opening table " + name);
            tableDb = myEnv.openDatabase(null, name, myDbConfig);
            openDbs.put(name, tableDb);
        } catch (Exception e) {
            DBError.Error("Opening table " + name + "\nError: " + e);
        }
        //return new TablePersistent(tableDb, e1, e2);
        return new StoredMap(tableDb, e1, e2, true);
    }

    /*
     * Open a table.
     *
     * @param String name - Table name @param EntryBinding e1 - BerkeleyDB
     * binding info for key @param EntryBinding e2 - BerkeleyDB binding info for
     * data @param duplicates - Are duplicates allowed? @returns Map -
     * constructed Map
     */
    public SortedMap openSortedTable(String name, EntryBinding e1, EntityBinding e2) {
        com.sleepycat.je.Database tableDb = null;

        try {
            //System.out.println("opening table " + name);
            tableDb = myEnv.openDatabase(null, name, myDbConfig);
            openDbs.put(name, tableDb);
        } catch (Exception e) {
            DBError.Error("Opening table " + name + "\nError: " + e);
        }
        //return new TablePersistent(tableDb, e1, e2);
        return new StoredSortedMap(tableDb, e1, e2, true);
    }

    /*
     * Open a table.
     *
     * @param String name - Table name @param EntryBinding e1 - BerkeleyDB
     * binding info for key @param EntryBinding e2 - BerkeleyDB binding info for
     * data @param duplicates - Are duplicates allowed? @returns Map -
     * constructed Map
     */
    public SortedMap openSortedTable(String name, EntryBinding e1, SerialBinding e2) {
        com.sleepycat.je.Database tableDb = null;
        try {
            //System.out.println("opening sorted table " + name);
            tableDb = myEnv.openDatabase(null, name, myDbConfig);
            openDbs.put(name, tableDb);
        } catch (Exception e) {
            DBError.Error("Opening table " + name + "\nError: " + e);
        }
        return new StoredSortedMap(tableDb, e1, e2, true);
    }

    /*
     * Open a table.
     *
     * @param String name - Table name @param Class c1 - Java class info for
     * key, BerekeleyDB binding will be created. @param Class c2 - Java class
     * info for data, BerekeleyDB binding will be created. @returns Map -
     * constructed Map
     */
    @Override
    public MapWithDuplicates openTableWithDuplicates(String name, Class c1, Class c2) {
        return openTableWithDuplicates(name, new SerialBinding(javaCatalog, c1), new SerialBinding(javaCatalog, c2));
    }

    /*
     * Open a table.
     *
     * @param String name - Table name @param Class c1 - Java class info for
     * key, BerekeleyDB binding will be created. @param Class c2 - Java class
     * info for data, BerekeleyDB binding will be created. @returns Map -
     * constructed Map
     */
    public MapWithDuplicates openTableWithDuplicates(String name, EntryBinding e1, EntryBinding e2) {
        com.sleepycat.je.Database tableDb = null;
        try {
            setAllowDuplicates();
            tableDb = myEnv.openDatabase(null, name, myDbConfig);
            openDbs.put(name, tableDb);
            setNoDuplicates();
        } catch (Exception e) {
            DBError.Error("Opening table " + name + "\nError: " + e);
        }
        StoredMapWithDuplicates s = new StoredMapWithDuplicates(tableDb, e1, e2, true);
        return s;
    }

    /**
     * Code to debug SortedMaps. Enables you to see the bit strings compared in
     * the B+-tree.
     *
     * @author Curtis Dyreson
     */
    private static class MyComparator implements Comparator<byte[]> {

        public MyComparator() {
        }

        public int compare(byte[] b1, byte[] b2) {
            System.out.println("here doing comparison " + b1.length + " " + b2.length);
            System.out.println(debug(b1));
            System.out.println(debug(b2));

            int i = calculate(b1, b2);
            System.out.println(i);
            return i;
        }

        public static int calculate(byte[] b1, byte[] b2) {
            int size = (b1.length < b2.length) ? b1.length : b2.length;
            for (int i = 0; i < size; i++) {
                //System.out.println("doing " + b1[i] + " " + b2[i]);
                if (b1[i] != b2[i]) {
                    return (((b1[i] & 0xFF) - (b2[i] & 0xFF)) < 0) ? -1 : 1;
                }
            }
            return (b1.length == b2.length) ? 0 : (b1.length < b2.length) ? -1 : 1;
        }

        public static String debug(byte[] b) {
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < b.length; i++) {
                buf.append(toBitString(b[i]));
            }
            return buf.toString();
        }

        private final static char[] digits = {'0', '1'};

        /**
         * Returns a string showing the bit representation of the given byte.
         *
         * @param b the byte to display
         * @return string representation
         */
        public final static String toBitString(byte b) {
            char[] buf = new char[8];
            int charPos = 8;
            int radix = 2;
            int mask = radix - 1;
            for (int i = 0; i < 8; i++) {
                buf[--charPos] = digits[b & mask];
                b >>>= 1;
            }
            return new String(buf);
        }
    }

}
