/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-06 The eXist Project
 *  http://exist-db.org
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  $Id: DLN.java 9474 2009-07-23 20:12:38Z dizzzz $
 */
package usu.dln.old;

import usu.dln.*;
import java.io.Serializable;
import java.util.Arrays;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * Represents a node id in the form of a dynamic level number (DLN). DLN's are
 * hierarchical ids, which borrow from Dewey's decimal classification. Examples
 * for node ids: 1, 1.1, 1.2, 1.2.1, 1.2.2, 1.3. In this case, 1 represents the
 * root node, 1.1 is the first node on the second level, 1.2 the second, and so
 * on.
 *
 * To support efficient insertion of new nodes between existing nodes, we use
 * the concept of sublevel ids. Between two nodes 1.1 and 1.2, a new node can be
 * inserted as 1.1/1, where the / is the sublevel separator. The / does not
 * start a new level. 1.1 and 1.1/1 are thus on the same level of the tree.
 *
 * In the binary encoding, the '.' is represented by a 0-bit while '/' is
 * written as a 1-bit.
 */
public class OldDLN implements Serializable {

    // the bits are stored in a byte[] 

    protected byte[] bits;

    // the current index into the byte[] used
    // for appending new bits
    protected transient short bitIndex = -1;
    // holds the serialized version of the object, so we can put the bitIndex 
    // after the bits so that BerkeleyDB can properly order it.
    //protected byte[] serialized;

    /**
     * The default number of bits used per fixed size unit.
     *
     */
    public final transient static int BITS_PER_UNIT = 4;
    public final transient static int[] BIT_MASK = new int[8];
    public final transient static int SIZE_OF_BITINDEX = 2;

    static {
        BIT_MASK[0] = 0x80;
        for (int i = 1; i < 8; i++) {
            int mask = 1 << (7 - i);
            BIT_MASK[i] = mask + BIT_MASK[i - 1];
        }
    }

    /**
     * Lists the maximum number that can be encoded by a given number of units.
     * PER_COMPONENT_SIZE[0] corresponds to 1 unit used, PER_COMPONENT_SIZE[1]
     * to 2 units, and so on. With BITS_PER_UNIT = 4, the largest number to be
     * encoded by 1 unit is 7, for 2 units it's 71, for 3 units 583 ...
     */
    private transient static int[] PER_COMPONENT_SIZE = initComponents();

    private static int[] initComponents() {
        //System.out.println("InitComponents");
        int size[] = new int[10];
        size[0] = 7;  // = Math.pow(2, 3) - 1;
        int components, numBits;
        for (int i = 1; i < size.length; i++) {
            components = i + 1;
            numBits = components * BITS_PER_UNIT - components;
            size[i] = (int) (Math.pow(2, numBits)) + size[i - 1];
        }
        return size;
    }

    public transient final static int IS_CHILD = 1;
    public transient final static int IS_DESCENDANT = 2;
    public transient final static int IS_SELF = 3;

    protected transient static final OldDLN ROOT_NODE = new OldDLN(1);
    // Establish a node that is last in the ordering, greater than the root node
    protected transient static final OldDLN MAX_NODE = new OldDLN(2);
    // Establish a node that is first in the ordering, greater than the root node
    protected transient static final OldDLN MIN_NODE = new OldDLN(0);

    protected final transient static int UNIT_SHIFT = 3;
    /**
     * A 0-bit is used to mark the start of a new level
     */
    protected final transient static int LEVEL_SEPARATOR = 0;
    /**
     * A 1-bit marks the start of a sub level, which is logically a part of the
     * current level.
     */
    protected final transient static int SUBLEVEL_SEPARATOR = 1;

    public OldDLN(OldDLN dln) {
        //System.out.println("Curt: DLN " + dln);
        this.bits = new byte[dln.bits.length];
        //this.bits = new byte[NUMBEROFBYTES];
        //for (int i = 0; i < NUMBEROFBYTES; i++) {
        //    bits[1] = (byte) 0;
        //}
        //System.out.println("CURT: " + this.bits.length + " " + dln.bits.length);
        System.arraycopy(dln.bits, 0, this.bits, 0, dln.bits.length);
        //System.arraycopy(dln.bits, 0, this.bits, 0, NUMBEROFBYTES-1);
        this.restoreBitIndex();
        //this.bitIndex = dln.bitIndex;
    }

    public OldDLN(byte[] b) {
        //this.bits = b;
        int length = b[15];
        this.bits = new byte[length];
        System.arraycopy(b,0,this.bits,0,length);
        this.restoreBitIndex();
    }

