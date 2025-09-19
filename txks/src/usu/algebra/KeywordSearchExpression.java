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
    Time sliceTime = new Time();
    private int operand = -1;
    private final boolean verbose = false;

    /*
     * Create an empty postfix expression
     */
    public KeywordSearchExpression() {
        initialize();
    }

    /*
     * Add a new word/operand to the expression
     */
    public KeywordSearchExpression(String word, int count) {
        initialize();
        if (verbose) System.out.println("KeywordSearchExpression: constructor word " + word);
        words.add(word.toLowerCase());

        stack.push(count);
    }

    /*
     * Add a new word/operand to the expression
     */
    public KeywordSearchExpression(String word1, Integer op, String word2, int count1, int count2) {
        initialize();
        if (verbose) System.out.println("KeywordSearchExpression: constructor word op word " + word1 + " " + op + " " + word2);
        words.add(word1.toLowerCase());
        words.add(word2.toLowerCase());
        stack.push(count1);
        stack.push(count2);
        stack.push(op);
    }

    /*
     * Add a new slice expression to the expression
     */
    public KeywordSearchExpression(Time t, KeywordSearchExpression exp) {
        initialize();
        if (verbose) System.out.println("KeywordSearchExpression: constructor slice time exp " + t + " " + TXKSParser.K_SLICE);

        sliceTime = t;
        words.addAll(exp.words);

        for (Integer i : exp.stack) {
            stack.push(i);
        }
        //stack.push(count);
        stack.push(TXKSParser.K_SLICE);
    }
    
    /*
     * Add a new expression to the expression
     */
    public KeywordSearchExpression(String word, Integer op, KeywordSearchExpression exp, int count) {
        initialize();
        if (verbose) System.out.println("KeywordSearchExpression: constructor word op exp " + word + " " + op);

        words.add(word.toLowerCase());
        words.addAll(exp.words);
        stack.push(count);
        for (Integer i : exp.stack) {
            stack.push(i);
        }
        stack.push(op);
    }

    /*
     * Add a new expression to the expression
     */
    public KeywordSearchExpression(KeywordSearchExpression exp, Integer op, String word, int count) {
        initialize();
        if (verbose) System.out.println("KeywordSearchExpression: constructor exp op word " +  op + " " + word);

        words.addAll(exp.words);
        words.add(word.toLowerCase());

        for (Integer i : exp.stack) {
            stack.push(i);
        }
        stack.push(count);
        stack.push(op);
    }

    /*
     * Add a new expression to the expression
     */
    public KeywordSearchExpression(KeywordSearchExpression exp1, Integer op, KeywordSearchExpression exp2) {
        initialize();
        if (verbose) System.out.println("KeywordSearchExpression: constructor exp op exp " + op);
        
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
        //if (verbose) System.out.println("KeywordSearchExpression.java: searchType is " + searchType);
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        switch (searchType.intValue()) {
            case TXKSParser.K_NONTEMPORAL:
                //System.out.println("KeywordSearchExpression: Search type is NONTemporal");
                this.searchType = SearchAlgoEnum.NontemporalSearch;
                break;
            case TXKSParser.K_SEQUENCED:
                //System.out.println("KeywordSearchExpression: Search type is SQ");
                this.searchType = SearchAlgoEnum.SequencedSearch;
                break;
            case TXKSParser.K_NONSEQUENCED:
                //System.out.println("KeywordSearchExpression: Search type is NONSQ");
                this.searchType = SearchAlgoEnum.TemporalSearch;
                break;
            case TXKSParser.K_EARLIEST: 
                this.searchType = SearchAlgoEnum.EarliestSearch;
                break;
            case TXKSParser.K_LATEST: 
                this.searchType = SearchAlgoEnum.LatestSearch;
                break;
            case TXKSParser.K_DURATION: 
                this.searchType = SearchAlgoEnum.DurationalSearch;
                break;
            default:
                System.err.println("Bad searh type in Keyword Expression");
                System.exit(-1);
        }
    }

    public Time evaluateTime(Time[] times) {
        if (verbose) System.out.println("KeywordSearchExpression.java: evaluating " + times.length);
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
            if (verbose) System.out.println("KeywordSearchExpression.java: switching on " + op);
            switch (op) {
                case TXKSParser.K_SLICE:
                    e1 = opStack.pop();
                    e3 = e1.intersection(sliceTime);
                    System.out.println("Sliced time " + e3 + " " + sliceTime);
                    if (e3 == null) {
                        return null;
                    }
                    opStack.push(e3);
                    break;
                
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
                  
                case TXKSParser.K_DURING:
                    e2 = opStack.pop();
                    e1 = opStack.pop();
                    e3 = e2.contains(e1);
                    if (e3 == null) {
                        return null;
                    }
                    opStack.push(e3);
                    break;
                    
                case TXKSParser.K_MEETS:
                    e2 = opStack.pop();
                    e1 = opStack.pop();
                    if (!e1.meets(e2)) {
                        return null;
                    }
                    opStack.push(e1);
                    break;

                default:
                    int i = (op * -1) - 1;
                    opStack.push(times[i]);
            }
        }
        return opStack.pop();
    }

    public TimeElement evaluate(TimeElement[] times) {
        if (verbose) System.out.println("KeywordSearchExpression.java: evaluating timeElement " + times.length);
        if (verbose) {
            for (TimeElement time : times) {
                System.out.println("KeywordSearchExpression.java: times " + time);
            }
            for (int op: stack) {
                System.out.println("KeywordSearchExpression.java: op " + op);
            }
        }
        int limit = times.length;
        // Sanity check - If no elements then return empty element
        if (limit == 0) {
            return new TimeElement();
        }
        //TimeElement result = times[0];
        // Sanity check - If singleton return it
        // Not needed is slicing is in force
        /*
        if (limit == 1) {
            return times[0];
        }
        */
        // Need to process the elements
        Iterator<Integer> iter = stack.iterator();
        Stack<TimeElement> opStack = new Stack();
        TimeElement e1, e2, e3;
        while (iter.hasNext()) {
            Integer op = iter.next();
            if (verbose) System.out.println("KeywordSearchExpression.java: switch on " + op + " "
                    + stack.size() + " " + TXKSParser.K_SLICE);
            
            switch (op) {
                case TXKSParser.K_SLICE:
                    e1 = opStack.pop();
                    e3 = e1.intersection(sliceTime);
                    System.out.println("Sliced time element " + e3 + " " + sliceTime);
                    opStack.push(e3);
                    break;
                    
                case TXKSParser.K_INTERSECTS:
                    e2 = opStack.pop();
                    e1 = opStack.pop();
                    e3 = e1.intersection(e2);
                    opStack.push(e3);
                    break;

                case TXKSParser.K_CONTAINS:
                    e2 = opStack.pop();
                    e1 = opStack.pop();
                    e3 = e1.contains(e2);
                    opStack.push(e3);
                    break;

                case TXKSParser.K_MEETS:
                    e2 = opStack.pop();
                    e1 = opStack.pop();
                    e3 = e1.meets(e2);
                    opStack.push(e3);
                    break;
                    
                default:
                    int i = (op * -1) - 1;
                    opStack.push(times[i]);
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
