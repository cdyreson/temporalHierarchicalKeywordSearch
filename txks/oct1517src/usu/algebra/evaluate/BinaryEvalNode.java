/*
 * EvalNode represents a structure to evaluate after matches are performed
 */
package usu.algebra.evaluate;

import java.util.List;
import java.util.Set;
import usu.NodeId;
import usu.algebra.timeeval.AfterTimeEvalNode;
import usu.algebra.timeeval.TimeEvalNode;

/**
 *
 * @author cdy
 */
public abstract class BinaryEvalNode implements EvalNode {

    public EvalNode left;
    public EvalNode right;

    /*
     public BinaryEvalNode(EvalNode l, EvalNode r) {
     left = l;
     right = r;
     }
     */
    @Override
    public Set<NodeId> evaluate() {
        Set<NodeId> a = left.evaluate();
        a.addAll(right.evaluate());
        return a;
    }

    @Override
    public List<String> getKeywords() {
        List a = left.getKeywords();
        for (String s : right.getKeywords()) {
            a.add(s);
        }
        return a;
    }

    @Override
    public boolean isSequencedResult() {
        return left.isSequencedResult() || right.isSequencedResult();
    }

    @Override
    public boolean isNonsequencedResult() {
        return left.isNonsequencedResult() || right.isNonsequencedResult();
    }

    @Override
    public boolean isNonsequencedSearch() {
        return left.isNonsequencedSearch() || right.isNonsequencedSearch();
    }

    @Override
    public boolean isSequencedSearch() {
        return left.isSequencedSearch() || right.isSequencedSearch();
    }

    @Override
    /* By default, this does nothing */
    public TimeEvalNode getTimeEvalNode() {
        return null;
    }

}
