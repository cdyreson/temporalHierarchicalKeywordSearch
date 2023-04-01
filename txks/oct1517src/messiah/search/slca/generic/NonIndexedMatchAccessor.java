package messiah.search.slca.generic;

//import messiah.search.slca.*;
import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import usu.NodeId;
import usu.PathId;

/**
 *
 * @author truongbaquan
 */
public class NonIndexedMatchAccessor implements MatchAccessor {
    
    private final SortedMap<NodeId,PathId>[] maps;
    
    public NonIndexedMatchAccessor(String[] query, Map<String,SortedMap<NodeId,PathId>> termMap) {
        System.out.println(this.getClass());
        maps = new TreeMap[query.length];
        for (int i = 0; i < query.length; i++) {
            maps[i] = termMap.get(query[i]);
        }
    }

    @Override
    public NodeId[] getPrevNext(NodeId nodeId, int index) {
        TreeMap<NodeId,PathId> localMap = (TreeMap)maps[index];
        return new NodeId[] { localMap.floorKey(nodeId), localMap.ceilingKey(nodeId) };
    }

    @Override
    public MatchIterator getMatchIterator(int index) {
        return new NonIndexedMatchIterator(index);
    }

    @Override
    public void close() {
    }
    
    private class NonIndexedMatchIterator implements MatchIterator {
        
        private final SortedMap<NodeId,PathId> map;
        private final Iterator<Entry<NodeId,PathId>> iter;
        
        private NodeId lastNodeId;
        private PathId lastPathId;
        
        public NonIndexedMatchIterator(int index) {
            map = maps[index];
            iter = map.entrySet().iterator();
        }

        @Override
        public PathId getPathId() {
            return lastPathId;
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public NodeId next() {
            Entry<NodeId,PathId> entry = iter.next();
            lastNodeId = entry.getKey();
            lastPathId = entry.getValue();
            return lastNodeId;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
}
