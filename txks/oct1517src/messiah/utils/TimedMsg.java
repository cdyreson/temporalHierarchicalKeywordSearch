package messiah.utils;

import java.sql.Time;
import java.text.DateFormat;

/**
 * An utility to print a message along with the current time for session-wide logging.
 * An excellent idea -- John
 */
public class TimedMsg {

    /**
     * Gets the string described the current time in short date format in square brackets
     * @return      The description string for the current time
     */
    public static String getTimeStr() {
        Time t = new Time(System.currentTimeMillis());
        return "[" + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.FULL).format(t) + "] ";
    }

    /**
     * Prints a message (along with the time it is displayed)
     * @param msg   The message
     */
    public static void printMsg(String msg) {
        System.out.println(getTimeStr() + " " + msg);
    }
}
