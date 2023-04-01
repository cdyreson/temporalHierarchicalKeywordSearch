package messiah.parse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import messiah.utils.Counter;

/**
 * This object is used to read all unique root-to-node paths in the document
 *
 * @author John Truong Ba Quan
 */
public class PathsReader implements ParserListener {

    private final Stack<LabelSet> globalChildLabels = new Stack<>();
    private final Stack<HashSet<String>> localChildLabels = new Stack<>();
    private final LabelSet rootLabelSet;
    private boolean processingValue = false; // indicating whether the current node is value or not

    public SortedMap<String, LabelAppearanceInfo> getPathExprs() {
        SortedMap<String, LabelAppearanceInfo> res = new TreeMap<>();
        Stack<LabelSet> stack = new Stack<>();
        Stack<String> pathExprStack = new Stack<>();
        stack.push(rootLabelSet);
        while (!stack.isEmpty()) {
            LabelSet curLabelSet = stack.pop();
            String curPathExpr = pathExprStack.isEmpty() ? "" : pathExprStack.pop();
            for (String childLabel : curLabelSet.childLabels.keySet()) {
                String childPathExpr = curPathExpr + "." + childLabel + "#";
                pathExprStack.push(childPathExpr);
                LabelSet childLabelSet = curLabelSet.childLabels.get(childLabel);
                res.put(childPathExpr, new LabelAppearanceInfo(childLabelSet.counter.getValue(), childLabelSet.parentAppCounter.getValue()));
                stack.push(childLabelSet);
            }
        }
        return res;
    }

    public SortedSet<String> getRepeatablePathExprs() {
        SortedSet<String> res = new TreeSet<>();
        Stack<LabelSet> stack = new Stack<>();
        Stack<String> pathExprStack = new Stack<>();
        stack.push(rootLabelSet);
        while (!stack.isEmpty()) {
            LabelSet curLabelSet = stack.pop();
            String curPathExpr = pathExprStack.isEmpty() ? "" : pathExprStack.pop();
            for (String childLabel : curLabelSet.childLabels.keySet()) {
                String childPathExpr = curPathExpr + "." + childLabel + "#";
                pathExprStack.push(childPathExpr);
                if (curLabelSet.repeatableChildLabels.contains(childLabel)) {
                    res.add(childPathExpr);
                }
                LabelSet childLabelSet = curLabelSet.childLabels.get(childLabel);
                stack.push(childLabelSet);
            }
        }
        return res;
    }

    public PathsReader() {
        this.rootLabelSet = new LabelSet();
        this.globalChildLabels.add(this.rootLabelSet);
        this.localChildLabels.add(new HashSet<String>());
    }

    @Override
    public void startDocument() {
    }

    @Override
    public void endDocument() {
    }

    @Override
    public void start(String str, boolean isAttribute, boolean isValue) {
        if (!isValue) {
            if (isAttribute) {
                str = '@' + str;
            }

            // update the local child labels and the global repeatable child labels
            // note: the local child label is used to check for repeatable child labels
            if (!localChildLabels.peek().contains(str)) {
                localChildLabels.peek().add(str);
            } else {
                globalChildLabels.peek().repeatableChildLabels.add(str);
            }

            // update the global child labels and get the entry for pushing to stack
            // note: local child labels' new entry is always new and empty but global one may be old
            LabelSet childLabelSet;
            if (!globalChildLabels.peek().childLabels.containsKey(str)) {
                childLabelSet = new LabelSet();
                globalChildLabels.peek().childLabels.put(str, childLabelSet);
            } else {
                childLabelSet = globalChildLabels.peek().childLabels.get(str);
            }
            childLabelSet.counter.increment();

            // update the two stacks
            localChildLabels.push(new HashSet<String>());
            globalChildLabels.push(childLabelSet);
        } else {
            this.processingValue = true;
        }
    }

    @Override
    public void end(boolean isAttribute, boolean isValue) {
        if (!this.processingValue) {
            HashSet<String> childLabels = localChildLabels.pop();
            LabelSet labelSet = globalChildLabels.pop();
            for (String childLabel : childLabels) {
                LabelSet childLabelSet = labelSet.childLabels.get(childLabel);
                childLabelSet.parentAppCounter.increment();
            }
        } else {
            this.processingValue = false;
        }
    }

    /**
     * The set of all child labels and repeatable child labels of a root-to-node
     * path
     */
    private class LabelSet {
        /**
         * The number of appearances of this root-to-node path
         */
        private Counter counter = new Counter();
        /**
         * The number of appearances of this root-to-node path in its parent
         */
        private Counter parentAppCounter = new Counter();
        private HashMap<String, LabelSet> childLabels = new HashMap<>();
        private HashSet<String> repeatableChildLabels = new HashSet<>();
    }
}
