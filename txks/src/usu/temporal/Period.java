package usu.temporal;

import usu.temporal.timestamp.Granule;
import java.rmi.*;


/**
* The <code>Period</code> class provides a set of constructor and accessor
* operations on periods.
* (Arithmetic and comparison operations are implemented in a Semantics.)
* A period is the time between two Instants anchored to the time-line,
* for instance, "[July 4, 2002 - July 12, 2002]".  All periods, when
* represented in a Period object, are 
* assumed to be closed, that is the endpoints are included in the
* time of the period (note however, that periods may be constructed
* from strings that represent either open or closed periods, it is just
* that the constructed period will be assumed to be closed).
* Currently there is a constraint that the Instants in the period
* must have either be granularity-less values (e.g., MIN) or have
* the same granularity.  Futhermore the starting Instant must come 
* before the ending Instant.
* This is the standard package that supports operations on periods that
* have a known, fixed position as well as those that 
* indeterminately known.
*
* To perform comparisons and arithmetic operations on Periods, 
* it is first necessary to create a Semantics interface.
* A Semantics implements common comparison and arithmetic operations on 
* temporal entities.  For example a user would write code like the following.
*  <pre>
*     // Left operand semantics casts operands in binary operations 
*     // to the granularity of the left operand, and then performs
*     // the desired operation. 
*     Ops = new LeftOperandSemantics();
*     // Create two Periods, in this case, both are at the granularity of days
*     Period i = new Period("[July 3, 2002 - July 10, 2002]");
*     Period j = new Period("[July 4, 2002 - July 5, 2002]");
*     // Is i earlier on the time-line than j?
*     if (Ops.precedes(i,j)) ...
*  </pre>
* We employ this strategy so that all the period operations can be
* performed under varying Semantics.
*
* @author  Curtis Dyreson 
* @version 0.1, 10/10/02
* @see     tauzaman.semantics.DeterminateSemantics
* @see     tauzaman.semantics.IndeterminateSemantics
**/
public class Period implements Cloneable {

  private Instant startingInstant;
  private Instant endingInstant;

  /** <code>CalendricSystem</code> that this <code>Instant</code> is created under **/


  /**
  * Construct a Period from a pair of Instants.  It will first convert
  * each Instant to the desired granularity using the default conversion
  * semantics. It will also ensure 
  * that the starting Instant is before the ending Instant.  
  * @param g - Granularity of desired period
  * @param startingInstant - first Instant
  * @param endingInstant - second Instant
  * @exception badPeriod - possibly terminates before it starts, currently
  *  it will set the starting and terminating Instants
  *  as the same Instant.
  * @status NOT IMPLEMENTED
  **/
  public Period(Instant startingInstant, Instant endingInstant) {
    // Need to scale!
    this.startingInstant = startingInstant;
    this.endingInstant = endingInstant;
    int lt = this.startingInstant.getGranule().compareTo(this.endingInstant.getGranule());
    if (lt > 0) {
      this.endingInstant = this.startingInstant;
    //  throw exception "Trying to create a period that ends before it starts"
      }
    }


  /** 
  * Build a nice string image of an Period, for debugging mostly.
  * Provides an alternative to toString().
  * @return String image of Period
  **/
  public String image() {
    return "[Period" + 
           " startingInstant " + startingInstant.image() + 
           " endingInstant " + endingInstant.image() + "]";
    }

  /**
  * Selector - Returns the starting Instant in the Period.
  * @return Instant - the Instant
  **/
  public Instant getStarting() {
    return startingInstant;
    }

  /**
  * Selector - Returns the ending Instant in the Period.
  * @return Instant - the Instant
  **/
  public Instant getEnding() {
    return endingInstant;
    }
  
   

  /**
  * Selector - Returns the earliest Granule in the Period.
  * @return Granule - the Granule of the Starting Instant
  **/
  public Granule getEarliest() {
    return startingInstant.getGranule();
    }

  /**
  * Selector - Returns the latest Granule in the Period.
  * @return the ending instant's granule
  **/
  public Granule getLatest() {
    return endingInstant.getGranule();
    }

}