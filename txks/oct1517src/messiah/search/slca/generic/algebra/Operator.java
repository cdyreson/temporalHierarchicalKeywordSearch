package messiah.search.slca.generic.algebra;

import usu.NodeId;

/**
 *
 * @author Curt
 */
public interface Operator {

    /*
     Peek at the current LCA node in the sequence
     */
    public NodeId peekAtLCA();

    /*
     Peek at the current LCA node in the sequence
     */
    public NodeId peek();

    /* 
     Do we have more NodeIds?  
     */
    public boolean hasMore();

    /* 
     Set the level at which LCAs are produced  
     */
    public void setLevel(int it);

    /* 
     Advance this iterator past the given id, stop
     when advanced past it.  Does nothing if the
     iterator is already past or equal to it.
    
     Returns true (hasMore nodes)
     or false (no more nodes)
     */
    public boolean advancePast(NodeId id);

}
