package klay.common.dictionary.structure;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StringValueTrie extends Trie<CharSequence> {

    public StringValueTrie(boolean forward) {
        super(forward);
    }

    public StringValueTrie(DataInput is) throws IOException {
        forward = is.readBoolean();
        root = is.readInt();
        for (int i = is.readInt(); i > 0; i--) {
            cmds.add(is.readUTF());
        }
        for (int i = is.readInt(); i > 0; i--) {
            rows.add(new Row(is));
        }
    }

    public void store(DataOutput os) throws IOException {
        os.writeBoolean(forward);
        os.writeInt(root);
        os.writeInt(cmds.size());
        for (CharSequence cmd : cmds)
            os.writeUTF(cmd.toString());

        os.writeInt(rows.size());
        for (Row row : rows)
            row.store(os);
    }

    public void add(CharSequence key, CharSequence cmd, boolean addIfNotExist) {
        if (key == null || cmd == null || cmd.length() == 0) return;

        super.add(key, cmd, addIfNotExist);
    }
}
