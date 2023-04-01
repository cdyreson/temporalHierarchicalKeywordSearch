package messiah.search.slca.generic;

//import messiah.search.slca.*;
import java.io.Closeable;
import java.util.Iterator;
import usu.NodeId;
import usu.PathId;

/**
 *
 * @author truongbaquan
 */
public interface MatchAccessor extends Closeable {
    
    public NodeId[] getPrevNext(NodeId nodeId, int index);
    
    public MatchIterator getMatchIterator(int index);
    
    interface MatchIterator extends Iterator<NodeId> {
        
        public PathId getPathId();
    }
    
    @Override
    public void close();
}
