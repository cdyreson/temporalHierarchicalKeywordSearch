package messiah.search.generic;

import java.util.List;
import java.util.Set;
import javax.swing.JTree;
import messiah.search.resultbuilder.ResultBuilder;
import messiah.search.resultbuilder.generic.SubtreeResultBuilder;
import messiah.search.slca.generic.*;
import messiah.storage.generic.DbAccess;
import messiah.utils.Stopwatch;
import usu.NodeId;
import usu.dln.DLNFactory;
import messiah.database.Database;
import messiah.search.SearchAlgoEnum;
import usu.algebra.KeywordSearchExpression;
import usu.algebra.evaluate.EvalNode;

/**
 * The Search class performs search
 *
 * @author truongbaquan, curtis
 */
public class Search {

    boolean verbose = false;

    private double slcaTime;
    private SLCAFinder slcaFinder;
    private ResultBuilder resultBuilder;

    private ResultSizeEstimator resultSizeEstimator;
    private KeywordSearchExpression exp;

    /*
     Create a new search object
     @param db - provides DbAccess
     @param bdb - the Database
     @param indexedUsed? - Do we use an index?
     @param searchAlgo - Search algorithm used, currently supports Partial and Complete FSLCA, and sequenced PartialFSLCA
     */
    public Search(DbAccess db, Database bdb, boolean indexUsed, KeywordSearchExpression exp) {

        SearchAlgoEnum searchAlgo = exp.getSearchType();
        this.exp = exp;
        switch (searchAlgo) {
            case SequencedSearch:
            case SequencedFSLCA:
                this.slcaFinder = new SimpleSequencedSLCA(bdb);
                break;
            case PartialFSLCA:
                this.slcaFinder = new PartialFSLCAFinder(bdb, new BinarySLCAFinder<DLNFactory>(new DLNFactory()));
                break;
            case NonTemporalFSLCA:
            case NontemporalSearch:
                //this.slcaFinder = new SequencedPartialFSLCAFinder(bdb, new BinarySLCAFinder<DLNFactory>(new DLNFactory()));
                //this.slcaFinder = new CurtFixedSLCA(bdb /*, new BinarySLCAFinder<DLNFactory>(new DLNFactory())*/);
                this.slcaFinder = new SimpleSLCA(bdb /*, new BinarySLCAFinder<DLNFactory>(new DLNFactory())*/);
                break;
            case CompleteFSLCA:
                this.slcaFinder = new CompleteFSLCAFinder(db, bdb, db.getTypeSLCAFinder(), new BinarySLCAFinder<DLNFactory>(new DLNFactory()), indexUsed);
                break;
            case TemporalSearch:
                this.slcaFinder = new TemporalSLCA(bdb /*, new BinarySLCAFinder<DLNFactory>(new DLNFactory())*/);
                break;

//            case IndexedLookup:
//                this.slcaFinder = new IleSLCAFinder(db.getNodeUtils(), termNodeIndex);
//                this.slcaFinder = new ILESlcaFinder(db.getNodeUtils(), termNodeIndex);
//                break;
//            case ScanEager:
//                this.slcaFinder = new ScanEagerSLCAFinder(db.getNodeUtils(), termIndex);//            case IndexedLookup:
//                this.slcaFinder = new IleSLCAFinder(db.getNodeUtils(), termNodeIndex);
//                this.slcaFinder = new ILESlcaFinder(db.getNodeUtils(), termNodeIndex);
//                break;
//            case ScanEager:
//                this.slcaFinder = new ScanEagerSLCAFinder(db.getNodeUtils(), termIndex);
            default:
                System.err.println("Search Type not supported in Search.java");
                System.exit(-1);
        }
//        this.resultBuilder = new SingleNodeResultBuilder(db);
        this.resultBuilder = new SubtreeResultBuilder(db, bdb);
        this.resultSizeEstimator = new ResultSizeEstimator(db, bdb);
    }

    /*
     Perform the search given the a search query tree
     @Param tree - EvalNode tree
     */
    /*
     private SearchResult search(
     //EvalNode exp
     KeywordSearchExpression exp
     ) {
     // find SLCA
     Stopwatch sw = new Stopwatch();
     sw.start();
     Set<NodeId> resultSet = exp.evaluate();
     List keywords = exp.getKeywords();
     //Set<NodeId> resultSet = slcaFinder.getSLCA(keywords);
     sw.stop();
     this.slcaTime = sw.readTime();
     sw.reset();
     System.out.println(resultSet.size() + " results found");
     // build result
     if (verbose) {
     for (NodeId nodeId : resultSet) {
     System.out.println("Curt: search " + nodeId);
     }
     }
     JTree resultTree = this.resultBuilder.buildResultTree(resultSet, (String[]) keywords.toArray());
     SearchResult result = new SearchResult(resultTree, this.slcaTime, resultSet.size());
     return result;
     }
     */

    /*
     Perform the search given the searchTokens
     */
    public SearchResult search( //String[] searchTokens
            //KeywordSearchExpression exp
            ) {

        // find SLCA
        Stopwatch sw = new Stopwatch();
        sw.start();
        Set<NodeId> resultSet = slcaFinder.getSLCA(exp);
        sw.stop();
        this.slcaTime = sw.readTime();
        System.out.println(resultSet.size() + " results found");
        sw.reset();
        // build result
        if (verbose) {
            for (NodeId nodeId : resultSet) {
                System.out.println("Curt: search " + nodeId);
            }
        }
        sw.start();
        JTree resultTree = this.resultBuilder.buildResultTree(resultSet, exp.getKeywordsAsArray());
        sw.stop();
        System.out.println("SLCA time " + this.slcaTime + "ms");
        System.out.println("Result time " + sw.readTime() + "ms");
        SearchResult result = new SearchResult(resultTree, this.slcaTime, resultSet.size());
        return result;
    }

    public double getSearchTime() {
        return this.slcaTime;
    }
}
