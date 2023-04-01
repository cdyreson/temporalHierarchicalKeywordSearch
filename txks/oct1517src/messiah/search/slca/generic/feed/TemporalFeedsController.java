/*
 * This is the critical class. What we want to do is create one list that we
 * can traverse in sorted order from the merging of iterators of several
 * lists.
 */
package messiah.search.slca.generic.feed;

//import java.lang.reflect.Array;
import java.util.*;
import messiah.database.Database;
import usu.NodeId;
import usu.PathId;
import usu.algebra.KeywordSearchExpression;
import usu.dln.HistoryDLN;
//import usu.dln.TimeElementHistoryDLN;
import usu.temporal.Time;
import usu.temporal.TimeElement;

/**
 * The Feeds Controller generates sets of potential SLCAs. It merges the feeds
 * for the FeedGroups that go into producing an SLCA
 *
 * @author Curt
 */
public class TemporalFeedsController implements Iterator<NodeId> {

    private final boolean verbose = true;

    //TimeElement lifetime = new TimeElement();
    private PriorityQueue<CachedNodeTriple> whoIsNext;
    private TemporalFeedGroup[] feeds;
    //Map<Integer, Integer> lcaLevelMap;
    private boolean empty;
    private Integer count;
    private int key;
    private int previousShifted[];
    private int shift[];
    private Map<Integer, Integer> lcaLevelMap;
    private Map<Integer, NodeId> currentLCAMap;
    private NodeId[] currentNodeIds;
    private int maxLevel = 0;
    private Database db;
    private boolean inEndGame = false;

    public TemporalFeedsController(Database db) {
        this.db = db;
    }

    public int initialize(KeywordSearchExpression exp) {
        String[] query = exp.getKeywordsAsArray();
        short dimensions = (short) query.length;
        whoIsNext = new PriorityQueue();
        empty = false;
        count = 0;
        key = 0;
        feeds = new TemporalFeedGroup[dimensions];
        this.lcaLevelMap = new HashMap();
        this.currentLCAMap = new HashMap();
        previousShifted = new int[dimensions];
        currentNodeIds = new NodeId[dimensions];
        inEndGame = false;

        Set<PathId>[] paths = new HashSet[dimensions];

        // Look up node lists for each path
        for (short i = 0; i < dimensions; i++) {
            paths[i] = db.keywordPathsIndex.get(query[i]);
            // If no list for the path, return empty result
            if (paths[i] == null) {
                return 0;
            }
        }

        // Allocate space to hold the shift numbers
        shift = new int[dimensions];
        for (int i = 0; i < dimensions; i++) {
            int j = 2;
            int k = 1;
            while (j < paths[i].size()) {
                j = j << 1;
                k++;
            }
            // K is power of 2 above number of paths
            shift[i] = (i == 0) ? k : k + shift[i - 1];
        }

        // Adjust shift to be total shift for level below
        for (int i = dimensions - 1; i > 0; i--) {
            shift[i] = shift[i - 1];
        }
        shift[0] = 0;
        //SplitMapKeywordNodeIndex index = (SplitMapKeywordNodeIndex)db.keywordNodesIndex;
        for (short i = 0; i < dimensions; i++) {
            // Create the iterators
            TemporalFeedGroup group = addFeedGroup();
            short count = 0;

            for (PathId pathId : paths[i]) {
                Iterator<NodeId> nIter = (db.keywordNodesIndex.get(query[i]).get(pathId)).iterator();
                group.addFeed(i, count++, nIter, pathId, query[i]);
            }
        }

        short temp = 0;
        createLCALevels(temp, dimensions, null, 0, paths);
        wrapCreateTypeSLCAs(dimensions, paths);
        return maxLevel;
    }

        Map<PathId, Set<Integer>> typeSLCAMap;
    int typeSLCACount = 0;

