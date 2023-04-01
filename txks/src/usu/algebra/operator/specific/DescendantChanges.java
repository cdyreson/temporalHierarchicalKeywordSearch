package usu.algebra.operator.specific;

import usu.algebra.evaluate.EvalNode;
import usu.algebra.operator.UnaryOperator;
import usu.algebra.operator.Operator;
import java.util.List;

/**
 * The DescendantChanges operator finds changes
 *
 * @author Curt
 */
public class DescendantChanges extends UnaryOperator implements Operator {

    public DescendantChanges(Operator c) {
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
