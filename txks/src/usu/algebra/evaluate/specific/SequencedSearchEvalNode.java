/*
 * EvalNode represents a structure to evaluate after matches are performed
 */
package usu.algebra.evaluate.specific;

import usu.algebra.evaluate.EvalNode;
import usu.algebra.evaluate.UnaryEvalNode;

/**
 * A Sequenced Search node
 * @author Curtis
 */
public class SequencedSearchEvalNode extends UnaryEvalNode implements EvalNode {
    
    public SequencedSearchEvalNode(EvalNode c) {
        child = c;
    }

    @Override
    public boolean isSequencedSearch() {
        return true;
    }

    @Override
    public boolean isNonsequencedSearch() {
        return false;
    }

}
