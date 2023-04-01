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
 * Class to manage a Branch in a tree.  The Branch records which levels above
 * a node are "new" in the keyword search for a node.
 * @author Curtis Dyreson
 */
public class Branch implements Serializable {
    NodeId nodeId;
    int level;
    List<Integer> newAtLevels;
    
    /**
     * Create a new Branch.  A Branch is which new levels does this node 
     * represent in the ancestors for the list of nodes that match this keyword,
     * So for the first "foo" with nodeId 1.1.1.1, then it represents the first
     * "foo" at all the levels above it.  If the next "foo" is at 1.1.2.1 then 
     * that would be the first "foo" at level 2 (and 3, 4, etc.)
     * @param nodeId
     * @param levels
     */
    public Branch(NodeId nodeId, int level, List<Integer> levels) {
        this.nodeId = nodeId;
        this.level = level;
        this.newAtLevels = levels;
    }
    
}
