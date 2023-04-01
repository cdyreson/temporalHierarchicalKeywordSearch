package messiah.parse.old;

import messiah.parse.ParserListener;
import messiah.utils.Utilities;

/**
 * This object is used to read the constants fanout and maxDepth from the document
 * @author John Truong Ba Quan
 */
public class ConstantsReader implements ParserListener {
    public static final int MAXIMUM_POSSIBLE_DEPTH = 64;
      
    private int maxDepth = 0;
    public int getMaxDepth() {
        return this.maxDepth;
    }
    
    private long[] fanouts = new long[MAXIMUM_POSSIBLE_DEPTH];    
    public long getFanout(int level) {
        return this.fanouts[level];
    }    
    public long[] getFanouts() {
        long[] res = new long[this.maxDepth+1];
        for (int i = 0; i <= this.maxDepth; i++)
            res[i] = getFanout(i);
        return res;
    }
    public long getLevelOrder(int level) {
        return Utilities.computeTheNextPowerOf2(this.getFanout(level)+1);
    }
    public long[] getLevelOrders() {
        long[] res = new long[this.maxDepth+1];
        for (int i = 0; i <= this.maxDepth; i++)
            res[i] = this.getLevelOrder(i);
        return res;
    }
    
    private int curLevel = 0;
    private long[] numChildren = new long[MAXIMUM_POSSIBLE_DEPTH];
    
    @Override
    public void startDocument() {}

    @Override
    public void endDocument() {}
    
    @Override
    public void start(String str, boolean isAttribute, boolean isValue) {
        this.curLevel++;
        if (this.maxDepth < this.curLevel) this.maxDepth = this.curLevel;
        numChildren[this.curLevel-1]++; // increase the number of children of its parent
        numChildren[this.curLevel] = 0; // reset number of children at this level
    }

    @Override
    public void end(boolean isAttribute, boolean isValue) {
        if (this.fanouts[this.curLevel] < this.numChildren[this.curLevel])
            this.fanouts[this.curLevel] = this.numChildren[this.curLevel];
        this.numChildren[this.curLevel] = 0;
        this.curLevel--;
    }

}
