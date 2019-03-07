package klay.common.dictionary.structure;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ItemValueTrie extends Trie<Item[]> {

    public ItemValueTrie(boolean forward) {
        super(forward);
    }

    public ItemValueTrie(DataInput is) throws IOException {
        forward = is.readBoolean();
        root = is.readInt();

        int cmdListSize = is.readInt();
        for (int i = cmdListSize; i > 0; i--) {
            int itemSize = is.readInt();
            Item[] items = new Item[itemSize];
            for(int j=0; j<itemSize; j++) {
                items[j] = Item.read(is);
            }
            cmds.add(items);
        }

        for (int i = is.readInt(); i > 0; i--) {
            rows.add(new Row(is));
        }
    }

    public void store(DataOutput os) throws IOException {
        os.writeBoolean(forward);
        os.writeInt(root);

        os.writeInt(cmds.size());
        for (Item[] cmd : cmds) {
            os.writeInt(cmd.length);
            for(Item item : cmd)
                item.store(os);
        }

        os.writeInt(rows.size());
        for (Row row : rows)
            row.store(os);
    }

    public void add(CharSequence key, Item[] cmd, boolean addIfNotExist) {
        if (key == null || cmd == null || cmd.length == 0) return;

        super.add(key, cmd, addIfNotExist);
    }
}
