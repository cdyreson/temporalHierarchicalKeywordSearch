package usu.algebra.operator.specific;

import usu.algebra.evaluate.EvalNode;
import usu.algebra.evaluate.specific.AnyEvalNode;
import usu.algebra.operator.Operator;
import usu.algebra.operator.BinaryOperator;
import usu.temporal.Time;
import java.util.List;
import usu.NodeId;

/**
 *
 * The Any operator permits any relationship
 *
 * @author Curt
 */
public class Any extends BinaryOperator implements Operator {

    public Any(Operator op1, Operator op2) {
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public EvalNode evaluate() {
        super.evaluate();
        return new AnyEvalNode(op1.evaluate(), op2.evaluate());
    }

    @Override
    public void buildMatchList(List<String> toBuild) {
        super.buildMatchList(toBuild);
        op1.buildMatchList(toBuild);
        op2.buildMatchList(toBuild);
    }
}
