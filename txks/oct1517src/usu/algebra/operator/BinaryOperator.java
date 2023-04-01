package usu.algebra.operator;

import usu.algebra.evaluate.EvalNode;
import java.util.List;

/**
 * The BinaryOperator abstract class implements a binary operator
 *
 * @author Curt
 */
public abstract class BinaryOperator implements Operator {
    public Operator op1;
    public Operator op2;
    
    @Override
    public EvalNode evaluate() {
        if (verbose) System.out.println("BinaryOperator Evaluate " + this.getClass());
        return null;
    }

    @Override
    public void buildMatchList(List<String> toBuild) {
        if (verbose) System.out.println("BinaryOperator BuildMatchList " + this.getClass());
    }
}