    private void wrapCreateTypeSLCAs(short dimensions, Set<PathId>[] paths) {
        System.out.println("Wrap ");
        typeSLCAMap = new HashMap();
        createTypeSLCAs((short) 0, dimensions, null, 0, paths);

        // Clean up the type SLCA map by removing SLCAs that are ancestors of others
        Map<PathId, Set<Integer>> tempMap;
        tempMap = new HashMap();
        for (PathId pathId : typeSLCAMap.keySet()) {
            tempMap.put(pathId, typeSLCAMap.get(pathId));
                        //System.out.println("Put " + pathId + " " + pathId.getLevel());
        }
        for (PathId pathId : typeSLCAMap.keySet()) {
            PathId tempPathId = pathId;
            //System.out.println("Level " + tempPathId + " " + tempPathId.getLevel());
            while (tempPathId.getLevel() > 1) {
                PathId parentPathId = tempPathId.getParentId();
                if (tempMap.containsKey(parentPathId)) {
                    //System.out.println("Removing " + parentPathId);
                    tempMap.remove(parentPathId);
                }
                tempPathId = parentPathId;
                            //System.out.println("Leveli " + tempPathId + " " + tempPathId.getLevel());
            }
            //tempMap.put(pathId, typeSLCAMap.get(pathId));
        }
        for (PathId pathId : tempMap.keySet()) {
            System.out.println("Controller SLCA " + pathId + " " + tempMap.get(pathId));
        }
    }
    
        /*
     A Type SLCA is an SLCA among the types.
     */
    private void createTypeSLCAs(short current, short dimensions, PathId pathLCA, int hashKey, Set<PathId>[] paths) {
        System.out.println("CreateTypeSLCAs: " + current + " " + dimensions);
        // Reached the end
        if (current >= dimensions) {
            System.out.println("level is " + pathLCA.getLevel());
            int level = pathLCA.getLevel() - 1;
            if (typeSLCAMap.containsKey(pathLCA)) {
                Set<Integer> set = typeSLCAMap.get(pathLCA);
                set.add(typeSLCACount++);
                typeSLCAMap.put(pathLCA, set);
            } else {
                Set<Integer> set = new HashSet();
                set.add(typeSLCACount++);
                typeSLCAMap.put(pathLCA, set);
            }
            maxLevel = (level > maxLevel) ? level : maxLevel;
            return;
        }

        // Process this path
        int i = 0;
        for (PathId pathId : paths[current]) {
            int level = (pathLCA == null) ? pathId.getLevel() : pathId.computeNCALevel(pathLCA);
            PathId newPathLCA = pathId.getAncestor(level);
            int newHashKey = hashKey + (i++ << shift[current]);
            short nextCurrent = current;
            createTypeSLCAs(++nextCurrent, dimensions, newPathLCA, newHashKey, paths);
        }
    }
    
    private void createLCALevels(short current, short dimensions, PathId pathLCA, int hashKey, Set<PathId>[] paths) {
        // Reached the end
        if (current >= dimensions) {
            //System.out.println("level is " + pathLCA.getLevel());
            int level = pathLCA.getLevel() - 1;
            lcaLevelMap.put(hashKey, level);
            maxLevel = (level > maxLevel) ? level : maxLevel;
            return;
        }

        // Process this path
        int count = 0;
        for (PathId pathId : paths[current]) {
            int level = (pathLCA == null) ? pathId.getLevel() : pathId.computeNCALevel(pathLCA);
            PathId newPathLCA = pathId.getAncestor(level);
            int newHashKey = hashKey + (count++ << shift[current]);
            short nextCurrent = current;
            createLCALevels(++nextCurrent, dimensions, newPathLCA, newHashKey, paths);
        }
    }

    public NodeId[] getCurrentNodeIds() {
        return currentNodeIds;
    }

    public TemporalFeedGroup addFeedGroup() {
        TemporalFeedGroup feedGroup = new TemporalFeedGroup();

        // Set the level of the current feed group as nothing
        //lcaLevelMap.put(count, 0);
        // Add the feed to the current feed set
        if (verbose) {
            System.out.println("TemporalFeedsController addFeedGroup count is " + count + " " + feedGroup);
        }

        feeds[count] = feedGroup;
        count++;
        return feedGroup;
    }

    private NodeId reachedEndGame() {
        return null;
    }
    
