package usu.algebra.operator.specific;

import java.util.List;
import usu.NodeId;
import usu.algebra.evaluate.EvalNode;
import usu.algebra.evaluate.specific.NonsequencedSearchEvalNode;
import usu.algebra.operator.Operator;
import usu.algebra.operator.UnaryOperator;
import usu.temporal.Time;

/**
 * The NonsequencedSearch operator performs nonsequenced search
 *
 * @author Curt
 */
public class NonsequencedSearch extends UnaryOperator implements Operator {
    
    public NonsequencedSearch (Operator c) {
        child = c;
    }
    
    @Override
    public EvalNode evaluate() {
        super.evaluate();
        return new NonsequencedSearchEvalNode(child.evaluate());
    }

    @Override
    public void buildMatchList(List<String> toBuild) {
                super.buildMatchList(toBuild);
        child.buildMatchList(toBuild);
    }
}
