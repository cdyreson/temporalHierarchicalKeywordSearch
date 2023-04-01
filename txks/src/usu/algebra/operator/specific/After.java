package usu.algebra.operator.specific;

import usu.algebra.evaluate.EvalNode;
import usu.algebra.evaluate.specific.AfterEvalNode;
import usu.algebra.operator.Operator;
import usu.algebra.operator.BinaryOperator;
import usu.temporal.Time;
import java.util.List;
import usu.NodeId;

/**
 *
 * The After operator checks to ensure one operand is after another
 *
 * @author Curt
 */
public class After extends BinaryOperator implements Operator {

    public After(Operator op1, Operator op2) {
        this.op1 = op1;
        this.op2 = op2;
    }

    public EvalNode evaluate() {
        super.evaluate();
        return new AfterEvalNode(op1.evaluate(), op2.evaluate());
    }

    public void buildMatchList(List<String> toBuild) {
        super.buildMatchList(toBuild);
        op1.buildMatchList(toBuild);
        op2.buildMatchList(toBuild);
    }
}
