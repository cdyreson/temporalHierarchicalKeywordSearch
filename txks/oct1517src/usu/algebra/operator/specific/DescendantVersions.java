package usu.algebra.operator.specific;

import usu.algebra.evaluate.EvalNode;
import usu.algebra.operator.UnaryOperator;
import usu.algebra.operator.Operator;
import java.util.List;

/**
 * The DescendantVersions operator finds versioned results
 *
 * @author Curt
 */
public class DescendantVersions extends UnaryOperator implements Operator {

    public DescendantVersions(Operator c) {
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
