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

import java.io.*;
import java.util.*;

/**
 *
 * @author Curtis Dyreson
 */
public class LogSegments {

    Set<String> segments;

    public LogSegments() {
        segments = new HashSet();
    }

    public LogSegments(long s, long e) {
        segments = segment(s, e);
    }

    public Set<String> segment(long s, long e) {
        int size = 6;
        int offset = 2 << (size - 2);
        Set<String> sValues = new HashSet();
        Set<String> eValues = new HashSet();
        String prefix = "1";
        e += offset;
        s += offset;
        long c = 0;
        long shift = size - 2;
        while (c == 0) {
            long sShifted = s >> shift;
            long eShifted = e >> shift;
            if (sShifted != eShifted) {
                c = shift;
            } else {
                shift--;
                prefix = prefix + ((sShifted % 2) == 1 ? "1" : "0");
            }
            if (shift < -1) {
                break;
            }
        }
        String sString = prefix;
        String eString = prefix;
        while (c >= 0) {
            //c--;
            long sShifted = s >> c;
            long eShifted = e >> c;
            if (sShifted % 2 == 0) {
                if (c != shift) {
                    sValues.add(sString + "1");
                }
                sString = sString + "0";
            } else {
                sString = sString + "1";
            }
            if (eShifted % 2 == 1) {
                if (c != shift) {
                    eValues.add(eString + "0");
                }
                eString = eString + "1";
            } else {
                eString = eString + "0";
            }
            c--;
        }
        sValues.add(sString);
        eValues.add(eString);
        sValues.addAll(eValues);
        return sValues;
    }

}
