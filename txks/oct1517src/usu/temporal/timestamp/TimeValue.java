package usu.temporal.timestamp;

import java.io.*;

/**
* <code>TimeValue</code> class implements the arithmetic and 
* comparison operations for an underlying domain of times 
* times that are bounded by "Min" and "Max" TimeValueTypes.
* Min (Max) are before (after) the smallest (largest) times.  
* Operations on TimeValues usually construct a new TimeValue, they
* never <em>mutate</em> a TimeValue.
*
* For example, assuming a discrete domain of times implemented
* using longs.
* <pre>
*    TimeValue min = TimeValue.BEGINNING_OF_TIME;
*    TimeValue max = TimeValue.END_OF_TIME;
*    TimeValue one = new TimeValue(1);
*    TimeValue three = new TimeValue(3);
*    TimeValue result = max.add(min);
*    System.out.println(max.image());  // Prints [TimeValue MAX]
*    System.out.println(min.image()); // Prints [TimeValue MIN]
*    System.out.println(result.image()); // prints [TimeValue NORMAL 0]
*    result = one.add(three); 
*    System.out.println(one.image()); // Prints [TimeValue NORMAL 1]
*    System.out.println(three.image()); // Prints [TimeValue NORMAL 3]
*    System.out.println(result.image()); //Prints [TimeValue NORMAL 4]
* </pre>
*
* @author  Curtis Dyreson
* @version 1.0, Mar/3/2003
* @see     usu.temporal.timestamp.Granule
* @status  IMPLEMENTED and TESTED
**/
public class TimeValue implements Comparable<TimeValue>, Serializable {

  // The three types of TimeValues: Max, Min, and Normal.
  public enum TimeValueType{Max, Min, Normal};
 
  // Largest possible time value 
  public static final TimeValue END_OF_TIME = new TimeValue(TimeValueType.Max, 0);
  public static final TimeValue ALL_OF_TIME = new TimeValue(TimeValueType.Max, 0);

  // Smallest possible time value 
  public static final TimeValue BEGINNING_OF_TIME = new TimeValue(TimeValueType.Min, 0);
  public static final TimeValue NEGATIVE_ALL_OF_TIME = new TimeValue(TimeValueType.Min, 0);

  public static final TimeValue ZERO_TIMEVALUE = new TimeValue(TimeValueType.Normal, 0);
  public static final TimeValue ONE_TIMEVALUE = new TimeValue(TimeValueType.Normal, 1);
  public static final TimeValue MINUS_ONE_TIMEVALUE = new TimeValue(TimeValueType.Normal, -1);

  // Kind of the TimeValue ranges from MIN to NORMAL
  private TimeValueType kind;

  // Actual value of the TimeValue
  private long value;

  /**
  * Construct a Normal, Max or Min time value.
  * The long represents the displacement from the origin, it is ignored
  * for Max or Min TimeValues.
  * @param kind - Normal, Max, or Min TimeValueType
  * @param count - granule count
  **/ 
  public TimeValue(TimeValueType kind, long count) {
    this.kind = kind;
    this.value = count;
    }

  /**
  * Construct a Normal, Max or Min TimeValue.
  * The long represents the displacement from the origin, it is ignored
  * for Max or Min TimeValues.
  * @param kind - Normal, Max, or Min TimeValueType
  * @param count - granule count
  **/ 
  public TimeValue(TimeValueType kind, int count) {
    this.kind = kind;
    this.value = count;
    }

  /**
  * Construct a Normal TimeValue.
  * @param count - granule count
  **/ 
  public TimeValue(int count) {
    this.kind = TimeValueType.Normal;
    this.value = count;
    }

  /**
  * Creates a Normal TimeValue.
  * @param granules - count in granules of this TimeValue
  **/ 
  public TimeValue(long granules) {
    this.kind = TimeValueType.Normal;
    this.value = granules;
    }

  /**
  * Create a nice string image of a time value
  * @return String image of time value
  **/
  public String image() {
    switch (kind) {
      case Max: return "[TimeValue MAX]";
      case Min: return "[TimeValue MIN]";
      case Normal: 
      default: 
        return "[TimeValue NORMAL " + Long.toString(value) + "]";
      }
    }

