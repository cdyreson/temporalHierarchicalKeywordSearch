/*
 * EvalNode represents a structure to evaluate after matches are performed
 */
package usu.algebra.evaluate;

import java.util.List;
import java.util.Set;
import usu.NodeId;
import usu.algebra.timeeval.TimeEvalNode;

/**
 *
 * @author cdy
 */
public abstract class UnaryEvalNode implements EvalNode {

    public EvalNode child;

    /*
     public UnaryEvalNode(EvalNode c) {
     child = c;
     }
     */
    @Override
    public Set<NodeId> evaluate() {
        return child.evaluate();
    }

    @Override
    public List<String> getKeywords() {
        return child.getKeywords();
    }

    @Override
    public boolean isSequencedResult() {
        if (child == null) {
            return false;
        }
        return child.isSequencedResult();
    }

    @Override
    public boolean isNonsequencedResult() {
        if (child == null) {
            return false;
        }
        return child.isNonsequencedResult();
    }

    @Override
    public boolean isNonsequencedSearch() {
        if (child == null) {
            return false;
        }
        return child.isNonsequencedSearch();
    }

    @Override
    public boolean isSequencedSearch() {
        if (child == null) {
            return false;
        }
        return child.isSequencedSearch();
    }

    @Override
    public TimeEvalNode getTimeEvalNode() {
        if (child == null) {
            return null;
        }
        return child.getTimeEvalNode();
    }

}
