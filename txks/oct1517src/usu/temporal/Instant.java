package usu.temporal;

import usu.temporal.timestamp.Granule;
import java.rmi.*;

import usu.temporal.timestamp.Granule.GranuleType;

/**
* The <code>Instant</code> class provides a set of constructor and accessor
* operations on Instants.
* (Arithmetic and comparison operations are implemented in a Semantics.)
* An instant is a
* point on the time-line, e.g., 'July 3, 2002'.
* An instant could also be indeterminately known, 
* e.g., "July 3, 2002 ~ July 4, 2002".
*
* To perform comparisons and arithmetic operations on Instants, 
* it is first necessary to create a Semantics interface.
* A Semantics implements common comparison and arithmetic operations on 
* temporal entities.  For example a user would write code like the following
*  <pre>
*     // Left operand semantics casts operands in binary operations 
*     // to the granularity of the left operand, and then performs
*     // the desired operation. 
*     Ops = new LeftOperandSemantics();
*     // Create two Instants, in this case, both are at the granularity of days
*     Instant i = new Instant("July 3, 2002");
*     Instant j = new Instant("July 4, 2002");
*     // Is i earlier on the time-line than j?
*     if (Ops.precedes(i,j)) ...
*  </pre>
* We employ this strategy so that all the instant operations can be
* performed under varying Semantics.
*
* @author  Curtis Dyreson 
* @version 0.9, Jan/31/2003
* @see     tauzaman.calendricsystem.temporaldatatypes.semantics.DeterminateSemantics
* @see     tauzaman.calendricsystem.temporaldatatypes.semantics.IndeterminateSemantics
* @status  design complete, NOT IMPLEMENTED
**/
public class Instant implements Cloneable {
  // The BEGINNING_OF_TIME Instant constant represents a value that
  // is the start of the time-line.  
  // It is a granularity-less constant.
 // public static final Instant BEGINNING_OF_TIME = 
 //     new Instant(new Granule(TimeValue.BEGINNING_OF_TIME));

  // The END_OF_TIME Instant constant represents a value that
  // is the end of the time-line.  
  // It is a granularity-less constant.
//  public static final Instant END_OF_TIME =
//      new Instant(new Granule(TimeValue.END_OF_TIME));


  // Instants have a single Granule
  protected Granule granule;


  /**
  * Construct an Instant from a granule representing 
  * the distance from the granularity anchor point
  * @param granule - distance in granules from the anchor
  **/
  public Instant(Granule granule) {
    this.granule = granule;
  }

    /** 
  * Build a nice string image of an Instant, for debugging mostly.  Provides
  * an alternative to toString().
  * @return String image of Instant
  **/
  public String image() {
    return "[Instant " + granule.image() + "]";
    }

  /** 
  * Generate the hash code value, needed for supporting Hashtables.
  * @return int 
  **/
//  public int hashCode() {
//    return granule.hashCode();
//    }

  /**
  * Returns the earliest (only!) Granule in the Instant.
  * @return Granule - the Granule for this Instant
  **/
  public Granule earliest() {
    return this.granule;
    }

  /**
  * Returns the latest (only!) Granule in the Instant.
  * @return Granule - the latest Granule
  **/
  public Granule latest() {
    return this.granule;
    }

  /**
  * Retrieve the granule from this Instant
  * @return Granule
  **/
  public Granule getGranule() {
    return granule;
    }

}
