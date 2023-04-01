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
public class AnyTimeEvalNode implements TimeEvalNode {

    public AnyTimeEvalNode(TimeEvalNode left, TimeEvalNode right) {

    }

    public Set<NodeId> evaluate() {
        return null;
    }

}
