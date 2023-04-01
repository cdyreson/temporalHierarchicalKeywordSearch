package usu.temporal.timestamp;

/**
* The <code>Granule</code> class implements the arithmetic and comparison 
* operations on Granules.  A Granule is the most basic unit of time in
* a given Granularity.  Each granule is a (TimeValue) distance (in granules).
* Granules can be used for both unanchored and anchored times.  If the time
* is anchored, the Granule represents the distance from the granularity 
* anchor point.  If the time is unanchored, the Granule just represents
* a distance.  Granules can be further classified as determinate or 
* indeterminate.  A determinate granule has a known, fixed distance.
* An indeterminate granule is some distance between a lower and upper 
* bound.  A probability mass function determines the probability of 
* each indeterminate alternative.
*
* Granules can also be now-relative.  
* A now-relative granule is a moving point on the time-line.  It consists
* of the variable <em>now</em> that represents the current time, and an
* granule count that displaces the granule the specified distance 
* from the current time.  For example, the now-relative granule
* 'now + 1 day' indicates that the Granule represented is whatever the
* current time is displaced by one day in the future.
* This class implements now-relative instants that
* have a known, fixed displacement (as opposed to instants that have
* indeterminate displacements).
* Note that none of the operations are mutators, that is, they all
* create a new instant if needed.  
*
*
* @author  Curtis Dyreson
* @version 1.0, Jan/31/2003
* @see     usu.temporal.timestamp.Granule
* @see     usu.temporal.timestamp.TimeValue
* @see     tauzaman.extendedboolean.ExtendedBoolean
**/
public class NowRelativeGranule extends Granule {

  /**
  * Create the now granule at the default Granularity 
  **/ 
  public NowRelativeGranule() {
    super(TimeValue.ZERO_TIMEVALUE);
    this.kind = GranuleType.NowRelative;
    }

  /**
  * Create a now-relative granule at the default Granularity
  * @param p - displacement from now
  *
  * @throws TauZamanException if any abnormal condition occurs when
  * setting this Granule's default Granularity
  **/ 
  public NowRelativeGranule(TimeValue p) {
    super(p);
    this.kind = GranuleType.NowRelative;
    }


  /**
  * Create a now-relative granule at the default Granularity
  * @param p - count of granules
  *
  * @throws TauZamanException if any abnormal condition occurs when
  * setting this Granule's default Granularity
  **/ 
  public NowRelativeGranule(long p) {
    super(p);
    this.kind = GranuleType.NowRelative;
    }

  /**
  * Accessor - retrieve the granule.  When the value is returned
  * it is first bound to the current time and the displacment is added.
  * @return TimeValue object that represents the current time + displacement
  **/
  public TimeValue getGranule() {
    return TimeValue.ZERO_TIMEVALUE.add(lower);
    }

  /**
  * Accessor - retrieve the lower bound from the now-relative granule.
  * The lower bound is computed by displacing the current time with 
  * the lower bound.
  * @return TimeValue object that represents the current lower bound
  **/
  public TimeValue getLower() {
    return TimeValue.ZERO_TIMEVALUE.add(lower);
    }

  /**
  * Accessor - retrieve the upper bound from the now-relative granule.
  * The upper bound is computed by displacing the current time with 
  * the upper bound.
  * @return TimeValue object that represents the current upper bound
  **/
  public TimeValue getUpper() {
    return TimeValue.ZERO_TIMEVALUE.add(upper);
    }

  /**
  * Create a nice string image of a granule, alternative to toString()
  * @return String image of a granule
  * @status NOT IMPLEMENTED for INDETERMINATE GRANULES
  **/
  public String image() {
    return "[NowRelative " + lower.image() + "]";
    }

  /**
  * Construct a NowRelativeGranule that has the relative part decremented by 1
  * @return this - 1
  **/
  public Granule decrement() {
    return new NowRelativeGranule(lower.decrement());
    }

  /**
  * Construct a NowRelativeGranule that has the relative part incremented by 1
  * @return this + 1
  **/
  public Granule increment() {
    return new NowRelativeGranule(lower.increment());
    }

  /**
  * Negate the granule.  This method constructs a new granule that has the
  * relative part negated.  This does NOT NEGATE NOW.
  * @return a new Granule with the relative part negated
  **/
  public Granule negate() {
    return new NowRelativeGranule(lower.negate());
    }

  /**
  * NowRelative/NowRelative comparison - Is this == other?  
  * capture the comparison for other kinds of granules.  
  * @param other - The NowRelativeGranule to compare
  * @return does this == other?
  **/ 
  public boolean equalTo(NowRelativeGranule other) {
    if (this.lower.compareTo(other.getLower()) == 0) return true;
    return false;
    }

  /**
  * NowRelative/NowRelative comparison - Is this < other?  
  * @param other - The NowRelativeGranule to compare
  * @return does this < other?
  **/ 
  public boolean lessThan(NowRelativeGranule other) {
    if (this.lower.compareTo(other.getLower()) < 0) return true;
    return false;
    }

  /**
  * NowRelative/NowRelative comparison - Is this <= other?  
  * @param other - The NowRelativeGranule to compare
  * @return does this <= other?
  **/ 
  public boolean lessThanOrEqualTo(NowRelativeGranule other) {
    if (this.lower.compareTo(other.getLower()) <= 0) return true;
    return false;
    }

  /**
  * NowRelative/NowRelative comparison - Is this > other?  
  * @param other - The NowRelativeGranule to compare
  * @return does this > other?
  **/ 
  public boolean greaterThan(NowRelativeGranule other) {
    if (this.lower.compareTo(other.getLower()) > 0) return true;
    return false;
    }

  /**
  * NowRelative/NowRelative comparison - Is this >= other?  
  * @param other - The NowRelativeGranule to compare
  * @return does this >= other?
  **/ 
  public boolean greaterThanOrEqualTo(NowRelativeGranule other) {
    if (this.lower.compareTo(other.getLower()) >= 0) return true;
    return false;
    }

  /**
  * Add a now-relative to a now-relative.  Only the displacements are added.
  * @param other - The NowRelativeGranule to add
  * return this + other
  **/ 
  public NowRelativeGranule add(NowRelativeGranule other) {
    return new NowRelativeGranule(lower.add(other.getLower()));
    }

  /**
  * Subtract a now-relative from a now-relative.  
  * Only the displacements are subtracted.
  * @param other - The NowRelativeGranule to subtract
  * return this - other
  **/ 
  public NowRelativeGranule subtract(NowRelativeGranule other) {
    return new NowRelativeGranule(lower.subtract(other.getLower()));
    }

  /* this is a solution but needs downcasting... best solution is to 
     have a different name for the method */

  /**
  * Multiply by a constant.  
  * This constructs a new NowRelativeGranule that is the
  * the multiplication of the existing granule by a constant.  
  * The granularity remains the same.
  * @param n - The multiplier
  * @return a new NowRelativeGranule with the relative part multiplied by n
  **/
  public Granule multiply(int n) {
    return new NowRelativeGranule(lower.multiply(n));
    }

  /* this is a solution but needs downcasting... best solution is to 
     have a different name for the method */

  /**
  * Divide by a constant.  
  * This constructs a new NowRelativeGranule that is the
  * the division of the existing granule by a constant.  
  * The granularity remains the same.
  * @param n - The divisor
  * @return a new NowRelativeGranule with the relative part divided by n
  **/
  public Granule divide(int n) {
    return new NowRelativeGranule(lower.divide(n));
    }



// End of class
}