    public OldDLN(short units, byte[] data, int startOffset) {
        if (units < 0) {
            throw new IllegalArgumentException("Negative size for DLN: " + units);
        }
        int blen = units / 8;
        if (units % 8 > 0) {
            ++blen;
        }
        bits = new byte[blen + SIZE_OF_BITINDEX];
        //bits = new byte[NUMBEROFBYTES];
        //for (int i = 0; i < NUMBEROFBYTES; i++) {
        //    bits[1] = (byte) 0;
        //}
        //System.out.println("Copying " + units + " " + data.length + " " + startOffset + " " + blen);
        System.arraycopy(data, startOffset, bits, 0, blen);
        bitIndex = (short) (units - 1);
        encodeBitIndex();
        //schemaTypeID = ByteConversion.byteToInt(data, blen + startOffset);
    }

    protected OldDLN(byte[] data, short nbits) {
        int remainder = nbits % 8;
        int len = nbits / 8;
        bits = new byte[len + (remainder > 0 ? 1 : 0) + SIZE_OF_BITINDEX];
        //bits = new byte[NUMBEROFBYTES];
        //for (int i = 0; i < NUMBEROFBYTES; i++) {
        //    bits[1] = (byte) 0;
        //}
        if (len > 0) {
            System.arraycopy(data, 0, bits, 0, len);
        }
        if (remainder > 0) {
            byte b = 0;
            for (int i = 0; i < remainder; i++) {
                if ((data[len] & (1 << ((7 - i) & 7))) != 0) {
                    b |= 1 << (7 - i);
                }
            }
            bits[len] = b;
        }
        bitIndex = (short) (nbits - 1);
        encodeBitIndex();
    }

    /**
     * Constructs a new DLN by parsing the string argument. In the string,
     * levels are separated by a '.', sublevels by a '/'. For example, '1.2/1'
     * or '1.2/1.2' are valid ids.
     *
     * @param s
     */
    public OldDLN(String s) {
        bits = new byte[1 + SIZE_OF_BITINDEX];
        //bits = new byte[NUMBEROFBYTES];
        //for (int i = 0; i < NUMBEROFBYTES; i++) {
        //    bits[1] = (byte) 0;
        //}
        StringBuilder buf = new StringBuilder(16);
        boolean subValue = false;
        for (int p = 0; p < s.length(); p++) {
            char ch = s.charAt(p);
            if (ch == '.' || ch == '/') {
                addLevelId(Integer.parseInt(buf.toString()), subValue);
                subValue = ch == '/';
                buf.setLength(0);
            } else {
                buf.append(ch);
            }
        }
        if (buf.length() > 0) {
            addLevelId(Integer.parseInt(buf.toString()), subValue);
        }
    }

    /**
     * Constructs a new DLN, using the passed id as its single level value.
     *
     * @param id
     */
    public OldDLN(int id) {
        bits = new byte[1 + SIZE_OF_BITINDEX];
        //bits = new byte[NUMBEROFBYTES];
        //for (int i = 0; i < NUMBEROFBYTES; i++) {
        //   bits[1] = (byte) 0;
        //}
        addLevelId(id, false);
    }

    protected void encodeBitIndex() {
        int i = this.bits.length;
        // Put bitIndex in place
        bits[i - 1] = (byte) (bitIndex & 0xff);
        bits[i - 2] = (byte) ((bitIndex >>> 8) & 0xff);
    }

    protected void restoreBitIndex() {
        int i = this.bits.length;
        this.bitIndex = (short) ((((this.bits[i - 2] & 0xff) << 8)) | ((this.bits[i - 1] & 0xff)));
        //System.out.println("BitIndex " + this.bitIndex + " " + this.bits[i-2] + " " + this.bits[i-1]);
    }

    /**
     * Compute the common lca at this level (or below)
     *
     * @return common lca, or null if none
     */
    public OldDLN commonLCA(OldDLN other, int level) {
        //level = (level == 0) ? 0 : level - 1;
        for (int i = level; i >= 1; i--) {
            int bitCount = getStartBitAtLevel(i);
            int otherBitCount = other.getStartBitAtLevel(i);
            if (bitCount > otherBitCount) {
                bitCount = otherBitCount;
            }
            //System.out.println("Has Common LCA " + bitCount + " " + level + " " + otherBitCount + " " + this.toBitString() + " " + other.toBitString() + " " + this.toString() + " " + other.toString());
            if (compareBitsExact(other, bitCount)) {
                return new OldDLN(this.bits, (short) bitCount);
            }
        }
        return new OldDLN(this.bits, (short) getStartBitAtLevel(0));
        //System.out.println("Has Common LCA " + result + " " + bitCount + " " + level + " " + otherBitCount + " " + this.toBitString() + " " + other.toBitString() + " " + this.toString() + " " + other.toString());

    }

