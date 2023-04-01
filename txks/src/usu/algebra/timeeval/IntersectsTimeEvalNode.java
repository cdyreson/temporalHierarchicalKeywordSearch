/*
 * EvalNode represents a structure to evaluate after matches are performed
 */
package usu.algebra.timeeval;

import java.util.Set;
import usu.NodeId;

/**
 *
 * @author Curtis
 */
public class IntersectsTimeEvalNode implements TimeEvalNode {
    TimeEvalNode left;
    TimeEvalNode right;

    public IntersectsTimeEvalNode(TimeEvalNode left, TimeEvalNode right) {
       this.left = left;
       this.right = right;
    }

    @Override
    public Set<NodeId> evaluate() {
        return null;
    }

}
