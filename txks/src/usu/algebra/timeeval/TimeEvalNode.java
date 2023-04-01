/*
 * EvalNode represents a structure to evaluate after matches are performed
 */
package usu.algebra.timeeval;

import java.util.Set;
import usu.NodeId;


/**
 *
 * @author cdy
 */
public interface TimeEvalNode {
    
    public Set<NodeId> evaluate();
    
}
