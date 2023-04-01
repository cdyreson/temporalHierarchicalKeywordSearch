package usu.algebra.operator.specific;

import usu.algebra.evaluate.EvalNode;
import usu.algebra.operator.UnaryOperator;
import usu.algebra.operator.Operator;
import java.util.List;
import usu.NodeId;

/**
 * The NontemporalSearch operator performs normal search
 *
 * @author Curt
 */
public class NontemporalSearch extends UnaryOperator implements Operator {

    public NontemporalSearch(Operator c) {
        child = c;
    }

    @Override
    public EvalNode evaluate() {
        super.evaluate();
        return child.evaluate();
    }

    @Override
    public void buildMatchList(List<String> toBuild) {
        super.buildMatchList(toBuild);
        child.buildMatchList(toBuild);
    }
}
