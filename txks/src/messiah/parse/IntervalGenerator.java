package messiah.parse;

import usu.temporal.Time;
import usu.temporal.TimeItem;

/**
 *
 * @author Curtis Dyreson
 */
public interface IntervalGenerator {
    
    public TimeItem generate(TimeItem parent);
}
