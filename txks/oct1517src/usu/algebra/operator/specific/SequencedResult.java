package usu.algebra.operator.specific;

import java.util.List;
import usu.NodeId;
import usu.algebra.evaluate.EvalNode;
import usu.algebra.evaluate.specific.SequencedResultEvalNode;
import usu.algebra.operator.Operator;
import usu.algebra.operator.UnaryOperator;
import usu.temporal.Time;

/**
 * The SequencedResult operator finds sequenced results
 *
 * @author Curt
 */
public class SequencedResult extends UnaryOperator implements Operator {

    public SequencedResult(Operator c) {
        child = c;
    }

    @Override
    public EvalNode evaluate() {
        super.evaluate();
        return new SequencedResultEvalNode(child.evaluate());
    }

    @Override
    public void buildMatchList(List<String> toBuild) {
        super.buildMatchList(toBuild);
        child.buildMatchList(toBuild);
    }
}
