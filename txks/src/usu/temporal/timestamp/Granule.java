package usu.temporal.timestamp;


import java.util.concurrent.LinkedBlockingQueue;


import java.io.*;

/**
* A <code>Granule</code> is a pairing of a TimeValue and a
* Granularity, independent of a <code>CalendricSystem</code>.  
* Granules can be used for both unanchored and anchored times.  
* If the time is anchored, the Granule represents the distance from the 
* granularity anchor point.  
* If the time is unanchored, the Granule just represents
* a distance.  Granules can be further classified as determinate, 
* indeterminate, or now-relative.  A determinate granule has a known, fixed distance.
* An indeterminate granule is some distance between a lower and upper 
* bound.  A probability mass function determines the probability of 
* each indeterminate alternative.  
*
* @author  Curtis Dyreson, Alex Henniges, Richard Snodgrass
* @version 2.0, Sep/2/2008
* @see     usu.temporal.timestamp.TimeValue
* @see     tauzaman.extendedboolean.ExtendedBoolean
**/
public class Granule implements Serializable, Comparable<Granule>{

	private static final long serialVersionUID = 1L;
// Kind of this granule
  protected GranuleType kind;
  // The three types of Granule: Determinate, Indeterminate, and NowRelative.
  /**
   * Enumeration type for Granule. A granule can be either Determinate, 
   * Indeterminate, or NowRelative. A determinate granule has a known, fixed distance.
   * An indeterminate granule is some distance between a lower and upper 
   * bound.  A probability mass function determines the probability of 
   * each indeterminate alternative. 
   */
  public enum GranuleType{Determinate, Indeterminate, NowRelative};

//  // Some useful ExtendedBoolean values 
//  protected final static ExtendedBoolean FALSE_EB = new ExtendedBoolean(false);
//  protected final static ExtendedBoolean TRUE_EB = new ExtendedBoolean(true);
//  protected final static ExtendedBoolean MAYBE_EB = new ExtendedBoolean();
  
  protected static int POSSIBLE = 0;
  protected static int DEFINITE = 100;


  // A determinate granule is represented as a TimeValue distance (in granules)
  // An indeterminate granule is represented by lower and upper bounds.
  protected TimeValue lower;
  protected TimeValue upper;


  /**
  * Create a determinate granule at the default Granularity
  * @param p - count of granules
  *
  * @throws TauZamanException if any abnormal condition occurs when 
  * setting this Granule's default Granularity
  **/ 
  public Granule(TimeValue p) /* throws TauZamanException */{
    this.lower = p;
    this.upper = p;
    this.kind = GranuleType.Determinate;
//    granularity = TauZamanSystem.getActiveService().getActiveCalendricSystemDefaultGranularity();
    }


  /**
  * Create a determinate granule at the default Granularity
  * @param p - count of granules
  *
  * @throws TauZamanException if any abnormal condition occurs when 
  * setting this Granule's default Granularity
  **/ 
  public Granule(long p) /* throws TauZamanException */{
    this.lower = new TimeValue(p);
    this.upper = new TimeValue(p);
    this.kind = GranuleType.Determinate;
    //granularity = TauZamanSystem.getActiveService().getActiveCalendricSystemDefaultGranularity();
    }


  /**
  * Create an indeterminate granule with an unknown PMF at the default Granularity
  * @param lower - count of granules
  * @param upper - count of granules
  *
  * @throws TauZamanException if any abnormal condition occurs when 
  * setting this Granule's default Granularity
  **/ 
  public Granule(TimeValue lower, TimeValue upper)/* throws TauZamanException */{
    this.lower = lower;
    this.upper = upper;
    this.kind = GranuleType.Indeterminate;
    //granularity = TauZamanSystem.getActiveService().getActiveCalendricSystemDefaultGranularity();
    }



  /**
  * Accessor - retrieve the kind of this granule as a <code>GranuleType</code>
  * @return the kind, either GranuleType.Determinate, GranuleType.Indeterminate
  **/
  public GranuleType getKind() {
    return kind;
    }

  /**
  * Accessor - retrieve the granules from a determinate granule (defaults
  * to lower bound for indeterminate granule)
  * @return TimeValue object that records the granules
  **/
  public TimeValue getGranule() {
    return lower;
    }

  /**
  * Accessor - retrieve the lower bound from an indeterminate granule (defaults
  * to granules for determinate granule)
  * @return TimeValue object that records the lower bound
  **/
  public TimeValue getLower() {
    return lower;
    }

