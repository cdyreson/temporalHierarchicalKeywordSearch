/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usu.level;

import java.io.Serializable;
import java.util.List;
import usu.NodeId;

/**
 * Class to manage a pair of a preorder count and level.  
 * @author Curtis Dyreson
 */
public class PrefixPair implements Serializable {
    int id;
    int level;
    
    /**
     * Create a new pair.  A pair has an id (the preorder count for the node)
     * and a level at which the node exists.
     * @param nodeId
     * @param levels
     */
    public PrefixPair(int id, int level) {
        this.id = id;
        this.level = level;
    }
    
}
