package messiah.search.slca.generic;

//import messiah.search.slca.*;
import java.util.SortedMap;
import messiah.TermNodeKey;
import usu.PathId;
import usu.NodeId;
import usu.NodeIdFactory;
import usu.dln.DLNFactory;

/**
 *
 * @author truongbaquan
 */
class IndexedMatchAccessor implements MatchAccessor {

    private final String[] query;
    private final SortedMap<TermNodeKey, PathId> termNodeIndex;
    private static final NodeIdFactory nodeIdFactory = new DLNFactory();

    public IndexedMatchAccessor(String[] query, SortedMap<TermNodeKey, PathId> termNodeIndex) {
        System.out.println(this.getClass());
        this.query = query;
        this.termNodeIndex = termNodeIndex;
        //this.cursorManager = new CursorManager(termNodeIndex, query.length);
    }

    @Override
    public NodeId[] getPrevNext(NodeId nodeId, int index) {
        NodeId[] results = new NodeId[2];
        TermNodeKey keyRight = new TermNodeKey(query[index], nodeId);
        SortedMap<TermNodeKey, PathId> m = termNodeIndex.tailMap(keyRight);
        keyRight = termNodeIndex.tailMap(keyRight).firstKey();
        TermNodeKey keyLeft = termNodeIndex.headMap(keyRight).lastKey();
        results[1] = keyRight.getNodeId();
        results[0] = keyLeft.getNodeId();
        return results;
    }

    @Override
    public MatchIterator getMatchIterator(int index) {
        return new IndexedMatchIterator(index);
    }

    private class IndexedMatchIterator implements MatchIterator {

        private int index;
        private NodeId nodeId;
        private PathId pathId;
        private NodeId lastNodeId;
        private PathId lastPathId;

        public IndexedMatchIterator(int index) {
            this.index = index;
            TermNodeKey key = new TermNodeKey(query[index], nodeIdFactory.rootId());
            retrieveData(key);
        }

        @Override
        public boolean hasNext() {
            return nodeId != null;
        }

        @Override
        public NodeId next() {
            // get the pre-stored data
            lastNodeId = nodeId;
            lastPathId = pathId;
            // move the cursor
            // load the data
            //CURT THE FOLLOWING IS BROKEN
            //retrieveData(op, keyEntry, dataEntry);
            // return the pre-stored data
            return lastNodeId;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public PathId getPathId() {
            return lastPathId;
        }

        private void retrieveData(TermNodeKey key) {
            if (query[index].equals(key.getTerm())) {
                nodeId = key.getNodeId();
                pathId = termNodeIndex.get(key);
            } else {
                nodeId = null;
                pathId = null;
            }
        }
    }

    @Override
    public void close() {
    }
}