    /**
     * Is this one equal to the other to the given level in the tree? Used to
     * check if they have a common lca at this level (or below)
     *
     * @return whether they are equal
     */
    public boolean hasCommonLCA(OldDLN other, int level) {
        //level = (level == 0) ? 0 : level - 1;
        int bitCount = getStartBitAtLevel(level);
        int otherBitCount = other.getStartBitAtLevel(level);
        if (bitCount > otherBitCount) {
            bitCount = otherBitCount;
        }
        //System.out.println("Has Common LCA " + bitCount + " " + level + " " + otherBitCount + " " + this.toBitString() + " " + other.toBitString() + " " + this.toString() + " " + other.toString());
        boolean result = compareBitsExact(other, bitCount);
        //System.out.println("Has Common LCA " + result + " " + bitCount + " " + level + " " + otherBitCount + " " + this.toBitString() + " " + other.toBitString() + " " + this.toString() + " " + other.toString());
        return result;
    }

    /**
     * Returns the number of sub-levels in the id starting at startOffset. This
     * is required to determine where a node can be inserted.
     *
     * @param level
     * @return number of sub-levels
     */
    public int getStartBitAtLevel(int level) {
        int bit = 0;
        while (bit > -1 && bit <= bitIndex && level > 0) {
            int units = unitsUsed(bit, bits);
            bit += units;
            bit += bitWidth(units);
            if (bit < bitIndex) {
                --level;
                if ((bits[bit >> UNIT_SHIFT] & (1 << ((7 - bit++) & 7))) == LEVEL_SEPARATOR) {
                    //break;
                }
            } else {
                --level;
            }
        }
        //System.out.println("level is " + bit);
        return bit;
    }

    /**
     * Returns the number of sub-levels in the id starting at startOffset. This
     * is required to determine where a node can be inserted.
     *
     * @param level
     * @return number of sub-levels
     */
    public int getStartBitAtLevel2(int level) {
        int bit = 0;
        while (bit > -1 && bit <= bitIndex && level > 0) {
            int units = unitsUsed(bit, bits);
            bit += units;
            bit += bitWidth(units);
            if (bit < bitIndex) {
                --level;
                if ((bits[bit >> UNIT_SHIFT] & (1 << ((7 - bit++) & 7))) == LEVEL_SEPARATOR) {
                    //break;
                }
            } else {
                --level;
            }
        }
        //System.out.println("level is " + bit);
        return bit;
    }

    /**
     * Set the level id which starts at offset to the given id value.
     *
     * @param offset
     * @param levelId
     */
    public void setLevelId(short offset, int levelId) {
        bitIndex = (short) (offset - 1);
        encodeBitIndex();
        setCurrentLevelId(levelId);
    }

    /**
     * Adds a new level to the node id, using levelId as initial value.
     *
     * @param levelId initial value
     */
    public void addLevelId(int levelId, boolean isSubLevel) {
        if (bitIndex > -1) {
            setNextBit(isSubLevel);
        }
        setCurrentLevelId(levelId);
    }

    /**
     * Increments the last level id by one.
     */
    public void incrementLevelId() {
        short last = lastFieldPosition();
        bitIndex = (short) (last - 1);
        encodeBitIndex();
        setCurrentLevelId(getLevelId(last) + 1);
    }

    public void decrementLevelId() {
        short last = lastFieldPosition();
        bitIndex = (short) (last - 1);
        encodeBitIndex();
        int levelId = getLevelId(last) - 1;
        if (levelId < 1) {
            levelId = 0;
        }
        setCurrentLevelId(levelId);

        // after decrementing, the DLN may need less bytes
        // than before. Remove the unused bytes, otherwise binary
        // comparisons may get wrong.
        int len = bitIndex + 1;
        int blen = len / 8;
        if (len % 8 > 0) {
            ++blen;
        }
        if (blen < (bits.length - SIZE_OF_BITINDEX)) {
            byte[] nbits = new byte[blen + SIZE_OF_BITINDEX];
            //byte[] nbits = new byte[NUMBEROFBYTES];
            //for (int i = 0; i < NUMBEROFBYTES; i++) {
            //    nbits[1] = (byte) 0;
            //}
            System.arraycopy(bits, 0, nbits, 0, blen);
            bits = nbits;
            encodeBitIndex();
        }
    }

