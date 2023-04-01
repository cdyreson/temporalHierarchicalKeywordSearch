/*
 * EvalNode represents a structure to evaluate after matches are performed
 */
package usu.algebra.evaluate.specific;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import usu.NodeId;
import usu.algebra.evaluate.EvalNode;
import usu.algebra.evaluate.UnaryEvalNode;
import usu.algebra.timeeval.TimeEvalNode;

/**
 *
 * @author Curtis
 */
public class MatchEvalNode extends UnaryEvalNode implements EvalNode {

    List<String> words;

    /*
     public MatchEvalNode(EvalNode c) {
     super(c);
     }
     */
    public MatchEvalNode(String s) {
        words = new ArrayList(1);
        words.add(s);
        child = null;
    }

    public MatchEvalNode(List<String> s) {
        words = s;
        child = null;
    }

    @Override
    public Set<NodeId> evaluate() {
        System.out.println("BAD ERROR in TemporalMatchEvalNode ");
        System.exit(-1);
        return null;
    }

    @Override
    public List<String> getKeywords() {
        return words;
    }

    @Override
    public TimeEvalNode getTimeEvalNode() {
        System.out.println("BAD ERROR in TemporalMatchEvalNode ");
        System.exit(-1);
        return null;
    }

}
