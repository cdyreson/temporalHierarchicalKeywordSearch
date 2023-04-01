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
public class AfterTimeEvalNode implements TimeEvalNode {

    public AfterTimeEvalNode(TimeEvalNode left, TimeEvalNode right) {

    }

    public Set<NodeId> evaluate() {
        return null;
    }

}
