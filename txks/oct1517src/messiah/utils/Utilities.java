package messiah.utils;

import java.util.Collection;

/**
 * A class consisting of supporting static methods
 * @author truongbaquan
 */
public class Utilities {
    public static long computeTheNextPowerOf2(long input) {
        long n = input - 1;
        n = n | (n >> 1);
        n = n | (n >> 2);
        n = n | (n >> 4);
        n = n | (n >> 8);
        n = n | (n >> 16);
        n = n | (n >> 32);
        return n + 1;
    }

    public static int computeTheNextPowerOf2(int input) {
        int n = input - 1;
        n = n | (n >> 1);
        n = n | (n >> 2);
        n = n | (n >> 4);
        n = n | (n >> 8);
        n = n | (n >> 16);
        return n + 1;
    }

    public static long powerOf2(int exponent) {
        if (exponent < 0) throw new IllegalArgumentException("Exponent cannot be negative");
        return 1L << exponent;
    }

    public static Object getFirstElem(Collection list) {
        for (Object e : list) {
            return e;
        }
        return null;
    }
}