  /**
  * Accessor function to get the kind represented by this TimeValue.
  * @return the kind
  **/ 
  public TimeValueType getKind() {
    return kind;
    }

  /**
  * Accessor function to get the value represented by this TimeValue.
  * @return the value
  **/ 
  public long getValue() {
    return value;
    }

  /**
  * Compare this TimeValue with another.   Implements the Comparable interface.
  *   if this < other then returns negative number
  *   if this == other then returns 0
  *   if this > other then returns positive number
  * @param other - The TimeValue to compare
  * @return integer representing result of comparison
  * @see    java.lang.Comparable
  * @status NOT TESTED
  **/ 
  public int compareTo(TimeValue other) {
    switch (kind) {
      case Max: switch (other.getKind()) {
        case Max: return 0;
        case Min: return 1;
        case Normal: default: return 1;
        } 
      case Min: switch (other.getKind()) {
        case Max: return -1;
        case Min: return 0;
        case Normal: default: return -1;
        } 
      case Normal: default: switch (other.getKind()) {
        case Max: return -1;
        case Min: return 1;
        case Normal: default:  
          if (value == other.getValue()) return 0;
          if (value < other.getValue()) return -1;
          return 1;
        } 
      }
    }

  /**
  * Construct the result of "this minus other".
  * @param other - The TimeValue to subtract
  * @return this - other
  **/ 
  public TimeValue subtract(TimeValue other) {
    switch (kind) {
      case Max: switch (other.getKind()) {
        case Max: return ZERO_TIMEVALUE;
        case Min: return ALL_OF_TIME;
        case Normal: default: return ALL_OF_TIME;
        } 
      case Min: switch (other.getKind()) {
        case Max: return NEGATIVE_ALL_OF_TIME;
        case Min: return ZERO_TIMEVALUE;
        case Normal: default: return NEGATIVE_ALL_OF_TIME;
        } 
      case Normal: default: switch (other.getKind()) {
        case Max: return NEGATIVE_ALL_OF_TIME;
        case Min: return ALL_OF_TIME;
        case Normal: default:  
          return new TimeValue(TimeValueType.Normal, value - other.getValue());
        } 
      }
    }

  /**
  * Construct the result of "this plus other".
  * @param other - The TimeValue to add
  * @return this + other
  **/ 
  public TimeValue add(TimeValue other) {
    switch (kind) {
      case Max: switch (other.getKind()) {
        case Max: return ALL_OF_TIME;
        case Min: return ZERO_TIMEVALUE;
        case Normal: default: return ALL_OF_TIME;
        } 
      case Min: switch (other.getKind()) {
        case Max: return ZERO_TIMEVALUE;
        case Min: return NEGATIVE_ALL_OF_TIME;
        case Normal: default: return NEGATIVE_ALL_OF_TIME;
        } 
      case Normal: default: switch (other.getKind()) {
        case Max: return ALL_OF_TIME;
        case Min: return NEGATIVE_ALL_OF_TIME;
        case Normal: default: 
          return new TimeValue(TimeValueType.Normal, value + other.getValue());
        } 
      }
    }

  /**
  * Construct the result of "minus this".
  * @return this * -1
  **/ 
  public TimeValue negate() {
    switch (kind) {
      case Max: return NEGATIVE_ALL_OF_TIME;
      case Min: return ALL_OF_TIME;
      case Normal: default: return new TimeValue(TimeValueType.Normal, value * -1);
      }
    }

  /**
  * Construct this TimeValue + 1
  * @return this + 1
  **/ 
  public TimeValue increment() {
    switch (kind) {
      case Max: return this; // do nothing
      case Min: return this; // do nothing
      case Normal: default: return new TimeValue(TimeValueType.Normal, this.getValue() + 1);
      }
    }

  /**
  * Construct this TimeValue - 1
  * @return this - 1
  **/ 
  public TimeValue decrement() {
    switch (kind) {
      case Max: return this; // do nothing
      case Min: return this; // do nothing
      case Normal: default: return new TimeValue(TimeValueType.Normal, this.getValue() - 1);
      }
    }

