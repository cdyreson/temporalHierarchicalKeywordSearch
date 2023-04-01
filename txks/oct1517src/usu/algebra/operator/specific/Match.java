/*
 * The TemporalMatch operator sices the given sequence at a particular time
 */
package usu.algebra.operator.specific;

import java.util.List;
import java.util.ArrayList;
import usu.algebra.evaluate.EvalNode;
import usu.algebra.evaluate.specific.MatchEvalNode;
import usu.algebra.operator.NoaryOperator;
import usu.algebra.operator.Operator;

/**
 *
 * The Match Operator matches a keyword
 *
 * @author Curt
 */
public class Match extends NoaryOperator implements Operator {

    List<String> keywords = null;

    public Match(String s) {
        String keyword = s;
        // Strip leading and trailing quotes
        keyword.trim();
        keyword = keyword.replaceAll("^\"|\"$", "");
        keywords = new ArrayList(3);
    }

    public Match(List<String> a) {
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
        return new MatchEvalNode(keywords);
    }

    @Override
    public void buildMatchList(List<String> toBuild) {
        super.buildMatchList(toBuild);
        for (String keyword : keywords) {
            toBuild.add(keyword);
        }
    }
}
