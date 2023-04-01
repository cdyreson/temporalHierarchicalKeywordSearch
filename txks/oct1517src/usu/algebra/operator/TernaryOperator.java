package usu.algebra.operator;

import usu.algebra.evaluate.EvalNode;
import usu.algebra.operator.Operator;
import usu.temporal.Time;
import java.util.List;
import usu.NodeId;

/**
 * The SequencedResult operator finds sequenced results
 *
 * @author Curt
 */
public abstract class TernaryOperator implements Operator {
    Time time;
    public Operator op1;
    public Operator op2;
   
    public EvalNode evaluate() {
        if (verbose) System.out.println("TernaryOperator Evaluate " + this.getClass());
        return null;
    }

    public void buildMatchList(List<String> toBuild) {
        if (verbose) System.out.println("TernaryOperator BuildMatchList " + this.getClass());
    }
}
