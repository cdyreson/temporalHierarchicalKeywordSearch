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
public class LogSegment {

    String segment;

    public LogSegment() {
        segment = "";
    }

    public LogSegment(long i) {
        segment = segment(i);
    }

    public String segment(long s) {
        int size = 6;
        int offset = 2 << (size - 2);
        String prefix = "1";
        s += offset;
        long c = 0;
        long shift = size - 2;
        while (c == 0) {
            System.out.println(" prefix " + prefix);
            long sShifted = s >> shift;

            shift--;
            prefix = prefix + ((sShifted % 2) == 1 ? "1" : "0");

            if (shift < -1) {
                break;
            }
        }
        return prefix;
    }

}
