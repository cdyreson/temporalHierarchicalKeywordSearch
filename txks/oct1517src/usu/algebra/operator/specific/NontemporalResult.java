package usu.algebra.operator.specific;

import usu.algebra.evaluate.EvalNode;
import usu.algebra.operator.UnaryOperator;
import usu.algebra.operator.Operator;
import java.util.List;

/**
 * The NontemproalResult operator finds normal results
 *
 * @author Curt
 */
public class NontemporalResult extends UnaryOperator implements Operator {

    public NontemporalResult(Operator c) {
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
