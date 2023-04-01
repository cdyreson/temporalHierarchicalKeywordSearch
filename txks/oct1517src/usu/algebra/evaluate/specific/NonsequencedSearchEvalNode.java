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
public class NonsequencedSearchEvalNode extends UnaryEvalNode implements EvalNode {
    
    public NonsequencedSearchEvalNode(EvalNode c) {
        child = c;
    }

    @Override
    public boolean isSequencedSearch() {
        return false;
    }

    @Override
    public boolean isNonsequencedSearch() {
        return true;
    }

}
