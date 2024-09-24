package messiah;

import com.sleepycat.je.DatabaseException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTree;
import messiah.database.Database;
import messiah.parse.CurtParseTask;
import messiah.parse.DeleteIntervalGenerator;
import messiah.parse.HistoryJSONParseTask;
import messiah.parse.HistoryParseTask;
import messiah.parse.IntervalGenerator;
import messiah.parse.RandomIntervalGenerator;
import messiah.parse.JSONParseTask;
import messiah.parse.RepresentationalParseTask;
import messiah.parse.StaticIntervalGenerator;
import messiah.search.SearchAlgoEnum;
import messiah.search.generic.ResultSizeEstimator;
import messiah.search.generic.Search;
import messiah.search.generic.SearchResult;
import messiah.storage.generic.DbAccess;
import messiah.utils.Stopwatch;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import usu.algebra.KeywordSearchExpression;
import usu.grammar.TXKSLexer;
import usu.grammar.TXKSParseException;
import usu.grammar.TXKSParser;

/**
 * Program to test/run the search
 *
 * @author truongbaquan, curt
 */
public class Main {

    private static boolean verbose = false;

    private Database bdb;
    private messiah.database.berkeleydb.Database diskCopyDB;
    private boolean doDiskCopy = false;
    private DbAccess db;

    public Main() {

    }

    public Database getCurDataset() {
        return bdb;
    }

    public DbAccess getCurIndex() {
        return db;
    }

    public boolean isInitialized() {
        return (bdb != null);
    }

    public boolean resetIndexes() {
        if (this.isInitialized()) {
            this.db = new DbAccess(bdb);
            return true;
        } else {
            return false;
        }
    }

    private ResultSizeEstimator estimator;

    public SearchAlgoEnum selectAlgo(String searchText) {
        if (estimator == null) {
            estimator = new ResultSizeEstimator(db, bdb);
        }
        double estimatedSize = estimator.estimate(parseQuery(searchText));
        if (estimatedSize <= Config.SIZE_THRESHOLD) {
            return SearchAlgoEnum.PartialFSLCA;
        } else {
            return SearchAlgoEnum.CompleteFSLCA;
        }
    }

    public SearchResult search(boolean indexUsed, KeywordSearchExpression exp) {
        Search s = new Search(db, bdb, indexUsed, exp);
        // get the search keywords
        //String[] tokens = parseQuery(searchText);
        //generate results and make a JTree
        return s.search();
    }

    public DbAccess loadDataset(String datasetName) {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        try {
            //if (isInitialized()) {
            //    closeDB();
            //}
            //if (bdb == null) bdb = new messiah.database.memory.Database();
            // Open a disk resident DB
            openDB(Config.DB_FOLDER_STRING + datasetName, true, true, true /* isTemporal */);
            
            if (db == null) {
                db = new DbAccess(bdb);
            }
            return db;
        } catch (DatabaseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            stopwatch.stop();
            System.out.println("Loading DB time = " + stopwatch.readTime() + "ms");
        }
    }

