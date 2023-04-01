package messiah.search.generic;

import messiah.search.*;
import javax.swing.JTree;

/**
 * A search result
 * @author truongbaquan
 */
public class SearchResult {

    private final JTree resultTree;
    public JTree getResultTree() {
        return resultTree;
    }

    private final double searchTime; // in ms
    public double getSearchTime() {
        return searchTime;
    }

    private final int size;
    public int getSize() {
        return size;
    }
    
    public SearchResult(JTree resultTree, double searchTime, int size) {
        this.resultTree = resultTree;
        this.searchTime = searchTime;
        this.size = size;
    }

    
}
