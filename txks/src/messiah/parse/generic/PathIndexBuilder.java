package messiah.parse.generic;

import java.util.Map;
import java.util.Stack;
import messiah.PathInfo;
import messiah.database.Database;
import messiah.parse.*;
import usu.PathId;
import usu.PathIdFactory;

/**
 * This object is in charge of building the path index using the current Path
 * stack
 *
 * @author Curtis
 */
public class PathIndexBuilder implements ParserListener {

    private final Database db;
    private final Stack<String> labelStack;
    private final Stack<String> completeStack;

    public PathIndexBuilder(Database db, PathIdFactory factory) {
        this.db = db;
        labelStack = new Stack();
        completeStack = new Stack();
        labelStack.push("#");
        completeStack.push("#");
        db.pathNameIndex.put("#", factory.rootId());
        db.pathIndex.put(factory.rootId(), new PathInfo("#", 0, false, 0, 0));
    }

    @Override
    public void startDocument() {
        //System.out.println("Start Document");
    }

    @Override
    public void endDocument() {
    }

    @Override
    public void start(String str, boolean isAttribute, boolean isValue) {
        // Text nodes get their own label
        if (isValue) {
            str = "#";
        } else {

            // Ignore case in names of things
            str = str.toLowerCase();

            // Prepend a @ for an attribute, otherwise treat as a new path
            if (isAttribute) {
                str = '@' + str;

            }
        }

        // Build off of the parent if needed
        String parent = completeStack.peek();
        labelStack.push(str);
        String current = parent + "." + str;
        completeStack.push(current);
        if (!db.pathNameIndex.containsKey(current)) {
            PathId pathId = db.pathNameIndex.get(parent);
            //System.out.println("parent is " + parent + " " + pathId);
            pathId = pathId.newChild();
            while (db.pathIndex.containsKey(pathId)) {
                pathId = pathId.nextSibling();
            }
            //db.pathIndex.put(pathId, null);
            //System.out.println("child is " + current + " " +  pathId);
            db.pathIndex.put(pathId, new PathInfo(current, 0, false, 0, 0));
            db.pathNameIndex.put(current, pathId);
        }

    }

    public PathId getCurrentPathId() {
        String s = completeStack.peek();
        return db.pathNameIndex.get(s);
    }

    @Override
    public void end(boolean isAttribute, boolean isValue) {
        /*
         if (isValue) {
         return;
         }
         */
        labelStack.pop();
        completeStack.pop();
    }

    public static void main(String[] args) {
        Database bdb = new messiah.database.berkeleydb.Database(true, false);
        Map<String, PathId> map = bdb.pathNameIndex;
        Map<PathId, PathInfo> map2 = bdb.pathIndex;
        System.out.println("Doing pathNameIndex..........");
        for (String name : map.keySet()) {
            PathId id = map.get(name);
            System.out.println("key = " + name + " --> " + id);
        }
        System.out.println("Doing pathIndex..........");
        for (PathId id : map2.keySet()) {
            PathInfo value = map2.get(id);
            System.out.println("key = " + id + " --> " + value);
        }

    }
}
