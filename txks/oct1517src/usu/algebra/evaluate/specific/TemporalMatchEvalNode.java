/*
 * TemporalMatchEvalNode represents a structure to evaluate after matches are performed
 */
package usu.algebra.evaluate.specific;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import usu.NodeId;
import usu.algebra.evaluate.EvalNode;
import usu.algebra.evaluate.UnaryEvalNode;
import usu.algebra.timeeval.TimeEvalNode;
import usu.temporal.Time;

/**
 *
 * @author Curtis
 */
public class TemporalMatchEvalNode extends UnaryEvalNode implements EvalNode {

    List<String> words;

    Time t = null;

    /*
     public MatchEvalNode(EvalNode c) {
     super(c);
     }
     */
    public TemporalMatchEvalNode(Time t, String s) {
        words = new ArrayList(1);
        words.add(s);
        child = null;
    }

    public TemporalMatchEvalNode(Time t, List<String> s) {
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
        List<String> a = new ArrayList(1);
        return words;
    }

    @Override
    public TimeEvalNode getTimeEvalNode() {
        System.out.println("BAD ERROR in TemporalMatchEvalNode ");
        System.exit(-1);
        return null;
    }

}
