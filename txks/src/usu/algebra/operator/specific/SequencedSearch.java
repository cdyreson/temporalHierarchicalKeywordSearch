package usu.algebra.operator.specific;

import java.util.List;
import usu.NodeId;
import usu.algebra.evaluate.EvalNode;
import usu.algebra.evaluate.specific.SequencedSearchEvalNode;
import usu.algebra.operator.Operator;
import usu.algebra.operator.UnaryOperator;
import usu.temporal.Time;

/**
 * The SequencedSearch operator performs sequenced search
 *
 * @author Curt
 */
public class SequencedSearch extends UnaryOperator implements Operator {

    public SequencedSearch(Operator c) {
        child = c;
    }

    @Override
    public EvalNode evaluate() {
        super.evaluate();
        return new SequencedSearchEvalNode(child.evaluate());
    }

    @Override
    public void buildMatchList(List<String> toBuild) {
        super.buildMatchList(toBuild);
        child.buildMatchList(toBuild);
    }
}
