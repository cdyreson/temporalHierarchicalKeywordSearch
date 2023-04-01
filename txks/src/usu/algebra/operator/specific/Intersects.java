package usu.algebra.operator.specific;

import usu.algebra.evaluate.EvalNode;
import usu.algebra.evaluate.specific.IntersectsEvalNode;
import usu.algebra.operator.Operator;
import usu.algebra.operator.BinaryOperator;
import java.util.List;

/**
 *
 * The After operator checks to ensure one operand is after another
 *
 * @author Curt
 */
public class Intersects extends BinaryOperator implements Operator {

    public Intersects (Operator op1, Operator op2) {
        this.op1 = op1;
        this.op2 = op2;
    }
    
    @Override
    public EvalNode evaluate() {
        super.evaluate();
        return new IntersectsEvalNode(op1.evaluate(), op2.evaluate());
    }

    @Override
    public void buildMatchList(List<String> toBuild) {
                super.buildMatchList(toBuild);
        op1.buildMatchList(toBuild);
        op2.buildMatchList(toBuild);
    }
}
