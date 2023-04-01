package messiah.utils;

/**
 * Encapsulates a stopwatch to measure the performance of some operation.
 * It is not thread-safe.
 * @author truongbaquan
 */
public class Stopwatch {
    private long startTime;
    private long time = 0;
    private int count = 0;
    private boolean running = false;

    public boolean isRunning() {
        return running;
    }

    /**
     * Start/Restart the stopwatch
     */
    public void start() {
        if (!running) {
            running = true;
            startTime = System.nanoTime();
        }
    }

    /**
     * Stop the stopwatch
     */
    public void stop() {
        if (running) {
            running = false;
            long elapsedTime = System.nanoTime() - startTime;
            this.time += elapsedTime;
            startTime = Long.MIN_VALUE;
            count++;
        }
    }

    /**
     * Reset the time counter back to 0
     */
    public void reset() {
        if (!running) this.time = 0;
    }

    /**
     * Read the time elapsed.
     * @return The time elapsed if the stopwatch is stopped .-1 (error) if the stopwatch is running.
     */
    public double readTime() {
        if (!running) {
            return ((double)time) / 1000000;
        }
        else return 0;
    }
    
    public double readAverageTime() {
        if (!running) {
            return (((double)time) / 1000000) / count;
        }
        else return 0;
    }
}
