/*
 * Copyright (C) 2015 Curtis Dyreson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package usu.temporal;

//import com.sleepycat.bind.tuple.TupleInput;
//import com.sleepycat.bind.tuple.TupleOutput;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
//import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 * A TimeElement is a set of coalesced Time objects. Coalesced means that the
 * times are disjoint. The TimeElement is maintained in sorted order of Time. It
 * is not Serializable.
 *
 * @author Curtis Dyreson
 */
public class TimeElement /*implements Serializable*/ {

    TreeSet<Time> times;
    Iterator<Time> timeIterator;

    public TimeElement() {
        //timeSet = new HashSet();
        times = new TreeSet();
    }

    /* 
     Construct a new TimeElement from a single Time
     */
    public TimeElement(Time t) {
        //timeSet = new HashSet();
        times = new TreeSet();
        times.add(t);
    }

    /* Put a new time into the sorted order */
    public void add(Time time) {
        times.add(time);
    }

    public Set<Time> getTimes() {
        return times;
    }

    /* Merge overlapping times */
    public void coalesce() {
        TreeSet<Time> coalesced = new TreeSet();
        Time previousTime = null;
        for (Time time : times) {
            if (previousTime == null) {
                previousTime = time;
                continue;
            }
            if (time.overlaps(previousTime)) {
                previousTime.setEnd(time.end);
            } else {
                coalesced.add(previousTime);
            }
        }
        if (previousTime != null) {
            coalesced.add(previousTime);
        }
        times = coalesced;
    }

    /*
     *  Check to see if there is any time in this element
     */
    public boolean isEmpty() {
        return times.isEmpty();
    }

    /*
     A Constructor,
     * Computes the difference of this Element with another.
     */
    public TimeElement difference(TimeElement other) {

        // If either is empty, exit
        if (this.isEmpty() || other.isEmpty()) {
            //System.out.println("difference isEmpty ");
            return this;
        }

        TimeElement constructed = new TimeElement();

        Iterator<Time> thisIter = times.iterator();
        Iterator<Time> otherIter = other.times.iterator();

        Time time = thisIter.next();
        Time otherTime = otherIter.next();
        //System.out.println("difference notEmpty ");
        do {
            // Check if this time is before the other
            if (time.before(otherTime)) {
                // Rest of loop not executed
                if (thisIter.hasNext()) {
                    time = thisIter.next();
                    //System.out.println("difference continuing " + time + " other " + otherTime);
                    continue;
                } else {
                    // No more times, done
                    //System.out.println("difference breaking " + time + " other " + otherTime);
                    break;
                }
            }

            // Check if the other time is before this, 
            if (otherTime.before(time)) {
                if (otherIter.hasNext()) {
                    //System.out.println("difference other continuing " + time + " other " + otherTime);
                    otherTime = otherIter.next();
                    continue;
                } else {
                    // No more times, done
                    //System.out.println("difference other breaking " + time + " other " + otherTime);
                    break;
                }
            }

            // Neither is before the other, must have some overlap
            List<Time> diff = time.difference(otherTime);
            //System.out.println("difference diff " + diff + " = " + time + " - " + otherTime);
            // Check if we have some output from difference
            if (diff != null) {
                // Add any times produced by the difference to constructed
                for (Time t : diff) {
                    constructed.add(t);
                }
            }

            // Need to advance one or the other
            if (time.endsBefore(otherTime)) {
                if (thisIter.hasNext()) {
                    time = thisIter.next();
                } else {
                    // No more times, done
                    break;
                }

            } else {
                if (otherIter.hasNext()) {
                    otherTime = otherIter.next();
                } else {
                    // No more times, done
                    break;
                }
            }
        } while (true);

        // Not sure if coalescing is needed
        constructed.coalesce();
        return constructed;
    }

