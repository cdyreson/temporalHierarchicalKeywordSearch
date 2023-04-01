package usu.algebra.operator.specific;

import usu.algebra.evaluate.EvalNode;
import usu.algebra.operator.UnaryOperator;
import usu.algebra.operator.Operator;
import usu.temporal.Time;
import java.util.List;
import usu.NodeId;
import usu.algebra.evaluate.specific.NonsequencedResultEvalNode;

/**
 * The NonsequencedResult operator finds nonsequenced results
 *
 * @author Curt
 */
public class NonsequencedResult extends UnaryOperator implements Operator {
    
    public NonsequencedResult (Operator c) {
        child = c;
    }
    
    @Override
    public EvalNode evaluate() {
        super.evaluate();
        return new NonsequencedResultEvalNode(child.evaluate());
    }

    @Override
    public void buildMatchList(List<String> toBuild) {
                super.buildMatchList(toBuild);
        child.buildMatchList(toBuild);
    }
}