  /**
  * Accessor - retrieve the upper bound from an indeterminate granule (defaults
  * to granules for determinate granule)
  * @return TimeValue object that records the lower bound
  **/
  public TimeValue getUpper() {
    switch (kind) {
      // The NOWRELATIVE case is handled by the NowRelativeGranule class
      case Indeterminate: return upper;
      case Determinate: default: return lower;
      }
    }

  /**
  * Create a nice string image of a granule, alternative to toString()
  * @return String image of a granule
  **/
  public String image() {
    switch (kind) {
      case Indeterminate: 
        return "[Indeterminate " + lower.image() + "~" + upper.image() + "]";
// The NOWRELATIVE case is handled by the NowRelativeGranule class
//      case NOWRELATIVE: 
//        return "[NowRelative " + lower.image() + "]";
      case Determinate: default:
        return "[Determinate " + lower.image() + "]";
      }
    }

  /**
  * Construct a Granule that has each bound decremented by 1
  * @return this - 1
  **/ 
  public Granule decrement() {
    switch (kind) {
      case Indeterminate: 
        return new Granule(lower.decrement(), upper.decrement());
// The NOWRELATIVE case is handled by the NowRelativeGranule class
//      case NOWRELATIVE: 
//        return new NowRelativeGranule(this.granularity, lower.decrement());
      case Determinate: default:
        return new Granule(lower.decrement());
      }
    }

  /**
  * Construct a Granule that has each bound incremented by 1
  * @return this + 1
  **/ 
  public Granule increment() {
    switch (kind) {
      case Indeterminate: 
        return new Granule(lower.increment(), upper.increment());
// The NOWRELATIVE case is handled by the NowRelativeGranule class
//      case NOWRELATIVE: 
//        return new NowRelativeGranule(this.granularity, lower.increment());
      case Determinate: default:
        return new Granule(lower.increment());
      }
    }

  /**
  * Negate the granule.  This method constructs a new granule that has both
  * both bounds negated.  Since the granule represents a distance, negation
  * is well-defined.  The PMF and granularity are unchanged.
  * @return a new Granule with the lower and upper bounds negated
  **/ 
  public Granule negate() {
    switch (kind) {
      case Indeterminate: 
        return new Granule(upper.negate(), lower.negate()); 
// The NOWRELATIVE case is handled by the NowRelativeGranule class
//      case NOWRELATIVE: 
//      return new NowRelativeGranule(lower.negate()); 
      case Determinate: default:
        return new Granule(lower.negate()); 
      }
    }

  /**
   * Compare this <code>Granule</code> to <code>other</code>.
   * 
   * @param other  The <code>Granule</code> to compare this one.
   * 			   Error is thrown if the object is not a The <code>Granule</code>.
   * @return 
   *   If this is less than other, <I>-1</I> is returned. If
   *   this is equal to other, <I>0</I> is returned. Else if 
   *   this is greater than other, <I>1</I> is returned.
   */
  public int compareTo(Granule other) {		
		switch(this.kind) {
		case Indeterminate: {
			switch(other.kind) {
			case NowRelative:
			case Determinate:
			default: {
				if(this.upper.compareTo(other.lower) < 0)
					return -1;
				else if(this.lower.compareTo(other.lower) == 0)
					return 0;
				else
					return 1;
			}
			}
		}
		case Determinate: 
		default: {
			switch(other.kind) {
			case Indeterminate: {
				if(this.upper.compareTo(other.lower) < 0)
					return -1;
				else if(this.lower.compareTo(other.lower) == 0)
					return 0;
				else
					return 1;
			}
			case NowRelative:
			case Determinate:
			default: {
				return this.lower.compareTo(other.lower);
			}
			}
		}
		}
	}
  
  
  /**
   * Is this == other?  This returns an ExtendedBoolean value to
   * capture the comparison for indeterminate granules.  For determinate
   * granules, two granules are equal if they represent the same granule.
   * For indeterminate granules, the probability of their being equal must
   * exceed the plausibility.
   * @param other - The Granule to compare
   * @return does this == other?
   * @status NOT IMPLEMENTED
   **/
   public boolean equalTo(Granule other, int plausibility) {
     switch (kind) {
       case Determinate: default: switch (other.getKind()) {
         case NowRelative:
         case Indeterminate:
         case Determinate: default:
           if (this.lower.compareTo(other.getLower()) == 0) return true;
           return false;
         }
       }
     }