    /**
     * Set the level id for the last level that has been written. The data array
     * will be resized automatically if the bit set is too small to encode the
     * id.
     *
     * @param levelId
     */
    protected void setCurrentLevelId(int levelId) {
        int units = getUnitsRequired(levelId);
        int numBits = bitWidth(units);
        if (units > 1) {
            levelId -= PER_COMPONENT_SIZE[units - 2];
        }
        for (int i = 1; i < units; i++) {
            setNextBit(true);
        }

        setNextBit(false);

        for (int i = numBits - 1; i >= 0; i--) {
            setNextBit(((levelId >>> i) & 1) != 0);
        }
    }

    /**
     * Returns the id starting at offset.
     *
     * @param startBit
     * @return the level id
     */
    public int getLevelId(int startBit) {
        int units = unitsUsed(startBit, bits);
        startBit += units;
        int numBits = bitWidth(units);
        //System.out.println("startBit: " + startBit + "; bitIndex: " + bitIndex
        //        + "; units: " + units + ": numBits: " + numBits + " " + toBitString()
        //        + "; bits: " + bits.length);
        int id = 0;
        for (int i = numBits - 1; i >= 0; i--) {
            if ((bits[startBit >> UNIT_SHIFT] & (1 << ((7 - startBit++) & 7))) != 0) {
                id |= 1 << i;
            }
        }
        if (units > 1) {
            id += PER_COMPONENT_SIZE[units - 2];
        }
        return id;
    }

    /**
     * Returns the number of units currently used to encode the id. The size of
     * a single unit is given by {@link #BITS_PER_UNIT}.
     *
     * @return the number of units
     */
    public int units() {
        return bitIndex + 1;
    }

    private static int unitsUsed(int startBit, byte[] bits) {
        int units = 1;
        while ((bits[startBit >> UNIT_SHIFT] & (1 << ((7 - startBit++) & 7))) != 0) {
            ++units;
        }
        return units;
    }

    @Override
    public int hashCode() {
        System.out.println("Curt: hashCode " + this + " " + this.debug());
        return bitIndex;
    }

    public boolean isLevelSeparator(int index) {
        return (bits[index >> UNIT_SHIFT] & (1 << ((7 - index) & 7))) == 0;
    }

    /**
     * Returns the number of level in this id, which corresponds to the depth at
     * which the node occurs within the node tree.
     *
     * @return the number of levels in this id
     */
    public int getLevelCount(int startOffset) {
        int bit = startOffset;
        int count = 0;
        while (bit > -1 && bit <= bitIndex) {
            int units = unitsUsed(bit, bits);
            bit += units;
            bit += bitWidth(units);
            if (bit < bitIndex) {
                if ((bits[bit >> UNIT_SHIFT] & (1 << ((7 - bit++) & 7))) == LEVEL_SEPARATOR) {
                    ++count;
                }
            } else {
                ++count;
            }
        }
        return count;
    }

    /**
     * Returns the number of sub-levels in the id starting at startOffset. This
     * is required to determine where a node can be inserted.
     *
     * @param startOffset
     * @return number of sub-levels
     */
    public int getSubLevelCount(int startOffset) {
        int bit = startOffset;
        int count = 0;
        while (bit > -1 && bit <= bitIndex) {
            int units = unitsUsed(bit, bits);
            bit += units;
            bit += bitWidth(units);
            if (bit < bitIndex) {
                ++count;
                if ((bits[bit >> UNIT_SHIFT] & (1 << ((7 - bit++) & 7))) == LEVEL_SEPARATOR) {
                    break;
                }
            } else {
                ++count;
            }
        }
        return count;
    }

    /**
     * Return all level ids converted to int.
     *
     * @return all level ids in this node id.
     */
    public int[] getLevelIds() {
        int count = getLevelCount(0);
        int[] ids = new int[count];
        int offset = 0;
        count = 0;
        while (offset <= bitIndex) {
            if (offset > 0) {
                if ((bits[offset >> UNIT_SHIFT] & (1 << ((7 - offset++) & 7))) == 0) {
                }
            }
            int id = getLevelId(offset);
            ids[count++] = id;
            offset += getUnitsRequired(id) * BITS_PER_UNIT;
        }
        /*
         for (int i = 0; i < count; i++) {
         ids[i] = getLevelId(getStartBitAtLevel(i));
         }
         */
        return ids;
    }

