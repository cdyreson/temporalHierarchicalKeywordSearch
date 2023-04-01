package messiah.database;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import usu.NodeId;
import usu.PathId;
import messiah.TermNodeKey;
import java.util.TreeSet;
import java.util.List;
import java.util.Set;
import messiah.NodeInfo;
//import usu.metaData.NodeMetaData;
import messiah.KeywordInfo;
import java.util.TreeMap;
import messiah.PathInfo;
import usu.level.Branch;
import usu.level.PrefixPair;
//import messiah.Config;

/**
 * The Database class provides operators on the underlying database. In this
 * implementation, BerkeleyDB tables are used. Eventually this class will become
 * abstract and implemented by several database products.
 *
 * @author Curtis
 */
public abstract class Database {
    // Database internal table, keeps track of names of tables, etc.
    public Map<String, Integer> metadataTable;

    // Tables that help sequenced, nonsequenced, snapshot search
    // Note, we currently don't care about schema evolution
    public Map<String, SortedMap<NodeId, PathId>> termIndex;
    public SortedMap<TermNodeKey, PathId> termNodeIndex;
    public Map<String, PathId> pathNameIndex;
    public Map<PathId, PathInfo> pathIndex;
    public Map<PathId, TreeSet> typeIndex;
    public Map<String, KeywordInfo> keywordIndex;
    // Curt added these to do sequenced/noseq search
    public Map<String, Set<PathId>> keywordPathsIndex;
    public Map<String, Map<PathId, List<NodeId>>> keywordNodesIndex;
    
    // Snapshot document tables
    public SortedMap<NodeId, NodeInfo> nodeIndex;
    public SortedMap<NodeId, NodeInfo> temporalNodeIndex;
    
    public Map<String,List<Branch>> branchIndex;
    public Map<String,List<PrefixPair>> prefixIndex;
    
    // Tables to map a keyword to a list of level pairs
    
    // History document tables
    //ITEMOUT public SortedMap<NodeId, NodeInfo> historyNodeIndex;

    // Item document table
    //ITEMOUT public SortedMap<NodeId, NodeInfo> itemNodeIndex;
    //ITEMOUT public Map<NodeId, List<NodeId>> itemIdToHistoryIdIndex;
    //ITEMOUT public Map<NodeId, NodeId> historyIdToItemIdIndex;

    // Tables for testing
    //public SortedMap<NodeId, Integer> node2Index;

    //Amani's tables
    //ITEMOUT public Map<NodeId, NodeId> historyToItemIndex;
    //ITEMOUT public Map<NodeId, List<NodeId>> itemToHistoryIndex;
    //public Map<NodeId, NodeMetaData> itemMetaDataIndex;
    //public Map<NodeId, NodeMetaData> metaDataIndex;

    /*Flags */
    static public Boolean inMemoryDB = true;
    protected boolean alreadyOpen = false;
    // Default name of the directory in which the database lives
    protected String databaseName = "db";

    public void copyPartialDatabaseToDisk() {
    }
    
    /* 
     * Open all of the database tables.
     * Call this once to open the database.
     */
    public abstract void openDatabase(String name);

    /* Open the default database.
     * Call this once to open the database.
     */
    public abstract void openDatabase();

    /* This method closes all of the open tables, and the database
     * environment.
     */
    public abstract void closeDatabase();

    public String getDatasetName() {
        return databaseName;
    }
    
    /* Make the changes persistent.
     */
    public abstract void commitDatabase();

    /* Open an iterator for a table. Contains the code to do the iterator
     * creation.
     * @param String name - Table name
     * @param Class c - Class binding for key
     * @param Class b - Class binding for value
     * @returns TableIterator - constructed iterator
     */
    public abstract Iterator openIterator(String name, Class c, Class b);

    /* Open a table.
     * @param String name - Table name
     * @param Class c - Class binding for key
     * @param Class b - Class binding for value
     * @returns Map - constructed table
     */
    public abstract Map openTable(String name, Class c, Class b);

    /* Open a table with a sort order defined on the keys.
     * @param String name - Table name
     * @param Class c - Class binding for key
     * @param Class b - Class binding for value
     * @returns Map - constructed table
     */
    public abstract SortedMap openSortedTable(String name, Class c, Class b);

    /* Open a table.
     * @param String name - Table name
     * @param Class c - Class binding for key
     * @param Class b - Class binding for value
     * @returns Map - constructed table
     */
    public abstract MapWithDuplicates openTableWithDuplicates(String name, Class c, Class b);
}
