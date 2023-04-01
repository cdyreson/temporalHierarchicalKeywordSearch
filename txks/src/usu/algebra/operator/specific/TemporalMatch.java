/*
 * The TemporalMatch operator sices the given sequence at a particular time
 */
package usu.algebra.operator.specific;

import java.util.*;
import java.util.List;
import usu.NodeId;
import usu.PathId;
import usu.algebra.evaluate.EvalNode;
import usu.algebra.evaluate.specific.TemporalMatchEvalNode;
import usu.algebra.operator.NoaryOperator;
import usu.algebra.operator.Operator;
import usu.temporal.Time;

/**
 *
 * The temporal match takes a Time and find the nodes that match at that time,
 * that is, it is a Slice.
 *
 * @author Curt
 */
public class TemporalMatch extends NoaryOperator implements Operator {

    Time time;
    List<String> keywords = null;

    public TemporalMatch(Time t, String s) {
        time = t;
        String keyword = s;
        // Strip leading and trailing quotes
        keyword.trim();
        keyword = keyword.replaceAll("^\"|\"$", "");
        keywords = new ArrayList(3);
    }

    public TemporalMatch(Time t, List<String> a) {
        time = t;
        // Clean up each keyword in list
        keywords = new ArrayList(3);
        for (String keyword : a) {
            // Strip leading and trailing quotes
            keyword.trim();
            keyword = keyword.replaceAll("^\"|\"$", "");
            keywords.add(keyword);
        }
    }

    @Override
    public EvalNode evaluate() {
        super.evaluate();
        return new TemporalMatchEvalNode(time, keywords);
    }

    @Override
    public void buildMatchList(List<String> toBuild) {
        super.buildMatchList(toBuild);
        for (String keyword : keywords) {
            toBuild.add(keyword);
        }
    }
}