    /*
     * A Constructor,
     * Computes the meeting of this Element with another.
     * Returns empty TimeElement if there is no meeting.
     * Returns every interval in the first and second interval combined if
     * they actually meet (one ends as the other begins)
     */
    public TimeElement meets(TimeElement other) {
        TimeElement constructed = new TimeElement();

        // If either is empty, exit
        if (this.isEmpty() || other.isEmpty()) {
            return constructed;
        }

        Iterator<Time> thisIter = times.iterator();
        Iterator<Time> otherIter = other.times.iterator();

        Time time = thisIter.next();
        Time otherTime = otherIter.next();

        do {
            // Check if we meet
            if (time.meets(otherTime)) {
                constructed.add(new Time(time.getBeginTime(), otherTime.getEndTime()));
                if (thisIter.hasNext()) {
                    time = thisIter.next();
                } else {
                    // No more times, done
                    break;
                }
                if (otherIter.hasNext()) {
                    otherTime = otherIter.next();
                } else {
                    // No more times, done
                    break;
                }
                continue;
            }

            // Check if this time is before the other
            if (time.before(otherTime)) {
                // Rest of loop not executed
                if (thisIter.hasNext()) {
                    time = thisIter.next();
                    continue;
                } else {
                    // No more times, done
                    break;
                }
            }

            // Check if the other time is before this, 
            if (otherTime.before(time)) {
                if (otherIter.hasNext()) {
                    otherTime = otherIter.next();
                    continue;
                } else {
                    // No more times, done
                    break;
                }
            }

            // Neither is before the other or meet so advance both
            Time overlap = time.intersection(otherTime);
            if (thisIter.hasNext()) {
                time = thisIter.next();
            } else {
                // No more times, done
                break;
            }
            if (otherIter.hasNext()) {
                otherTime = otherIter.next();
            } else {
                // No more times, done
                break;
            }
            continue;

        } while (true);

        constructed.coalesce();
        return constructed;
    }
    
        /*
     A Constructor,
     * Computes the intersection of this Element with another.
     * Returns empty TimeElement if there is no intersection.
     */
    public TimeElement intersection(TimeElement other) {
        TimeElement constructed = new TimeElement();

        // If either is empty, exit
        if (this.isEmpty() || other.isEmpty()) {
            return constructed;
        }

        Iterator<Time> thisIter = times.iterator();
        Iterator<Time> otherIter = other.times.iterator();

        Time time = thisIter.next();
        Time otherTime = otherIter.next();

        do {
            // Check if this time is before the other
            if (time.before(otherTime)) {
                // Rest of loop not executed
                if (thisIter.hasNext()) {
                    time = thisIter.next();
                    continue;
                } else {
                    // No more times, done
                    break;
                }
            }

            // Check if the other time is before this, 
            if (otherTime.before(time)) {
                if (otherIter.hasNext()) {
                    otherTime = otherIter.next();
                    continue;
                } else {
                    // No more times, done
                    break;
                }
            }

            // Neither is before the other, must have some overlap
            Time overlap = time.intersection(otherTime);
            if (overlap != null) {
                constructed.add(overlap);
            }

            // Need to advance one or the other
            if (time.endsBefore(otherTime)) {
                if (thisIter.hasNext()) {
                    time = thisIter.next();
                } else {
                    // No more times, done
                    break;
                }

            } else {
                if (otherIter.hasNext()) {
                    otherTime = otherIter.next();
                } else {
                    // No more times, done
                    break;
                }
            }
        } while (true);

        constructed.coalesce();
        return constructed;
    }

    /*
     A Constructor,
     * Computes the intersection of this Element with a single Time.
     * Returns empty TimeElement if there is no intersection.
     */
    public TimeElement intersection(Time other) {
        TimeElement constructed = new TimeElement();

        // If either is empty, exit
        if (this.isEmpty()) {
            return constructed;
        }

        Iterator<Time> thisIter = times.iterator();

        Time time = thisIter.next();
        Time otherTime = other;

        do {
            // Check if this time is before the other
            if (time.before(otherTime)) {
                // Rest of loop not executed
                if (thisIter.hasNext()) {
                    time = thisIter.next();
                    continue;
                } else {
                    // No more times, done
                    break;
                }
            }

            // Check if the other time is before this, 
            if (otherTime.before(time)) {
                // No more times, done
                break;
            }

            // Neither is before the other, must have some overlap
            Time overlap = time.intersection(otherTime);
            if (overlap != null) {
                constructed.add(overlap);
            }

            // Need to advance one or the other
            if (time.endsBefore(otherTime)) {
                if (thisIter.hasNext()) {
                    time = thisIter.next();
                } else {
                    // No more times, done
                    break;
                }

            } else {
                // No more times, done
                break;

            }
        } while (true);

        constructed.coalesce();
        return constructed;
    }