    /**
     * Return all level ids converted to int. Had a bug so rewrote it, probably
     * don't use this one.
     *
     * @return all level ids in this node id.
     */
    public int[] getLevelIds2() {
        int count = getLevelCount(0);
        int[] ids = new int[count];
        int offset = 0;
        for (int i = 0; i < count; i++) {
            ids[i] = getLevelId(offset);
            offset += getUnitsRequired(ids[i]) * BITS_PER_UNIT;
        }
        return ids;
    }

    /**
     * Find the last level in the id and return its offset.
     *
     * @return start-offset of the last level id.
     */
    public int lastLevelOffset() {
        int bit = 0;
        int lastOffset = 0;
        while (bit <= bitIndex) {
            // check if the next bit starts a new level or just a sub-level component
            if (bit > 0) {
                if ((bits[bit >> UNIT_SHIFT] & (1 << ((7 - bit) & 7))) == LEVEL_SEPARATOR) {
                    lastOffset = bit + 1;
                }
                ++bit;
            }
            int units = unitsUsed(bit, bits);
            bit += units;
            bit += bitWidth(units);
        }
        return lastOffset;
    }

    protected short lastFieldPosition() {
        short bit = 0;
        short lastOffset = 0;
        while (bit <= bitIndex) {
            if (bit > 0) {
                lastOffset = ++bit;
            }
            int units = unitsUsed(bit, bits);
            bit += units;
            bit += bitWidth(units);
        }
        return lastOffset;
    }

    /**
     * Set (or unset) the next bit in the current sequence of bits. The current
     * position is moved forward and the bit set is resized if necessary.
     *
     * @param value the value of the bit to set, i.e. 1 (true) or 0 (false)
     */
    private void setNextBit(boolean value) {
        ++bitIndex;
        encodeBitIndex();
        //System.out.println("Curt: this " + bits.length + " " + bits.length + " " + bitIndex);
        if ((bitIndex >> UNIT_SHIFT) >= (bits.length - SIZE_OF_BITINDEX)) {
            byte[] new_bits = new byte[bits.length + 1];
            System.arraycopy(bits, 0, new_bits, 0, bits.length - SIZE_OF_BITINDEX);
            bits = new_bits;
            encodeBitIndex();
        }
        if (value) {
            bits[bitIndex >> UNIT_SHIFT] |= 1 << ((7 - bitIndex) & 7);
        } else {
            bits[bitIndex >> UNIT_SHIFT] &= ~(1 << ((7 - bitIndex) & 7));
        }
    }

    /**
     * Calculates the number of bits available in a bit set that uses the given
     * number of units. These are the bits that can be actually used for the id,
     * not including the trailing address bits.
     *
     * @param units
     * @return number of bits available
     */
    protected static int bitWidth(int units) {
        return (units * BITS_PER_UNIT) - units;
    }

    /**
     * Calculates the minimum number of units that would be required to properly
     * encode the given integer.
     *
     * @param levelId the integer to encode in the level id
     * @return number of units required
     */
    protected static int getUnitsRequired(int levelId) {
        //if (PER_COMPONENT_SIZE == null) initComponents();
        //System.out.println("Per_Componenet size " + PER_COMPONENT_SIZE);
        for (int i = 0; i < PER_COMPONENT_SIZE.length; i++) {
            if (levelId < PER_COMPONENT_SIZE[i]) {
                return i + 1;
            }
        }
        // can't happen
        throw new IllegalStateException("Number of nodes exceeds the internal limit");
    }

    protected void compact() {
        int units = bitIndex + 1;
        int blen = units / 8;
        if (units % 8 > 0) {
            ++blen;
        }
        byte[] nbits = new byte[blen + SIZE_OF_BITINDEX];
        System.arraycopy(bits, 0, nbits, 0, blen);
        this.bits = nbits;
        encodeBitIndex();
    }

    public static int getLengthInBytes(int units, byte[] data, int startOffset) {
        return (int) Math.ceil(units / 8.0);
    }

