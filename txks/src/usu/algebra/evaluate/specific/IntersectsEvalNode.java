/*
 * EvalNode represents a structure to evaluate after matches are performed
 */
package usu.algebra.evaluate.specific;

import java.util.List;
import java.util.Set;
import usu.NodeId;
import usu.algebra.evaluate.BinaryEvalNode;
import usu.algebra.evaluate.EvalNode;
import usu.algebra.timeeval.IntersectsTimeEvalNode;
import usu.algebra.timeeval.TimeEvalNode;

/**
 *
 * @author cdy
 */
public class IntersectsEvalNode extends BinaryEvalNode implements EvalNode {

    public IntersectsEvalNode(EvalNode l, EvalNode r) {
        left = l;
        right = r;
    }

    /**
     * Evaluate the intersection, producing the result
     *
     * @return Set of result nodes
     */
    @Override
    public Set<NodeId> evaluate() {
        return left.evaluate();
    }

    @Override
    public TimeEvalNode getTimeEvalNode() {
        return new IntersectsTimeEvalNode(left.getTimeEvalNode(), right.getTimeEvalNode());
    }

}
