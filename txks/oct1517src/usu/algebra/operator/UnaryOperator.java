package usu.algebra.operator;

import usu.algebra.evaluate.EvalNode;
import usu.algebra.operator.Operator;
import java.util.List;

/**
 * The SequencedResult operator finds sequenced results
 *
 * @author Curt
 */
public abstract class UnaryOperator implements Operator {
    public Operator child;
    
    @Override
    public EvalNode evaluate() {
        if (verbose) System.out.println("UnaryOperator Evaluate " + child);
        return child.evaluate();
    }

    @Override
    public void buildMatchList(List<String> toBuild) {
        if (verbose) System.out.println("UnaryOperator BuildMatchList " + child);
        child.buildMatchList(toBuild);
    }
}