    public boolean equals(OldDLN other) {
        if (bitIndex != other.bitIndex) {
            return false;
        }
        return Arrays.equals(bits, other.bits);
    }

//    public int compareTo(final DLN other) {
//        if (other == null)
//            return 1;
//        final int a1len = bits.length;
//        final int a2len = other.bits.length;
//
//        int limit = a1len <= a2len ? a1len : a2len;
//        byte[] obits = other.bits;
//        for (int i = 0; i < limit; i++) {
//            byte b1 = bits[i];
//            byte b2 = obits[i];
//            if (b1 != b2)
//                return (b1 & 0xFF) - (b2 & 0xFF);
//        }
//        return (a1len - a2len);
//    }
//
//    public int compareTo(Object obj) {
//        DLN other = (DLN) obj;
//        return compareTo(other);
//    }
    public int compareBits(OldDLN other, int bitCount) {
        int bytes = bitCount / 8;
        int remaining = bitCount % 8;
        for (int i = 0; i <= bytes; i++) {
            if (bits[i] != other.bits[i]) {
                //System.out.println("compare bits false " + i);
                return (bits[i] & 0xFF) - (other.bits[i] & 0xFF);
            }
        }
        //System.out.println("comparing bits  " + remaining);
        return ((bytes == 0) ? bits[bytes] : bits[bytes - 1] & BIT_MASK[remaining])
                - ((bytes == 0) ? bits[bytes] : other.bits[bytes - 1] & BIT_MASK[remaining]);
    }

    /**
     * Curtis added this. Checks to see if the current DLN is equal to the
     * previous up to the indicated number of bits. returns true/false. This
     * should be startsWith(other, bitCount).
     *
     * @param other
     */
    public boolean compareBitsExact(OldDLN other, int bitCount) {
        int bytes = bitCount / 8;
        int remaining = bitCount % 8;
        for (int i = 0; i < bytes; i++) {
            if (bits[i] != other.bits[i]) {
                //System.out.println("compare bits false " + i);
                return false;
            }
        }
        //System.out.println("comparing bits  " + remaining);
        if (remaining == 0) {
            return true;
        }
        return (bits[bytes] & BIT_MASK[remaining]) == (other.bits[bytes] & BIT_MASK[remaining]);
    }

    /**
     * Checks if the current DLN starts with the same bit sequence as other.
     * This is used to test ancestor-descendant relationships.
     *
     * @param other
     */
    public boolean startsWith(OldDLN other) {
        if (other.bitIndex > bitIndex) {
            return false;
        }
        int bytes = other.bitIndex / 8;
        int remaining = other.bitIndex % 8;
        for (int i = 0; i < bytes; i++) {
            if (bits[i] != other.bits[i]) {
                return false;
            }
        }
        return (bits[bytes] & BIT_MASK[remaining]) == (other.bits[bytes] & BIT_MASK[remaining]);
    }

    public String debug() {
        StringBuilder buf = new StringBuilder();
        //buf.append(toString());
        //buf.append(" = ");
        buf.append(toBitString());
        buf.append(" [");
        buf.append(bitIndex + 1);
        buf.append(']');
        return buf.toString();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        int offset = 0;
        while (offset <= bitIndex) {
            if (offset > 0) {
                if ((bits[offset >> UNIT_SHIFT] & (1 << ((7 - offset++) & 7))) == 0) {
                    buf.append('.');
                } else {
                    buf.append('/');
                }
            }
            //System.out.println("zzz " + bitIndex + " " + offset);
            if (offset > bitIndex) {
                return buf.toString();
            }
            int id = getLevelId(offset);
            buf.append(id);
            offset += getUnitsRequired(id) * BITS_PER_UNIT;
        }
        return buf.toString();
    }

    public String toBitString() {
        StringBuilder buf = new StringBuilder();
        int len = bits.length; // - SIZE_OF_BITINDEX;
        for (int i = 0; i < len; i++) {
            buf.append(toBitString(bits[i]));
        }
        return buf.toString();
    }
    private final static char[] digits = {'0', '1'};

    /**
     * Returns a string showing the bit representation of the given byte.
     *
     * @param b the byte to display
     * @return string representation
     */
    public final static String toBitString(byte b) {
        char[] buf = new char[8];
        int charPos = 8;
        int radix = 2;
        int mask = radix - 1;
        for (int i = 0; i < 8; i++) {
            buf[--charPos] = digits[b & mask];
            b >>>= 1;
        }
        return new String(buf);
    }

    /**
     * Returns a new DLN representing the first child node of this node.
     *
     * @return new child node id
     */
    public OldDLN newChild() {
        OldDLN child = new OldDLN(this);
        child.addLevelId(1, false);
        //System.out.println("Curt: newChild " + child);
        return child;
    }

    /**
     * Returns a new DLN representing the next following sibling of this node.
     *
     * @return new sibling node id.
     */
    public OldDLN nextSibling() {
        OldDLN sibling = new OldDLN(this);
        sibling.incrementLevelId();
        return sibling;
    }

