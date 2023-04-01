/*
 *  HDLNFactory constructs new HDLNs
 *  @author Curtis
 */
package usu.dln;

import usu.dln.HistoryDLN;
import usu.NodeIdFactory;
import usu.PathIdFactory;

/**
 * Implementation of {@link NodeIdFactory} for DLN-based node ids.
 */
public class HistoryDLNFactory implements NodeIdFactory, PathIdFactory {

    @Override
    public HistoryDLN createInstance(int id) {
        return new HistoryDLN(id);
    }

    @Override
    public HistoryDLN createFromData(byte[] data, int startOffset) {
        return new HistoryDLN(data, (short) startOffset);
    }

    @Override
    public HistoryDLN createFromString(String string) {
        return new HistoryDLN(string);
    }

    @Override
    public HistoryDLN rootId() {
        return HistoryDLN.ROOT_NODE;
    }

    @Override
    public HistoryDLN minId() {
        return HistoryDLN.MIN_NODE;
    }

    @Override
    public HistoryDLN maxId() {
        return HistoryDLN.MAX_NODE;
    }

    @Override
    public int lengthInBytes(int units, byte[] data, int startOffset) {
        return HistoryDLN.getLengthInBytes(units, data, startOffset);
    }

}