  /**
  * Construct the result of "this times N".
  * @param other - The integer to multiply
  * @return this * N
  **/ 
  public TimeValue multiply(int n) {
    switch (kind) {
      case Max: return ALL_OF_TIME;
      case Min: return NEGATIVE_ALL_OF_TIME;
      case Normal: default: return new TimeValue(TimeValueType.Normal, value * n);
      }
    }

  /**
  * Construct the result of "this div N".
  * @param other - The integer to divide by
  * @return this/N
  **/ 
  public TimeValue divide(int n) {
    switch (kind) {
      case Min: return TimeValue.BEGINNING_OF_TIME;
      case Max: return TimeValue.END_OF_TIME;
      case Normal: default: return new TimeValue(TimeValueType.Normal, value / n);
      }
    }

  /**
  * A simple test for the class.
  **/ 
  public static void main(String argv[]) {
    TimeValue min = TimeValue.BEGINNING_OF_TIME;
    TimeValue max = TimeValue.END_OF_TIME;
    TimeValue one = new TimeValue(TimeValueType.Normal, 1);
    TimeValue three = new TimeValue(TimeValueType.Normal, 3);
    TimeValue result = null;

    System.out.println("max is " + max.image());  // Prints [TimeValue MAX]
    System.out.println("min is " + min.image()); // Prints [TimeValue MIN]
    System.out.println("one is " + one.image()); // Prints [TimeValue NORMAL 1]
    System.out.println("three is " + three.image()); // Prints [TimeValue NORMAL 3]

    // Addition tests
    System.out.println("Testing add --------------------------------"); 

    System.out.print("1 + 3 = ");
    result = one.add(three); System.out.println(result.image());

    System.out.print("3 + 1 = ");
    result = three.add(one); System.out.println(result.image());

    System.out.print("3 + 3 = ");
    result = three.add(three); System.out.println(result.image());

    System.out.println("Min as an interval means -max, i.e., negative all of time.");
    System.out.print("3 + min => 3 + (-max) => 3 - max = ");
    result = three.add(min); System.out.println(result.image());

    System.out.print("three + max = ");
    result = three.add(max); System.out.println(result.image());

    System.out.print("min + min => min + (-max) => min - max = ");
    result = min.add(min); System.out.println(result.image());

    System.out.print("min + max = ");
    result = min.add(max); System.out.println(result.image());

    System.out.print("max + min => max + (-max) => max - max =  ");
    result = max.add(min); System.out.println(result.image());

    System.out.print("max + max = ");
    result = max.add(max); System.out.println(result.image());

    // Subtraction tests
    System.out.println("Testing subtract --------------------------------"); 

    System.out.print("1 - 3 = ");
    result = one.subtract(three); System.out.println(result.image());

    System.out.print("3 - 1 = ");
    result = three.subtract(one); System.out.println(result.image());

    System.out.print("3 - 3 = ");
    result = three.subtract(three); System.out.println(result.image());

    System.out.print("3 - min => 3 - (-max) => 3 + max = ");
    result = three.subtract(min); System.out.println(result.image());

    System.out.print("3 - max = ");
    result = three.subtract(max); System.out.println(result.image());

    System.out.print("min - three = ");
    result = min.subtract(three); System.out.println(result.image());

    System.out.print("min - min => min - (-max) => min + max = ");
    result = min.subtract(min); System.out.println(result.image());

    System.out.print("min - max = ");
    result = min.subtract(max); System.out.println(result.image());

    System.out.print("max - 3 = ");
    result = max.subtract(three); System.out.println(result.image());

    System.out.print("max - min => max - (-max) => max + max = ");
    result = max.subtract(min); System.out.println(result.image());

    System.out.print("max - max = ");
    result = max.subtract(max); System.out.println(result.image());

    // Multiply tests
    System.out.println("Testing * --------------------------------"); 

    System.out.print("1 * 3 = ");
    result = one.multiply(3); System.out.println(result.image());

    System.out.print("3 * 1 = ");
    result = three.multiply(1); System.out.println(result.image());

    System.out.print("3 * 3 = ");
    result = three.multiply(3); System.out.println(result.image());

    System.out.print("min * 3 = ");
    result = min.multiply(3); System.out.println(result.image());

    System.out.print("max * 3 = ");
    result = max.multiply(3); System.out.println(result.image());

    // Divide tests
    System.out.println("Testing * --------------------------------"); 

    System.out.print("1 / 3 = ");
    result = one.divide(3); System.out.println(result.image());

    System.out.print("3 / 1 = ");
    result = three.divide(1); System.out.println(result.image());

    System.out.print("3 / 3 = ");
    result = three.divide(3); System.out.println(result.image());

    System.out.print("min / 3 = ");
    result = min.divide(3); System.out.println(result.image());

    System.out.print("max / 3 = ");
    result = max.divide(3); System.out.println(result.image());

    // Less than tests
    System.out.println("Testing < --------------------------------"); 

    System.out.print("Is 1 < max? "); 
    if (one.compareTo(three) == -1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 1 < 3? "); 
    if (one.compareTo(three) == -1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 < 3? "); 
    if (three.compareTo(three) == -1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max < 3? "); 
    if (max.compareTo(three) == -1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min < 3? "); 
    if (min.compareTo(three) == -1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 < min? "); 
    if (three.compareTo(min) == -1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 < max? "); 
    if (three.compareTo(max) == -1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min < min? "); 
    if (min.compareTo(min) == -1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max < min? "); 
    if (max.compareTo(min) == -1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min < max? "); 
    if (min.compareTo(max) == -1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max < max? "); 
    if (max.compareTo(max) == -1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.println("Testing == --------------------------------"); 

    System.out.print("Is 1 == max? "); 
    if (one.compareTo(three) == 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 1 == 3? "); 
    if (one.compareTo(three) == 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 == 3? "); 
    if (three.compareTo(three) == 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max == 3? "); 
    if (max.compareTo(three) == 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min == 3? "); 
    if (min.compareTo(three) == 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 == min? "); 
    if (three.compareTo(min) == 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 == max? "); 
    if (three.compareTo(max) == 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min == min? "); 
    if (min.compareTo(min) == 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max == min? "); 
    if (max.compareTo(min) == 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min == max? "); 
    if (min.compareTo(max) == 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max == max? "); 
    if (max.compareTo(max) == 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.println("Testing > --------------------------------"); 

    System.out.print("Is 1 > max? "); 
    if (one.compareTo(three) == 1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 1 > 3? "); 
    if (one.compareTo(three) == 1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 > 3? "); 
    if (three.compareTo(three) == 1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max > 3? "); 
    if (max.compareTo(three) == 1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min > 3? "); 
    if (min.compareTo(three) == 1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 > min? "); 
    if (three.compareTo(min) == 1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 > max? "); 
    if (three.compareTo(max) == 1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min > min? "); 
    if (min.compareTo(min) == 1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max > min? "); 
    if (max.compareTo(min) == 1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min > max? "); 
    if (min.compareTo(max) == 1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max > max? "); 
    if (max.compareTo(max) == 1) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.println("Testing negate --------------------------------");

    System.out.print("negate 3 = ");
    result = three.negate(); System.out.println(result.image());

    System.out.print("negate min = ");
    result = min.negate(); System.out.println(result.image());

    System.out.print("negate max = ");
    result = max.negate(); System.out.println(result.image());

    System.out.println("Testing increment --------------------------------");

    System.out.print("3++");
    result = three.increment(); System.out.println(result.image());

    System.out.print("min++");
    result = min.increment(); System.out.println(result.image());

    System.out.print("max++");
    result = max.increment(); System.out.println(result.image());

    System.out.println("Testing decrement --------------------------------");

    System.out.print("1--");
    result = one.decrement(); System.out.println(result.image());

    System.out.print("min--");
    result = min.decrement(); System.out.println(result.image());

    System.out.print("max--");
    result = max.decrement(); System.out.println(result.image());
    }

} //End of class