    public OldDLN precedingSibling() {
        OldDLN sibling = new OldDLN(this);
        sibling.decrementLevelId();
        return sibling;
    }

    public OldDLN getChild(int child) {
        OldDLN nodeId = new OldDLN(this);
        nodeId.addLevelId(child, false);
        return nodeId;
    }

    public OldDLN insertNode(OldDLN right) {
        OldDLN rightNode = (OldDLN) right;
        if (right == null) {
            return nextSibling();
        }
        int lastLeft = lastLevelOffset();
        int lastRight = rightNode.lastLevelOffset();
        int lenLeft = getSubLevelCount(lastLeft);
        int lenRight = rightNode.getSubLevelCount(lastRight);
        OldDLN newNode;
        if (lenLeft > lenRight) {
            newNode = new OldDLN(this);
            newNode.incrementLevelId();
        } else if (lenLeft < lenRight) {
            newNode = (OldDLN) rightNode.insertBefore();
        } else {
            newNode = new OldDLN(this);
            newNode.addLevelId(1, true);
        }
        return newNode;
    }

    public OldDLN insertBefore() {
        short lastPos = lastFieldPosition();
        int lastId = getLevelId(lastPos);
        OldDLN newNode = new OldDLN(this);
//        System.out.println("insertBefore: " + newNode.toString() + " = " + newNode.bitIndex);
        if (lastId == 1) {
            newNode.setLevelId(lastPos, 0);
            newNode.addLevelId(35, true);
        } else {
            newNode.setLevelId(lastPos, lastId - 1);
            newNode.compact();
//            System.out.println("newNode: " + newNode.toString() + " = " + newNode.bitIndex + "; last = " + lastPos);
        }
        return newNode;
    }

    public OldDLN append(OldDLN otherId) {
        OldDLN other = (OldDLN) otherId;
        OldDLN newId = new OldDLN(this);
        int offset = 0;
        while (offset <= other.bitIndex) {
            boolean subLevel = false;
            if (offset > 0) {
                subLevel = ((other.bits[offset >> UNIT_SHIFT] & (1 << ((7 - offset++) & 7))) != 0);
            }
            int id = other.getLevelId(offset);
            newId.addLevelId(id, subLevel);
            offset += OldDLN.getUnitsRequired(id) * BITS_PER_UNIT;
        }
        return newId;
    }

    /**
     * Returns a new DLN representing the parent of the current node. If the
     * current node is the root element of the document, the method returns
     * {@link NodeId#DOCUMENT_NODE}. If the current node is the document node,
     * null is returned.
     *
     * @see NodeId#getParentId()
     */
    public OldDLN getParentId() {
        int last = lastLevelOffset();
        if (last == 0) {
            return ROOT_NODE;
        }
        return new OldDLN(bits, (short) (last - 1));
    }

    /**
     * Returns a new DLN representing the ancestor of the current node. If the
     * current node is the root element of the document, the method returns
     * {@link NodeId#DOCUMENT_NODE}. If the current node is the document node,
     * null is returned.
     *
     * @see NodeId#getParentId()
     */
    public OldDLN getAncestorId(int level) {
        int last = lastLevelOffset();
        if (last == 0) {
            return ROOT_NODE;
        }
        //System.out.println("getAncestorId " + last + " " + level + " " + getStartBitAtLevel(level));

        //return new DLN(bits, last - level);
        return new OldDLN(bits, (short) getStartBitAtLevel(level));
    }

    public boolean isDescendantOf(OldDLN other) {
        return startsWith(other) && bitIndex > other.bitIndex
                && isLevelSeparator(other.bitIndex + 1);
    }

    public boolean isDescendantOrSelfOf(OldDLN ancestor) {
        return startsWith(ancestor)
                && (bitIndex == ancestor.bitIndex || isLevelSeparator((ancestor).bitIndex + 1));
    }

    public boolean isChildOf(OldDLN other) {
        if (!startsWith(other)) {
            return false;
        }
        int levels = getLevelCount(other.bitIndex + 2);
        return levels == 1;
    }

    public int computeRelation(OldDLN other) {
        if (other == ROOT_NODE) {
            return getLevelCount(0) == 1 ? IS_CHILD : IS_DESCENDANT;
        }

        if (startsWith(other)) {
            if (bitIndex == other.bitIndex) {
                return IS_SELF;
            }
            if (bitIndex > other.bitIndex && isLevelSeparator(other.bitIndex + 1)) {
                if (getLevelCount(other.bitIndex + 2) == 1) {
                    return IS_CHILD;
                }
                return IS_DESCENDANT;
            }
        }
        return -1;
    }

