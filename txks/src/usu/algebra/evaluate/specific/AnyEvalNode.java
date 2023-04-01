/*
 * EvalNode represents a structure to evaluate after matches are performed
 */
package usu.algebra.evaluate.specific;

import java.util.List;
import java.util.Set;
import usu.NodeId;
import usu.algebra.evaluate.BinaryEvalNode;
import usu.algebra.evaluate.EvalNode;
import usu.algebra.timeeval.AfterTimeEvalNode;
import usu.algebra.timeeval.AnyTimeEvalNode;
import usu.algebra.timeeval.TimeEvalNode;

/**
 *
 * @author Curtis
 */
public class AnyEvalNode extends BinaryEvalNode implements EvalNode {

    public AnyEvalNode(EvalNode l, EvalNode r) {
        left = l;
        right = r;
    }

    @Override
    public TimeEvalNode getTimeEvalNode() {
        return new AnyTimeEvalNode(left.getTimeEvalNode(), right.getTimeEvalNode());
    }

}
