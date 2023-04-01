package messiah.utils;

/**
 * A non-thread-safe counter class
 * @author Truong Ba Quan
 */
public class Counter {
    private int count = 0;

    public int getValue() {
        return this.count;
    }

    public void increment() {
        this.count++;
    }

    public void decrememt() {
        this.count--;
    }
    
    public void clear() {
        this.count = 0;
    }
}