    /**
     * Parses an XML file and stores into a Berkeley dataset
     *
     * @param datasetName Dataset name
     * @param parsedFile Input XML file
     * @param parseDialog A dialog to show the parsing progress; ignored if null
     */
    public void parseDataset(String datasetName, File parsedFile, ParseDialog parseDialog, IntervalGenerator intervalGenerator, int maxNodes) {
        if (bdb == null) {
            System.out.println("Creating in-memory db");
            bdb = new messiah.database.memory.Database(false, /* does not matter for in memory */ false);
        }
        /*
         if (doDiskCopy) {
         messiah.database.memory.Database memdb = (messiah.database.memory.Database) bdb;
         memdb.cacheDiskDBHandle(diskCopyDB);
         }S
         */

        try {
            HistoryJSONParseTask task = new HistoryJSONParseTask(bdb, parsedFile, intervalGenerator, maxNodes);
            if (parseDialog != null) {
                parseDialog.registerTask(task);
            }
            //System.out.println("Curt : executing ");
            task.execute();
            //task.doIt();
        } catch (DatabaseException | FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Parses an XML file and stores into a Berkeley dataset, non-Swing version
     * of the Parser
     *
     * @param datasetName Dataset name
     * @param parsedFile Input XML file
     *
     */
    public void testParser(String datasetName, File parsedFile, boolean isJSON, boolean isTemporal, boolean isRepresentational, IntervalGenerator intervalGenerator, int maxNodes) {
        try {
            if (isJSON) {
                if (isTemporal) {
                    
                }
                JSONParseTask task = (isTemporal)? 
                        new HistoryJSONParseTask(bdb, parsedFile, intervalGenerator,maxNodes)
                        : new JSONParseTask(bdb, parsedFile, maxNodes);
                task.doInBackground();
                resetIndexes();
            } else if (isTemporal) {
                HistoryParseTask task = new HistoryParseTask(bdb, parsedFile, intervalGenerator, maxNodes);
                //System.out.println("Curt : HistoryParseTask executing ");
                task.parseThisThing();
                resetIndexes();
            } else if (isRepresentational) {
                RepresentationalParseTask task = new RepresentationalParseTask(bdb, parsedFile, maxNodes);
                //System.out.println("Curt : executing ");
                task.doInBackground();
                resetIndexes();
            } else {
                CurtParseTask task = new CurtParseTask(bdb, parsedFile, maxNodes);
                //System.out.println("Curt : executing ");
                task.doInBackground();
                resetIndexes();
            }
        } catch (DatabaseException | FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeDB() {
        if (bdb != null) {
            try {

                //close the previous db cleanly
                if (doDiskCopy) {
                    System.out.println("Doing a disk copy");
                    messiah.database.memory.Database memdb = (messiah.database.memory.Database) bdb;
                    memdb.copyDatabaseToDisk();
                    diskCopyDB.closeDatabase();
                    diskCopyDB = null;
                }

                bdb.closeDatabase();
            } catch (DatabaseException ex) {
                ex.printStackTrace();
            }
            bdb = null;
        }
    }

    public boolean isDatasetCreated(String datasetName) {
        return (new File(Config.DB_FOLDER_STRING + datasetName).exists());
    }

    private String[] parseQuery(String textQuery) {
        return textQuery.split("[\\s]+");
    }

    /**
     * Compile a SearchQuery stored in a file. The name of the file is passed as
     * a String.
     */
    public static KeywordSearchExpression compile(String fileName) {
        TXKSLexer lex = null;

        try {
            if (verbose) {
                System.out.println("Reading query from file " + fileName);
            }
            lex = new TXKSLexer(new ANTLRStringStream(fileName));
            lex = new TXKSLexer(new ANTLRFileStream(fileName));
        } catch (Exception t) {
            System.out.println("Error building the TXKS lexer: probably could not open file " + fileName);
            t.printStackTrace();
            System.exit(-1);
        }
        CommonTokenStream tokens = new CommonTokenStream(lex);
        TXKSParser parser = new TXKSParser(tokens);
        try {
            KeywordSearchExpression q = parser.program();
            //System.out.println("Query q is " + q.getClass());
            //EvalNode evalNode = q.evaluate();
            //System.out.println("Query got evaluate " + evalNode);
            //System.out.println("Query get keywords: " + evalNode.getKeywords());
            //System.out.println("Query is sequenced search: " + evalNode.isSequencedSearch());
            //System.out.println("Query is sequenced result: " + evalNode.isSequencedResult());
            //System.out.println("in here " + q);
            return q;
        } catch (TXKSParseException t) {
            System.out.println("TXKS.java: Error " + t.msg);
            //t.printStackTrace();
            //System.exit(-1);
        } catch (RecognitionException t) {
            System.out.println("TXKS.java: Parse Error " + parser.getErrorMessage());
            //t.printStackTrace();
            //System.exit(-1);
        } catch (Exception t) {
            System.out.println("TXKS.java: other error ");
            t.printStackTrace();
            //System.exit(-1);
        }
        return null;
    }

    /**
     * Compile a SearchQuery stored in a string. The query is passed as a
     * String.
     */
    public static KeywordSearchExpression compileFromString(String query) {
        TXKSLexer lex = null;

        try {
            if (verbose) {
                System.out.println("Doing query " + query);
            }
            lex = new TXKSLexer(new ANTLRStringStream(query));
            //lex = new TXKSLexer(new ANTLRFileStream(fileName));
        } catch (Exception t) {
            System.out.println("Error building the TXKS lexer: something wrong with query " + query);
            t.printStackTrace();
            System.exit(-1);
        }
        CommonTokenStream tokens = new CommonTokenStream(lex);
        TXKSParser parser = new TXKSParser(tokens);
        try {
            KeywordSearchExpression q = parser.program();
            //System.out.println("Query q is " + q.getClass());
            //EvalNode evalNode = q.evaluate();
            //System.out.println("Query got evaluate " + evalNode);
            //System.out.println("Query get keywords: " + evalNode.getKeywords());
            //System.out.println("Query is sequenced search: " + evalNode.isSequencedSearch());
            //System.out.println("Query is sequenced result: " + evalNode.isSequencedResult());
            //System.out.println("in here " + q);
            return q;
        } catch (TXKSParseException t) {
            System.out.println("TXKS.java: Error " + t.msg);
            //t.printStackTrace();
            //System.exit(-1);
        } catch (RecognitionException t) {
            System.out.println("TXKS.java: Parse Error " + parser.getErrorMessage());
            //t.printStackTrace();
            //System.exit(-1);
        } catch (Exception t) {
            System.out.println("TXKS.java: other error ");
            t.printStackTrace();
            //System.exit(-1);
        }
        return null;
    }

    private void openDB(String dbName, boolean isDiskDb, boolean isReadOnly, boolean isTemporal) {
        if (isDiskDb) {
            this.bdb = new messiah.database.berkeleydb.Database(dbName, isReadOnly, isTemporal);
        } else {
            this.bdb = new messiah.database.memory.Database(dbName, isReadOnly, isTemporal);
        }
    }

    public static void main(String[] args) {
        Main foo = new Main();
        //foo.testParser("curt", new File("curt.xml"), false);
        //foo.closeDB();
        boolean isDiskDb = false;
        boolean isReadOnly = true;
        int maxNodes = Integer.MAX_VALUE;
        boolean isTemporalDB = false; // Set this flag if DB has timestamps
        int consumed = 0;
        String xmlFileName = Config.XML_FOLDER_STRING + "curt.xml";
        String dbName = Config.DB_FOLDER_STRING;
        int maxRange = 100;
        boolean isRandomIntervals = false;
        boolean isFixedIntervals = false;
        int maxInterval = 100;
        boolean isPercentDelete = false;
        int percentDelete = 0;
        IntervalGenerator intervalGenerator = new StaticIntervalGenerator();
        
        if (args[consumed].contentEquals("--verbose")) {
            verbose = true;
            consumed++;
        }
        if (args[consumed].contentEquals("--xml")) {
            consumed++;
            String fileName = args[consumed];
            xmlFileName = Config.XML_FOLDER_STRING + fileName;
            //xmlFileName = fileName;
            dbName = Config.DB_FOLDER_STRING + fileName;
            new File(dbName).mkdir();
            consumed++;
        }
        if (args[consumed].contentEquals("--json")) {
            consumed++;
            String fileName = args[consumed];
            xmlFileName = Config.JSON_FOLDER_STRING + fileName;
            //xmlFileName = fileName;
            dbName = Config.DB_FOLDER_STRING + fileName;
            new File(dbName).mkdir();
            consumed++;
        }
        if (args[consumed].contentEquals("--disk")) {
            if (verbose) {
                System.out.println("Using database on disk");
            }
            //foo.bdb = new messiah.database.berkeleydb.Database(dbName, true);
            isDiskDb = true;
            consumed++;
        } else if (args[consumed].contentEquals("--memory")) {
            //foo.bdb = new messiah.database.memory.Database(dbName, true);
            isDiskDb = false;
            consumed++;
        } else if (args[consumed].contentEquals("--hybrid")) {
            foo.bdb = new messiah.database.memory.Database(dbName, false, true);
            foo.diskCopyDB = new messiah.database.berkeleydb.Database(dbName, false, true);
            messiah.database.memory.Database db = (messiah.database.memory.Database) foo.bdb;
            System.out.println("Caching db handle " + foo.diskCopyDB + " " + db);
            db.cacheDiskDBHandle(foo.diskCopyDB);
            foo.doDiskCopy = true;
            consumed++;
        } else {
            // default to memory
            isDiskDb = false;
        }

        // Set the temporalDB flag if the data has timestamps
        if (args[consumed].contentEquals("--timestamps")) {
            isTemporalDB = true;
            consumed++;
            if (args[consumed].contentEquals("--maxRange")) {
                consumed++;
                maxRange = Integer.parseInt(args[consumed]);
                consumed++;
            }
            if (args[consumed].contentEquals("--maxInterval")) {
                consumed++;
                maxInterval = Integer.parseInt(args[consumed]);
                consumed++;
            }
            if (args[consumed].contentEquals("--randomIntervals")) {
                consumed++;
                isRandomIntervals = true;
            }
            if (args[consumed].contentEquals("--fixedIntervals")) {
                consumed++;
                isFixedIntervals = true;
            }
            if (args[consumed].contentEquals("--percentDelete")) {
                consumed++;
                percentDelete = Integer.parseInt(args[consumed]);
                consumed++;
                isPercentDelete = true;
                System.out.println("here ");
                intervalGenerator = new DeleteIntervalGenerator(percentDelete);
            }
            if (isRandomIntervals) intervalGenerator = new RandomIntervalGenerator(maxRange, maxInterval, true);
            if (isFixedIntervals) intervalGenerator = new RandomIntervalGenerator(maxRange, maxInterval, false);
        }

        // Set the temporalDB flag if the data has timestamps
        if (args[consumed].contentEquals("--noTimestamps")) {
            isTemporalDB = false;
            consumed++;
        }

        // Set the maximum parsed nodes
        if (args[consumed].contentEquals("--maxNodes")) {
            // System.out.println("consuming maxNodes");
            consumed++;
            maxNodes = Integer.parseInt(args[consumed]);
            System.out.println("max nodes is " + maxNodes);
            consumed++;
        }
        
        // Open database for writing iff we are parsing
        if (args[consumed].contentEquals("--parse")) {
            System.out.println("XML parse " + dbName + " xml file is " + xmlFileName);
            isReadOnly = false;
            foo.openDB(dbName, isDiskDb, isReadOnly, isTemporalDB);
            foo.testParser(dbName, new File(xmlFileName), false, isTemporalDB, false, intervalGenerator, maxNodes);
            consumed++;
        } else if (args[consumed].contentEquals("--jsonParse")) {
            System.out.println("JSON parse " + dbName + " json file is " + xmlFileName);
            isReadOnly = false;
            foo.openDB(dbName, isDiskDb, isReadOnly, isTemporalDB);
            foo.testParser(dbName, new File(xmlFileName), true, isTemporalDB, false, intervalGenerator, maxNodes);
            consumed++;
        } else if (args[consumed].contentEquals("--representational")) {
            isReadOnly = false;
            foo.openDB(dbName, isDiskDb, isReadOnly, true);
            foo.testParser(dbName, new File(xmlFileName), false, false, true, intervalGenerator, maxNodes);
            consumed++;
        }

        // Database has been opened by now
        if (consumed < args.length) {
            if (args[consumed].contentEquals("--search")) {
                // Close DB if initialized to store stuff
                if (isDiskDb && foo.isInitialized()) {
                    foo.closeDB();
                    isReadOnly = true;
                }
                if (!foo.isInitialized()) {
                    foo.openDB(dbName, isDiskDb, isReadOnly, isTemporalDB);
                }
                String s = "";
                consumed++;
                for (int i = consumed; i < args.length; i++) {
                    s += args[i] + " ";
                }
                s.trim();
                System.out.println("Query is " + s);
                KeywordSearchExpression exp = compileFromString(s);
                //if (!foo.isInitialized()) {
                //    foo.openDB(dbName, isDiskDb, isReadOnly, isTemporalDB);
                //}
                SearchResult result = foo.search(true, exp);
                //generate results and make a JTree
                JTree resultTree = result.getResultTree();
            } else if (args[consumed].contentEquals("--queryFile")) {
                // Close DB if initialized to store stuff
                if (isDiskDb && foo.isInitialized()) {
                    foo.closeDB();
                    isReadOnly = true;
                    foo.openDB(dbName, isDiskDb, isReadOnly, isTemporalDB);
                }
                if (!foo.isInitialized()) {
                    foo.openDB(dbName, isDiskDb, isReadOnly, isTemporalDB);
                }
                String fileName = "";
                consumed++;
                for (int i = consumed; i < args.length; i++) {
                    fileName += args[i] + " ";
                }
                fileName.trim();
                KeywordSearchExpression exp = compile(fileName);
                foo.db = foo.loadDataset(dbName);
                SearchResult result = foo.search(true, exp);
                //generate results and make a JTree
                JTree resultTree = result.getResultTree();
            }
        }
        if (foo.isInitialized()) {
            foo.closeDB();
        }
    }
}
