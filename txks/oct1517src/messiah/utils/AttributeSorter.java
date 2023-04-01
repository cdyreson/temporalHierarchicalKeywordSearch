package messiah.utils;

import java.util.PriorityQueue;

/**
 * A generic class to sort a list of objects based on their given score. The score
 * can be equal. Ties are broken randomly.
 * @param T Object type
 * @param K Comparable attribute type
 * @author truongbaquan
 */
public class AttributeSorter<T,K extends Comparable> {
    
    private PriorityQueue<Elem> pq;
    
    //<editor-fold defaultstate="collapsed" desc="Outputs">
    private Object[] resObjs;
    private Comparable[] resScores;
    
    public int getResultSize() {
        return resObjs.length;
    }
    
    public T getObj(int order) {
        return (T)resObjs[order];
    }
    
    public K getScore(int order) {
        return (K)resScores[order];
    }
    //</editor-fold>
    
    public void add(T obj, K score) {
        if (this.pq == null)
            this.pq = new PriorityQueue<Elem>();
        this.pq.add(new Elem(obj, score));
    }
    
    public void clear() {
        this.pq.clear();
    }
    
    /**
     * Gets all results. After calling this method, this object is clear.
     * @return 
     */
    public int getResults() {
        this.resObjs = new Object[pq.size()];
        this.resScores = new Comparable[pq.size()];
        int i = 0;
        while (!pq.isEmpty()) {
            Elem elem = pq.poll();
            resObjs[i] = elem.obj;
            resScores[i] = elem.score;
            i++;
        }
        return resObjs.length;
    }
    
    private class Elem implements Comparable<Elem> {
        T obj;
        K score;

        public Elem(T obj, K score) {
            this.obj = obj;
            this.score = score;
        }

        @Override
        public int compareTo(Elem o) {
            return this.score.compareTo(o.score);
        }
    }
}