    public NodeId getCandidateSLCA() {
        //System.out.println("key is " + key + " " + shift[0] + " " + shift[1] + " " + shift[2]);
        //System.out.println("key is " + key);
        int level = lcaLevelMap.get(key);

        // Check to see if we have a candidateLCA
        if (!currentLCAMap.containsKey(key)) {
            // Cycle through current list to figure out lca for this key
            NodeId tempLCA = null;
            for (CachedNodeTriple trip : whoIsNext) {
                NodeId nodeId = trip.nodeId;
                currentNodeIds[trip.dimension] = nodeId;
                NodeId lca2 = nodeId.getAncestor(level);
                //System.out.println(" nodeId " + nodeId + " lca2 ");
                if (tempLCA == null) {
                    tempLCA = lca2;
                } else {
                    //int lcaLevel = lca.computeNCALevel(lca2);
                    //lca = lca.getAncestor(lcaLevel);
                    // Make sure this LCA matches the current one
                    if (!tempLCA.equals(lca2)) {
                        // We don't have a match
                        System.out.println(" no match");
                        // Check to see if there are any more
                        if (hasNext()) {
                            // have more, advance and try again
                            next();
                            return getCandidateSLCA();
                        } else {
                            inEndGame = true;
                            return reachedEndGame();
                        }                     
                    }
                }
            }

        }
        NodeId lca = null;
        //System.out.println("Consider level " + level);
        for (CachedNodeTriple trip : whoIsNext) {
            NodeId nodeId = trip.nodeId;
            currentNodeIds[trip.dimension] = nodeId;
            NodeId lca2 = nodeId.getAncestor(level);
            //System.out.println(" nodeId " + nodeId + " lca2 ");
            if (lca == null) {
                lca = lca2;
            } else {
                int lcaLevel = lca.computeNCALevel(lca2);
                lca = lca.getAncestor(lcaLevel);
                //if (!lca.equals(lca2)) {
                //System.out.println(" no match");
                //    return null;
                //}
            }
        }
        if (verbose) {
            System.out.println("TemporalFeedsController: Priority queue is ");
            for (NodeId n : currentNodeIds) {
                System.out.print(n + " ");
            }
            System.out.println("");
        }
        return lca;
    }

    public NodeId getCandidateSLCAOld() {
        //System.out.println("key is " + key + " " + shift[0] + " " + shift[1] + " " + shift[2]);
        //System.out.println("key is " + key);
        int level = lcaLevelMap.get(key);
        NodeId lca = null;
        //System.out.println("Consider level " + level);
        for (CachedNodeTriple trip : whoIsNext) {
            NodeId nodeId = trip.nodeId;
            currentNodeIds[trip.dimension] = nodeId;
            NodeId lca2 = nodeId.getAncestor(level);
            //System.out.println(" nodeId " + nodeId + " lca2 ");
            if (lca == null) {
                lca = lca2;
            } else {
                int lcaLevel = lca.computeNCALevel(lca2);
                lca = lca.getAncestor(lcaLevel);
                //if (!lca.equals(lca2)) {
                //System.out.println(" no match");
                //    return null;
                //}
            }
        }
        if (verbose) {
            System.out.println("TemporalFeedsController: Priority queue is ");
            for (NodeId n : currentNodeIds) {
                System.out.print(n + " ");
            }
            System.out.println("");
        }
        return lca;
    }

    /* 
     * gather returns the list of times for the current CachedNodeTriple
     * at the front of the priority queue.  Use it to get the lifetime.
     */
    public Time[] gatherTimes() {
        Time[] time = new Time[whoIsNext.size()];
        int i = 0;
        for (CachedNodeTriple trip : whoIsNext) {
            HistoryDLN nodeId = (HistoryDLN) trip.nodeId;
            Time time2 = nodeId.getTime();
            time[i++] = time2;
        }
        return time;
    }

