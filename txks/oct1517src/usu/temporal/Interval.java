package usu.temporal;

import usu.temporal.timestamp.Granule;

/**
* The <code>Interval</code> class provides a set of constructor and accessor
* operations for intervals.
* (Arithmetic and comparison operations are implemented in a @see Semantics.)
* An interval is a length of time, unanchonred to any specific
* point or region on the time-line.  For example, the interval "1 day" 
* represents a length.  It can be used, for example, to displace values
* anchored to the time-line, e.g., "July 3, 2002", by a distance of "1 day",
* moving the instant to "July 4, 2002".
* This class implements intervals that have a known, fixed length as well as
* intervals that are indeterminately known.
*
* To perform comparisons and arithmetic operations on Intervals, 
* it is first necessary to create a @see Semantics interface.
* A Semantics implements common comparison and arithmetic operations on 
* temporal entities.  For example a user would write code like the following
*  <pre>
*     // Left operand semantics casts operands in binary operations 
*     // to the granularity of the left operand, and then performs
*     // the desired operation. 
*     Ops = new LeftOperandSemantics();
*     // Create an Instant and an Interval.
*     // In this example, both are at the granularity of days.
*     Instant i = new Instant("July 3, 2002");
*     Interval j = new Interval("1 day");
*     // Displace i by length j, k will represent the day "July 4, 2002" 
*     Instant k = Ops.add(i,j);
*  </pre>
* We employ this strategy so that interval operations can be
* performed under varying Semantics.
*
* @author  Curtis Dyreson 
* @version 0.9, Jan/31/2003
* @see     tauzaman.semantics.DeterminateSemantics
* @see     tauzaman.semantics.IndeterminateSemantics
* @status  design complete, NOT IMPLEMENTED
**/
public class Interval implements Cloneable {
  // The ALL_OF_TIME Interval constant represents the distance of  
  // the entire time-line.  It is a granularity-less constant.
  //public static final Interval ALL_OF_TIME = 
  //    new Interval(new Granule(TimeValue.ALL_OF_TIME));

  // The NEGATIVE_ALL_OF_TIME Interval constant represents the negated
  // distance of the entire time-line.  It is a granularity-less constant.
  //public static final Interval NEGATIVE_ALL_OF_TIME = 
  //    new Interval(new Granule(TimeValue.NEGATIVE_ALL_OF_TIME));

  // Intervals have a single Granule
  private Granule granule;


  /**
  * Construct an Interval from a count of granules. 
  * @param granule - length in granules
  **/
  Interval(Granule granule) {
		this.granule = granule;
	}
 
  
  /**  
  * Construct an Interval as the distance between a period's endpoints
  * @param period - the input period
  * @status NOT IMPLEMENTED
  **/
  Interval (Period p) {
      this.granule = null;  // Can we do this one?
   }
  
  /** 
  * Build a nice string image of an Interval, for debugging mostly.
  * Provides an alternative to toString().
  * @return String image of Interval
  **/
  public String image() {
    return "[Interval " + granule.image() + "]";
    }
 
  /** 
  * Get the smallest (only!) Granule from this Interval.
  * @return the Interval's granule
  * @status Is this used?
  **/
  public Granule smallest() {
    return this.granule;
    }

  /** 
  * Get the largest (only!) Granule in this Interval.
  * @return the Interval's granule
  * @status Is this used?
  **/
  public Granule largest() {
    return this.granule;
    }

  /** 
  * Accessor - retrieve the Granule from this Interval.
  * @return the Interval's granule
  **/
  public Granule granule() {
    return this.granule;
    }

}
