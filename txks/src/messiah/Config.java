package messiah;

/**
 *
 * @author truongbaquan
 */
public class Config {

    public final static String TOKENS_DELIMITER = "[ \\p{Punct}\n\r\t]+";

    public final static String DB_FOLDER_STRING = "D:\\Databases/";
    public final static String XML_FOLDER_STRING = "xml/";
    public final static String JSON_FOLDER_STRING = "json/";

    public static final double SIZE_THRESHOLD = 20;

    // Controls how many objects can be in the memory map before dumping to disk
    public static final int CACHE_MAP_SIZE = 1000000;

    // BerkeleyDB cache size
    public static final int CACHE_SIZE = 200000000;

    // Maximum number of items per split in CachedSplitTable, etc.
    public static final int NODE_INDEX_SPLIT_TABLE_SIZE =    1000000;
    public static final int KEYWORD_INDEX_SPLIT_TABLE_SIZE = 1000000;

    // Maximum number of splits allowed
    public static final int MAX_SPLITS = 100;
    
    // Initial maximum depth guess for XML tree (can grow)
    public static final int MAX_DEPTH = 1000;
}
