/*
 *  HDLNFactory constructs new HDLNs
 *  @author Amani Shatnawi
 */
package usu.dln.old;

import usu.NodeIdFactory;
import usu.PathIdFactory;
import usu.dln.old.ItemDLN;

/**
 * Implementation of {@link NodeIdFactory} for DLN-based node ids.
 */
public class ItemDLNFactory implements NodeIdFactory, PathIdFactory {

    @Override
    public ItemDLN createInstance(int id) {
        return new ItemDLN(id);
    }

    @Override
    public ItemDLN createFromData(byte[] data, int startOffset) {
        return new ItemDLN(data, (short) startOffset);
    }

    @Override
    public ItemDLN createFromString(String string) {
        return new ItemDLN(string);
    }

    @Override
    public ItemDLN rootId() {
        return ItemDLN.ROOT_NODE;
    }

    @Override
    public ItemDLN minId() {
        return ItemDLN.MIN_NODE;
    }

    @Override
    public ItemDLN maxId() {
        return ItemDLN.MAX_NODE;
    }

    @Override
    public int lengthInBytes(int units, byte[] data, int startOffset) {
        return ItemDLN.getLengthInBytes(units, data, startOffset);
    }

}
