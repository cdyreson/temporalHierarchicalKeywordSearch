/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usu.algebra.operator;

import usu.algebra.evaluate.EvalNode;
import java.util.List;
import usu.NodeId;

/**
 * An Operator is some function in the search engine language, such
 * as match, as in match a string to some nodes
 * @author Curt
 */
public interface Operator {
    public boolean verbose = false;

    /*
     Evaluate the operator
     */
    public EvalNode evaluate();
    
    public void buildMatchList(List<String> toBuild);
    

}