    /* 
     gather returns the intersection of the times for the current CachedNodeTriple
     at the front of the priority queue.  Use it to get the lifetime.
     */
    public TimeElement gather() {
        /*
         TimeElement lifetime = new TimeElement();
         for (Integer key : feedMap.keySet()) {
         lifetime.union(feedMap.get(key).gather());
         }
         */
        Time time = null;
        for (CachedNodeTriple trip : whoIsNext) {
            HistoryDLN nodeId = (HistoryDLN) trip.nodeId;
            Time time2 = nodeId.getTime();
            //System.out.print(" nodeId " + nodeId + " lca2 ");
            if (time == null) {
                time = time2;
            } else {
                time = time.intersection(time2);
                // If there is no intersection then exit with an empty element
                if (time == null) {
                    return new TimeElement();
                }
            }
        }
        return new TimeElement(time);
    }

    /* 
     fragments returns the fragments of the current CachedNodeTriple that do not
     overlap the given lifetime, and the fragment must be at the given level.
     */
    /*
     public List<NodeId> fragments(TimeElement lifetime, int level) {
     List<NodeId> result = new ArrayList();
     for (CachedNodeTriple trip : whoIsNext) {
     HistoryDLN nodeId = (HistoryDLN) trip.nodeId;
     TimeElement time2 = lifetime.difference(new TimeElement(nodeId.getTime()));
     if (!time2.isEmpty()) {
     for (Time t : time2.getTimes()) {
     result.add(new HistoryDLN(nodeId.getAncestor(level), t));
     }
     }
     }
     return result;
     }
     */
    public void startFeeds() {
        if (verbose) {
            System.out.println("TemporalFeedsController startFeeds count is " + count);
        }
        for (Integer i = 0; i < count; i++) {
            startFeed(i);
        }
    }

    private void startFeed(Integer i) {
        if (verbose) {
            System.out.println("TemporalFeedsController startFeed " + i);
        }
        TemporalFeedGroup group = feeds[i];

        if (!group.hasNext()) {
            // feed is empty, don't add
            //System.out.println("CachingFeedsController feed is empty " + i);
            empty = true;
            return;
        }
        CachedNodeTriple tuple = group.next();
        key = key + (tuple.id << shift[tuple.dimension]);
        previousShifted[tuple.dimension] = tuple.id << shift[tuple.dimension];

        if (verbose) {
            System.out.println("TemporalFeedsController found next " + tuple.nodeId);
        }
        if (tuple.nodeId != null) {
            whoIsNext.add(tuple);
            if (verbose) {
                System.out.println("TemporalFeedsController queue size " + whoIsNext.size());
            }
        }
    }

    /* 
     * Does this iterator have a next value?
     */
    @Override
    public boolean hasNext() {
        if (verbose) {
            System.out.println("TemporalFeedsController hasnext empty? " + empty + " queue size " + whoIsNext.size());
        }
        if (empty) {
            return false;
        }
        if (verbose) {
            System.out.println("TemporalFeedsController hasnext not empty");
        }
        return true;
    }

    @Override
    public NodeId next() {
        if (verbose) {
            System.out.println("TemporalFeedsController next");
        }
        if (empty) {
            // Should never get here, asking an empty iterator to produce
            System.err.println("Bad feed in cachingFeedsController");
            System.exit(-1);
            return null;
        }
        CachedNodeTriple tuple = whoIsNext.poll();
        //System.out.println("CachingFeedController next " + tuple.nodeId + " " + tuple.dimension + " " + tuple.id);

        int index = tuple.dimension;
        NodeId result = tuple.nodeId;
        TemporalFeedGroup group = feeds[index];
        if (group.hasNext()) {
            // Advance group
            CachedNodeTriple trip = group.next();
            key = key - (previousShifted[trip.dimension]);
            previousShifted[trip.dimension] = trip.id << shift[trip.dimension];
            key = key + previousShifted[trip.dimension];
            //System.out.println("CachingFeedController adding " + trip.nodeId + " " + trip.dimension + " " + trip.id);
            whoIsNext.add(trip);
        } else {
            // Group is done, controller is now empty
            empty = true;
        }
        if (verbose) {
            System.out.println("TemporalFeedsController after advance " + result);
        }
        return result;
    }

    @Override
    public void remove() {
    }

}
