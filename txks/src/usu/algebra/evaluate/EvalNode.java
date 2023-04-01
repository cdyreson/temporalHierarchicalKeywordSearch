/*
 * EvalNode represents a structure to evaluate after matches are performed
 */
package usu.algebra.evaluate;

import java.util.List;
import java.util.Set;
import usu.NodeId;
import usu.algebra.timeeval.*;

/**
 *
 * @author cdy
 */
public interface EvalNode {
    boolean verbose = false;
    
    public Set<NodeId> evaluate();
    public List<String> getKeywords();
    public boolean isSequencedResult();
    public boolean isNonsequencedResult();
    public boolean isNonsequencedSearch();
    public boolean isSequencedSearch();
    public TimeEvalNode getTimeEvalNode(); 
    
}
