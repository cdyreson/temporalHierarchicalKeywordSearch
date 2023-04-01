/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messiah.search.slca.generic;

import java.util.Set;
import usu.NodeId;
import usu.algebra.KeywordSearchExpression;

/**
 *
 * @author truongbaquan
 */
public abstract class SLCAFinder {
    
    public abstract Set<NodeId> getSLCA(KeywordSearchExpression exp);
    
}
