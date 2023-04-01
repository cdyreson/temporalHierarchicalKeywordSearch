/*
 * Copyright (C) 2017 Curtis Dyreson
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package usu.algebra;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import messiah.search.SearchAlgoEnum;
import usu.temporal.TimeElement;
import usu.grammar.TXKSParser;
import usu.temporal.Time;

//import usu.grammar.TXKSParser;
/**
 * Expression is a keyword search expression
 *
 * @author Curt
 */
public class KeywordSearchExpression {

    Stack<Integer> stack;
    List<String> words;
    SearchAlgoEnum searchType = SearchAlgoEnum.NontemporalSearch;
    private int operand = -1;

    /*
     * Create an empty postfix expression
     */
    public KeywordSearchExpression() {
        initialize();
    }

    /*
     * Add a new word/operand to the expression
     */
    public KeywordSearchExpression(String word) {
        initialize();
        words.add(word.toLowerCase());

        stack.push(operand--);
    }

    /*
     * Add a new word/operand to the expression
     */
    public KeywordSearchExpression(String word1, Integer op, String word2) {
        initialize();
        words.add(word1.toLowerCase());
        words.add(word2.toLowerCase());
        stack.push(operand--);
        stack.push(operand--);
        stack.push(op);
    }

    /*
     * Add a new expression to the expression
     */
    public KeywordSearchExpression(String word, Integer op, KeywordSearchExpression exp) {
        initialize();
        words.add(word.toLowerCase());
        words.addAll(exp.words);
        stack.push(operand--);
        for (Integer i : exp.stack) {
            stack.push(i);
        }
        stack.push(op);
    }

    /*
     * Add a new expression to the expression
     */
    public KeywordSearchExpression(KeywordSearchExpression exp, Integer op, String word) {
        initialize();

        words.addAll(exp.words);
        words.add(word.toLowerCase());

        for (Integer i : exp.stack) {
            stack.push(i);
        }
        stack.push(operand--);
        stack.push(op);
    }

    /*
     * Add a new expression to the expression
     */
    public KeywordSearchExpression(KeywordSearchExpression exp1, Integer op, KeywordSearchExpression exp2) {
        initialize();
        words.addAll(exp1.words);
        words.addAll(exp2.words);

        for (Integer i : exp1.stack) {
            stack.push(i);
        }
        for (Integer i : exp2.stack) {
            stack.push(i);
        }
        stack.push(op);
    }

    public SearchAlgoEnum getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        switch (searchType.intValue()) {
            case TXKSParser.K_NONTEMPORAL:
                this.searchType = SearchAlgoEnum.NontemporalSearch;
                break;
            case TXKSParser.K_SEQUENCED:
                this.searchType = SearchAlgoEnum.SequencedSearch;
                break;
            case TXKSParser.K_NONSEQUENCED:
                this.searchType = SearchAlgoEnum.SequencedSearch;
                break;
            default: 
                System.err.println("Bad searh type in Keyword Expression");
                System.exit(-1);
        }
    }

    public Time evaluateTime(Time[] times) {
        int limit = times.length;
        // Sanity check - If no elements then return empty element
        if (limit == 0) {
            return new Time();
        }
        //TimeElement result = times[0];
        // Sanity check - If singleton return it
        if (limit == 1) {
            return times[0];
        }
        // Need to process the elements
        Iterator<Integer> iter = stack.iterator();
        Stack<Time> opStack = new Stack();
        Time e1, e2, e3;
        while (iter.hasNext()) {
            Integer op = iter.next();
            switch (op.intValue()) {
                case TXKSParser.K_INTERSECTS:
                    e2 = opStack.pop();
                    e1 = opStack.pop();
                    e3 = e1.intersection(e2);
                    if (e3 == null) {
                        return null;
                    }
                    opStack.push(e3);
                    break;

                case TXKSParser.K_CONTAINS:
                    e2 = opStack.pop();
                    e1 = opStack.pop();
                    e3 = e1.contains(e2);
                    if (e3 == null) {
                        return null;
                    }
                    opStack.push(e3);
                    break;

                default:
                    int i = (op.intValue() * -1) - 1;
                    opStack.push(times[i]);
            }
        }
        return opStack.pop();
    }

    public TimeElement evaluate2(Time[] times) {
        int limit = times.length;
        // Sanity check - If no elements then return empty element
        if (limit == 0) {
            return new TimeElement();
        }
        //TimeElement result = times[0];
        // Sanity check - If singleton return it
        if (limit == 1) {
            return new TimeElement(times[0]);
        }
        // Need to process the elements
        Iterator<Integer> iter = stack.iterator();
        Stack<TimeElement> opStack = new Stack();
        TimeElement e1, e2, e3;
        while (iter.hasNext()) {
            Integer op = iter.next();
            switch (op.intValue()) {
                case TXKSParser.K_INTERSECTS:
                    e2 = opStack.pop();
                    e1 = opStack.pop();
                    e3 = e1.intersection(e2);
                    opStack.push(e3);
                    break;

                case TXKSParser.K_CONTAINS:
                    e2 = opStack.pop();
                    e1 = opStack.pop();
                    e3 = e1.intersection(e2);
                    opStack.push(e3);
                    break;

                default:
                    int i = (op.intValue() * -1) - 1;
                    opStack.push(new TimeElement(times[i]));
            }
        }
        return opStack.pop();
    }

    public List<String> getKeywords() {
        return words;
    }

    public String[] getKeywordsAsArray() {
        String[] a = new String[words.size()];
        int i = 0;
        for (String word : words) {
            a[i++] = word;
        }
        return a;
    }

    private void initialize() {
        // searchType = TXKSParser.K_NONTEMPORAL;
        stack = new Stack();
        words = new ArrayList(10);
        operand = -1;
    }

}
