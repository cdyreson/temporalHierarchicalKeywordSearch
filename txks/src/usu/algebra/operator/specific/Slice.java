/*
 * The TemporalMatch operator sices the given sequence at a particular time
 */
package usu.algebra.operator.specific;

import usu.algebra.evaluate.EvalNode;
import usu.algebra.operator.UnaryOperator;
import usu.algebra.operator.Operator;
import java.util.*;
import usu.NodeId;
import usu.PathId;
import usu.temporal.Time;
import java.util.List;

/**
 *
 * The Slice takes a Time and find the nodes that match at that time
 *
 * @author Curt
 */
public class Slice extends UnaryOperator implements Operator {

    Time time;

    public Slice(Time t, Operator c) {
        time = t;
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
