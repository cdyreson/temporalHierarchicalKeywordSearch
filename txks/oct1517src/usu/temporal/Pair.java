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
import java.util.Objects;

/**
 *
 * @author Curtis Dyreson
 * @param <I>
 * @param <J>
 */
public class Pair<I extends Comparable, J extends Comparable> implements Serializable, Comparable {

    I first;
    J second;

    public Pair(I i, J j) {
        first = i;
        second = j;
    }


    @Override
    public int compareTo(Object obj) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        if (obj == null) {
            return -1;
        }
        if (getClass() != obj.getClass()) {
            return -1;
        }
        
        //this optimization is usually worthwhile, and can
        //always be added
        if (this == obj) {
            return EQUAL;
        }
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        int i = first.compareTo(other.first);
        if (i == 0) {
            return second.compareTo(other.second);
        } else if (i < 0) {
            return BEFORE;
        } else {
            return AFTER;
        }
    }

    @Override
    public int hashCode() {
        return first.hashCode() + second.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        return Objects.equals(this.second, other.second);
    }

}
