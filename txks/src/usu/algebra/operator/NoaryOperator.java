package usu.algebra.operator;

import usu.algebra.evaluate.EvalNode;
import java.util.List;

/**
 * The NoaryOperator is an operator without a common argument
 *
 * @author Curt
 */
public abstract class NoaryOperator implements Operator {
    
    @Override
    public EvalNode evaluate() {
        if (verbose) System.out.println("NoOperator Evaluate " + this.getClass());
        return null;
    }

    @Override
    public void buildMatchList(List<String> toBuild) {
        if (verbose) System.out.println("NoOperator BuildMatchList " + this.getClass());
    }
}