	 /** 
	   * Determines if this Granule is less than <code>other</code> Granule.
	   * For determinate granules, it compares the values directly. For 
	   * indeterminate granules, it returns an ExtendedBoolean for if 
	   * <I>this < other</I> exceeds the given plausiblity.
	   * 
	   * The <code>plausibility</code> is an integer greater than or 
	   * equal to 0 and less than or equal to 100 giving the
	   * percentage of certainty that is desired. If the probability
	   * meets or exceeds the plausibility, it returns True. If the 
	   * probability that the other Granule is less than or equal to 
	   * this Granule exceeds <I> 100 - plausiblity </I>, it 
	   * returns False. If neither condition can be reached as a result of 
	   * approximation, it returns Maybe. The chance of returning Maybe is 
	   * <I> 2 / precision </I>.
	   * 
	   * @param other - The Granule to compare
	   * @param plausibility
	   * The plausibility that this Granule is less than <code>other</code>.
	   * @return <I>this < other</I>. True if it exceeds the plausibility or
	   * if it is certain. False if it cannot exceed the plausibility or if
	   * it is certain it is not. Else Maybe.
	   **/
	public boolean lessThan(Granule other, int plausibility) {
	     switch (kind) {
	       case Determinate: default: switch (other.getKind()) {
	         case NowRelative:
	         case Indeterminate:
	         case Determinate: default:
	           if (this.lower.compareTo(other.getLower()) == -1) return true;
	           return false;
	         }
	       }
	     }

	
  /**
   * Is this <= other?  This returns an ExtendedBoolean value to
   * capture the comparison for indeterminate granules.  For determinate
   * granules, a granule is <= if the count is no smaller.
   * For indeterminate granules, the probability of their being <= must
   * exceed the plausibility.
   * @param other - The Granule to compare
   * @return does this <= other?
   * @status NOT IMPLEMENTED
   **/
   public boolean lessThanOrEqualTo(Granule other, int plausibility) {
     switch (kind) {
       case Determinate: default: switch (other.getKind()) {
         case NowRelative:
         case Indeterminate:
         case Determinate: default:
           if (this.lower.compareTo(other.getLower()) <= 0) return true;
           return false;
         }
       }
     }

   
  /**
  * Construct a new Granule that is this + other.  The addition is
  * similar to that in interval mathematics, i.e., [a,b] + [c,d] = [a+c, b+d],
  * where a and c are lower bounds and b and d are upper bounds of the
  * respective granules.  The granules must be at the same granularity.
  * The addition discards the PMF, that is, the PMF of the new granule
  * is (usually) unknown since the needed PMF is the convolution of
  * the two PMFs which is difficult/expensive to construct on the fly.
  * @param other - The Granule to add
  * return this + other
  **/
  public Granule add(Granule other) {
    switch (kind) {
      case Indeterminate: switch (other.getKind()) {
        case Indeterminate:
          return new Granule(
                             lower.add(other.getLower()),
                             upper.add(other.getUpper())
                             );
        case NowRelative:
        case Determinate: default:
          return new Granule(
                             lower.add(other.getLower()),
                             upper.add(other.getLower())
                             );
        }
      case Determinate: default: switch (other.getKind()) {
        case Indeterminate:
          return new Granule(
                             lower.add(other.getLower()),
                             lower.add(other.getUpper())
                             );
        case NowRelative:
        case Determinate: default:
          return new Granule(
                             lower.add(other.getLower())
                            );
        }
      }
    }

  /**
  * Construct a new Granule that is this - other.  The addition is
  * similar to that in interval mathematics, i.e., [a,b] - [c,d] = [a-c, b-d],
  * where a and c are lower bounds and b and d are upper bounds of the
  * respective granules.  The granules must be at the same granularity.
  * The subtraction discards the PMF, that is, the PMF of the new granule
  * is (usually) unknown since the needed PMF is the convolution of
  * the two PMFs which is difficult/expensive to construct on the fly.
  * @param other - The Granule to subtract
  * return this - other
  **/
  public Granule subtract(Granule other) {
    switch (kind) {
      case Indeterminate: switch (other.getKind()) {
        case Indeterminate:
          return new Granule(
                             lower.subtract(other.getLower()),
                             upper.subtract(other.getUpper())
                             );
        case NowRelative:
        case Determinate: default:
          return new Granule(
                             lower.subtract(other.getLower()),
                             upper.subtract(other.getLower())
                             );
        }
      case Determinate: default: switch (other.getKind()) {
        case Indeterminate:
          return new Granule(
                             lower.subtract(other.getLower()),
                             lower.subtract(other.getUpper())
                             );
        case NowRelative:
        case Determinate: default:
          return new Granule(
                             lower.subtract(other.getLower())
                            );
        }
      }
    }


  /**
  * Multiply by a constant.  This constructs a new Granule that is the
  * the multiplication of the existing granule by a constant.  Both bounds
  * are multiplied, but the PMF and granularity remain the same.
  * @param n - The divisor
  * @return a new Granule with the lower and upper bounds multiplied by n
  **/
  public Granule multiply(int n) {
    switch (kind) {
      case NowRelative: case Determinate: default: 
        return new Granule(
                           lower.multiply(n)
                          );
      }
    }

