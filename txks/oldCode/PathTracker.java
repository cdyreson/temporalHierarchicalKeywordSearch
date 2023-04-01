package messiah.parse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import messiah.Path;
import usu.PathId;

/**
 * This object tracks the path of the current node when traversing the XML document tree
 * @author John Truong Ba Quan
 */
public class PathTracker implements ParserListener {
    private PathExpReader pathParser = null;
    
    public PathExpReader getPathParser() {
        return this.pathParser;
    }
    
    private Stack<MapEntry> entryStack = new Stack();
    private MapEntry rootEntry = new MapEntry(new Path());
    private boolean processingValue = false; // indicating whether the current node is value or not

    public PathTracker(PathExpReader pathParser) {
        this.pathParser = pathParser;
        initializeEntryStructure();
    }
    
    private void initializeEntryStructure() {
        Map<String,MapEntry> pathExprMap = new HashMap<>();
        for (Path path : (Collection<Path>)this.pathParser.getPathsSortedByPathExpr()) {
            String pathExpr = path.getInfo().getPathExpr();
            String parentExpr = PathExprUtilities.getParentPathExpr(pathExpr);
            String lastLabel = PathExprUtilities.getLastLabel(pathExpr);
            MapEntry newEntry = new MapEntry(path);
            MapEntry parentEntry = parentExpr.equals("") ? rootEntry : pathExprMap.get(parentExpr);
            parentEntry.mapToChildren.put(lastLabel, newEntry);
            pathExprMap.put(pathExpr, newEntry);
        }
    }
    
    @Override
    public void startDocument() {}

    @Override
    public void endDocument() {}
    
    @Override
    public void start(String str, boolean isAttribute, boolean isValue) {
        if (!isValue) {
            if (isAttribute) str = '@' + str;
            MapEntry parentEntry = entryStack.isEmpty() ? rootEntry : entryStack.peek();
            MapEntry childEntry = parentEntry.mapToChildren.get(str);
            entryStack.push(childEntry);
        }
        else this.processingValue = true;
    }

    @Override
    public void end(boolean isAttribute, boolean isValue) {
        if (!this.processingValue)
            entryStack.pop();
        else
            this.processingValue = false;
    }
    
    public PathId getPathId() {
        return this.entryStack.peek().path.getPathId();
    }
    
    
    private class MapEntry {
        private Path path;
        private Map<String, MapEntry> mapToChildren;
        
        public MapEntry(Path path) {
            this.path = path;
            this.mapToChildren = new HashMap<>();
        }
    }
}
