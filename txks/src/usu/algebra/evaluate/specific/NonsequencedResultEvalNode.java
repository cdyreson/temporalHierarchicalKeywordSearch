/*
 * EvalNode represents a structure to evaluate after matches are performed
 */
package usu.algebra.evaluate.specific;

import usu.algebra.evaluate.EvalNode;
import usu.algebra.evaluate.UnaryEvalNode;

/**
 * A Nonsequenced Result node
 * @author Curtis
 */
public class NonsequencedResultEvalNode extends UnaryEvalNode implements EvalNode {
    
    public NonsequencedResultEvalNode(EvalNode c) {
        child = c;
    }

    @Override
    public boolean isSequencedResult() {
        return false;
    }

    @Override
    public boolean isNonsequencedResult() {
        return true;
    }

}