  /**
  * Divide by a constant.  This constructs a new Granule that is the
  * the division of the existing granule by a constant. Both bounds
  * are divided, but the PMF and granularity remain the same.
  * @param n - The divisor
  * @return a new Granule with the lower and upper bounds divided by n
  **/
  public Granule divide(int n) {
    switch (kind) {
      case NowRelative: case Determinate: default:
        return new Granule(
                           lower.divide(n)
                          );
      }
    }
  /**
  * A simple test for the class.
  **/ 
  public static void main(String argv[]) throws Exception{
    TimeValue minTV = TimeValue.BEGINNING_OF_TIME;
    TimeValue maxTV = TimeValue.END_OF_TIME;
    TimeValue oneTV = new TimeValue(1);
    TimeValue threeTV = new TimeValue(3);

    Granule min = new Granule(minTV);
    Granule max = new Granule(maxTV);
    Granule one = new Granule(oneTV);
    Granule three = new Granule(threeTV);
    Granule oneToThree = new Granule(oneTV, threeTV);
    Granule minToMax = new Granule(minTV, maxTV);
    Granule result = null;

    System.out.println("max is " + max.image());  
    System.out.println("min is " + min.image()); 
    System.out.println("3 is " + three.image()); 
    System.out.println("1~3 is " + oneToThree.image()); 
    System.out.println("min~max is " + minToMax.image()); 

    // Negate tests
    System.out.println("Testing negate --------------------------------"); 

    System.out.print("negate min = ");
    result = min.negate();  System.out.println(result.image());

    System.out.print("negate max = ");
    result = max.negate();  System.out.println(result.image());

    System.out.print("negate 3 = ");
    result = three.negate();  System.out.println(result.image());

    System.out.print("negate 1~3 = ");
    result = oneToThree.negate();  System.out.println(result.image());

    System.out.print("negate min~max = ");
    result = minToMax.negate();  System.out.println(result.image());

    // Increment tests
    System.out.println("Testing increment --------------------------------"); 

    System.out.print("min++ = ");
    result = min.increment();  System.out.println(result.image());

    System.out.print("max++ = ");
    result = max.increment();  System.out.println(result.image());

    System.out.print("3++ = ");
    result = three.increment();  System.out.println(result.image());

    System.out.print("1~3++ = ");
    result = oneToThree.increment();  System.out.println(result.image());

    System.out.print("min~max++ = ");
    result = minToMax.increment();  System.out.println(result.image());

    // Decrement tests
    System.out.println("Testing decrement --------------------------------"); 

    System.out.print("min-- = ");
    result = min.decrement();  System.out.println(result.image());

    System.out.print("max-- = ");
    result = max.decrement();  System.out.println(result.image());

    System.out.print("3-- = ");
    result = three.decrement();  System.out.println(result.image());

    System.out.print("1~3-- = ");
    result = oneToThree.decrement();  System.out.println(result.image());

    System.out.print("min~max-- = ");
    result = minToMax.decrement();  System.out.println(result.image());

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
    if (one.compareTo(three) < 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 1 < 3? "); 
    if (one.compareTo(three) < 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 < 3? "); 
    if (three.compareTo(three) < 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max < 3? "); 
    if (max.compareTo(three) < 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min < 3? "); 
    if (min.compareTo(three) < 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 < min? "); 
    if (three.compareTo(min) < 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 < max? "); 
    if (three.compareTo(max) < 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min < min? "); 
    if (min.compareTo(min) < 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max < min? "); 
    if (max.compareTo(min) < 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min < max? "); 
    if (min.compareTo(max) < 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max < max? "); 
    if (max.compareTo(max) < 0) System.out.println("Yes"); 
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
    if (one.compareTo(three) > 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 1 > 3? "); 
    if (one.compareTo(three) > 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 > 3? "); 
    if (three.compareTo(three) > 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max > 3? "); 
    if (max.compareTo(three) > 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min > 3? "); 
    if (min.compareTo(three) > 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 > min? "); 
    if (three.compareTo(min) > 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is 3 > max? "); 
    if (three.compareTo(max) > 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min > min? "); 
    if (min.compareTo(min) > 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max > min? "); 
    if (max.compareTo(min) > 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is min > max? "); 
    if (min.compareTo(max) > 0) System.out.println("Yes"); 
    else System.out.println("No"); 

    System.out.print("Is max > max? "); 
    if (max.compareTo(max) > 0) System.out.println("Yes"); 
    else System.out.println("No"); 
}


} // End of class
