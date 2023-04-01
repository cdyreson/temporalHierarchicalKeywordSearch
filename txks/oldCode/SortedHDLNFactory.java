/*
 *  SortedHDLNFactory constructs new SortedHDLNs
 *  @author Curtis
 */
package usu.history;

import usu.NodeIdFactory;
import usu.PathIdFactory;

/**
 * Implementation of {@link NodeIdFactory} for DLN-based node ids.
 */
public class SortedHDLNFactory implements NodeIdFactory, PathIdFactory {

    @Override
    public SortedHDLN createInstance(int id) {
        return new SortedHDLN(id);
    }

    @Override
    public SortedHDLN createFromData(byte[] data, int startOffset) {
        return new SortedHDLN(data, (short) startOffset);
    }

    @Override
    public SortedHDLN createFromString(String string) {
        return new SortedHDLN(string);
    }

    @Override
    public SortedHDLN rootId() {
        return SortedHDLN.SORTED_ROOT_NODE;
    }

    @Override
    public SortedHDLN minId() {
        return SortedHDLN.SORTED_MIN_NODE;
    }

    @Override
    public HDLN maxId() {
        return SortedHDLN.SORTED_MAX_NODE;
    }

    @Override
    public int lengthInBytes(int units, byte[] data, int startOffset) {
        return SortedHDLN.getLengthInBytes(units, data, startOffset);
    }
}
