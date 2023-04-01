/*
 * EvalNode represents a structure to evaluate after matches are performed
 */
package usu.algebra.evaluate.specific;

import usu.algebra.evaluate.EvalNode;
import usu.algebra.evaluate.UnaryEvalNode;

/**
 * A Sequenced Result node
 * @author Curtis
 */
public class SequencedResultEvalNode extends UnaryEvalNode implements EvalNode {
    
    public SequencedResultEvalNode(EvalNode c) {
        child = c;
    }

    @Override
    public boolean isSequencedResult() {
        return true;
    }

    @Override
    public boolean isNonsequencedResult() {
        return false;
    }

}