    /*
     A Constructor,
     * Computes the times at which this Element contains another.
     * Returns empty TimeElement if there is no containment.
     */
    public TimeElement contains(TimeElement other) {
        TimeElement constructed = new TimeElement();

        // If either is empty, exit
        if (this.isEmpty() || other.isEmpty()) {
            return constructed;
        }

        Iterator<Time> thisIter = times.iterator();
        Iterator<Time> otherIter = other.times.iterator();

        Time time = thisIter.next();
        Time otherTime = otherIter.next();

        do {
            // Check if this time is before the other
            if (time.before(otherTime)) {
                // Rest of loop not executed
                if (thisIter.hasNext()) {
                    time = thisIter.next();
                    continue;
                } else {
                    // No more times, done
                    break;
                }
            }

            // Check if the other time is before this, 
            if (otherTime.before(time)) {
                if (otherIter.hasNext()) {
                    otherTime = otherIter.next();
                    continue;
                } else {
                    // No more times, done
                    break;
                }
            }

            // Neither is before the other, must have some overlap
            Time containment = time.containsIfOverlaps(otherTime);
            if (containment != null) {
                constructed.add(containment);
            }

            // Need to advance one or the other
            if (time.endsBefore(otherTime)) {
                if (thisIter.hasNext()) {
                    time = thisIter.next();
                } else {
                    // No more times, done
                    break;
                }

            } else {
                if (otherIter.hasNext()) {
                    otherTime = otherIter.next();
                } else {
                    // No more times, done
                    break;
                }
            }
        } while (true);

        constructed.coalesce();
        return constructed;
    }

    /*
     A Constructor,
     * Computes the time this Element contains a single Time.
     * Returns empty TimeElement if there is no intersection.
     */
    public TimeElement contains(Time other) {
        TimeElement constructed = new TimeElement();

        // If either is empty, exit
        if (this.isEmpty()) {
            return constructed;
        }

        Iterator<Time> thisIter = times.iterator();

        Time time = thisIter.next();
        Time otherTime = other;

        do {
            // Check if this time is before the other
            if (time.before(otherTime)) {
                // Rest of loop not executed
                if (thisIter.hasNext()) {
                    time = thisIter.next();
                    continue;
                } else {
                    // No more times, done
                    break;
                }
            }

            // Check if the other time is before this, 
            if (otherTime.before(time)) {
                // No more times, done
                break;
            }

            // Neither is before the other, must have some overlap
            Time containment = time.containsIfOverlaps(otherTime);
            if (containment != null) {
                constructed.add(containment);
            }

            // Need to advance one or the other
            if (time.endsBefore(otherTime)) {
                if (thisIter.hasNext()) {
                    time = thisIter.next();
                } else {
                    // No more times, done
                    break;
                }

            } else {
                // No more times, done
                break;

            }
        } while (true);

        constructed.coalesce();
        return constructed;
    }

    /*
     A Constructor,
     * Computes the union of this Element with another.
     * Returns empty TimeElement if there is no union.
     */
    public TimeElement union(TimeElement other) {
        // If either is empty, exit
        if (other.isEmpty()) {
            //System.out.println("union isEmpty ");
            return this;
        }
        if (this.isEmpty()) {
            //System.out.println("union isEmpty ");
            return other;
        }

        TimeElement constructed = new TimeElement();

        Iterator<Time> thisIter = times.iterator();
        Iterator<Time> otherIter = other.times.iterator();

        Time time = thisIter.next();
        Time otherTime = otherIter.next();
        //System.out.println("union notEmpty ");
        do {
            // Check if this time is before the other
            if (time.before(otherTime)) {
                constructed.add(time);
                // Rest of loop not executed
                if (thisIter.hasNext()) {
                    time = thisIter.next();
                    //System.out.println("union continuing " + time + " other " + otherTime);
                    continue;
                } else {
                    // No more times, done
                    //System.out.println("union breaking " + time + " other " + otherTime);
                    break;
                }
            }

            // Check if the other time is before this, 
            if (otherTime.before(time)) {
                constructed.add(time);
                if (otherIter.hasNext()) {
                    //System.out.println("union other continuing " + time + " other " + otherTime);
                    otherTime = otherIter.next();
                    continue;
                } else {
                    // No more times, done
                    //System.out.println("union other breaking " + time + " other " + otherTime);
                    break;
                }
            }

            // Neither is before the other, must have some overlap
            Time union = time.unionIfOverlaps(otherTime);
            //System.out.println("union " + union + " = " + time + " - " + otherTime);
            // Check if we have some output from difference
            constructed.add(union);

            // Need to advance one or the other
            if (time.endsBefore(otherTime)) {
                if (thisIter.hasNext()) {
                    time = thisIter.next();
                } else {
                    // No more times, done
                    break;
                }

            } else {
                if (otherIter.hasNext()) {
                    otherTime = otherIter.next();
                } else {
                    // No more times, done
                    break;
                }
            }
        } while (true);

        // Not sure if coalescing is needed
        constructed.coalesce();
        return constructed;
    }

    public TimeElement unionOld(TimeElement other) {
        // Add them all and coalesce
        TimeElement constructed = new TimeElement();
        for (Time t : times) {
            constructed.add(t);
        }
        for (Time t : other.times) {
            constructed.add(t);
        }

        constructed.coalesce();
        return constructed;
    }

    public String toString() {
        String s = "";
        for (Time t : times) {
            s += t + ",";
        }
        return "{" + s + "}";
    }

}
