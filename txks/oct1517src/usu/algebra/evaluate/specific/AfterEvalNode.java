/*
 * EvalNode represents a structure to evaluate after matches are performed
 */
package usu.algebra.evaluate.specific;

import usu.algebra.evaluate.BinaryEvalNode;
import usu.algebra.evaluate.EvalNode;
import usu.algebra.timeeval.AfterTimeEvalNode;
import usu.algebra.timeeval.TimeEvalNode;

/**
 *
 * @author Curtis
 */
public class AfterEvalNode extends BinaryEvalNode implements EvalNode {

    public AfterEvalNode(EvalNode l, EvalNode r) {
        left = l;
        right = r;
    }

    @Override
    public TimeEvalNode getTimeEvalNode() {
        return new AfterTimeEvalNode(left.getTimeEvalNode(), right.getTimeEvalNode());
    }

}