    public boolean isSiblingOf(OldDLN sibling) {
        //DLN other = (DLN) sibling;
        OldDLN parent = getParentId();
        return sibling.isChildOf(parent);
    }

    /**
     * Returns the level within the document tree at which this node occurs.
     */
    public int getTreeLevel() {
        return getLevelCount(0);
    }

    public int compareTo(OldDLN otherId) {
        if (otherId == null) {
            return 1;
        }
        System.out.print("Comparing " + this + " to " + otherId);
        final OldDLN other = otherId;
        final int a1len = bits.length - SIZE_OF_BITINDEX;
        final int a2len = other.bits.length - SIZE_OF_BITINDEX;

        int limit = a1len <= a2len ? a1len : a2len;
        byte[] obits = other.bits;
        for (int i = 0; i < limit; i++) {
            byte b1 = bits[i];
            byte b2 = obits[i];
            //if (bits[i] == obits[i]) continue;
            if (b1 != b2) {
                //return (b1 & 0xFF) - (b2 & 0xFF);
                //System.out.println("Comparing " + this + " to " + otherId + " result is " + ((b1 < b2) ? -1 : 1));
                return (((b1 & 0xFF) - (b2 & 0xFF)) < 1) ? -1 : 1;
            }
        }
        //System.out.println("Comparing " + this + " to " + otherId + " results in " + ((a1len == a2len) ? 0 : (a1len < a2len) ? -1 : 1));
        return (a1len == a2len) ? 0 : (a1len < a2len) ? -1 : 1; //(a1len - a2len);
    }

    public boolean after(OldDLN other, boolean isFollowing) {
        if (compareTo(other) > 0) {
            if (isFollowing) {
                return !isDescendantOf(other);
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean before(OldDLN other, boolean isPreceding) {
        if (compareTo(other) < 0) {
            if (isPreceding) {
                return !other.isDescendantOf(this);
            } else {
                return true;
            }
        }
        return false;
    }

    /*
     private void writeObject(java.io.ObjectOutputStream out) throws IOException {
     //System.out.println("Curt: writing " + this);
     int i = this.bits.length;
     if (serialized == null) {
     serialized = new byte[i+2];
     System.arraycopy(this.bits, 0, this.serialized, 0, i);
     } else if (serialized.length != i+2) {
     serialized = new byte[i+2];
     System.arraycopy(this.bits, 0, this.serialized, 0, i);
     } else {
     // serialization already in place
     }
     // Put bitIndex in place
     serialized[i] = (byte) (bitIndex & 0xff);
     serialized[i+1] = (byte) ((bitIndex >>> 8) & 0xff);  
     System.arraycopy(serialized, 0, this.bits, 0, i-2);
     out.write(serialized);
     }

     private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
     //this.bits = new byte[NUMBEROFBYTES];
     this.serialized = (byte[]) in.readObject();
     int i = this.serialized.length;
     this.bitIndex = (short)(((serialized[i] << 8)) | ((serialized[i + 1] & 0xff)));
     this.bits = new byte[i-2];
     System.arraycopy(serialized, 0, this.bits, 0, i-2);
     //this.bitIndex = in.readInt();
     //System.out.println("Curt: reading " + this);
     }
     */
    public static void main(String[] args) {
        OldDLN id0 = new OldDLN("1.1.7");
        OldDLN id1 = new OldDLN("1.1.6");
        OldDLN id2 = new OldDLN("1.1.7.1");
        OldDLN id3 = new OldDLN("1.1.7/1");
        OldDLN id4 = (OldDLN) id0.newChild();
        OldDLN id5 = (OldDLN) id0.nextSibling();

        System.out.println(id4.toString());
        System.out.println(id5.toString());
        System.out.println(id0.debug());
        System.out.println(id1.debug());
        System.out.println(id2.debug());
        System.out.println(id0.toString() + " -> " + id1.toString() + ": " + id0.isSiblingOf(id1));
        System.out.println(id1.toString() + " -> " + id0.toString() + ": " + id1.isSiblingOf(id0));
        System.out.println(id2.toString() + " -> " + id0.toString() + ": " + id2.isSiblingOf(id0));
        System.out.println(id0.toString() + " -> " + id2.toString() + ": " + id0.isSiblingOf(id2));
        System.out.println(id3.toString() + " -> " + id0.toString() + ": " + id3.isSiblingOf(id0));
        System.out.println(id0.toString() + " -> " + id3.toString() + ": " + id0.isSiblingOf(id3));
    }
}
