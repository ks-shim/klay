package klay.common.dictionary.structure;

import lombok.Data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Data
public class ItemData {

    private CharSequence word;
    private CharSequence pos;

    public ItemData(CharSequence word,
                    CharSequence pos) {
        this.word = word;
        this.pos = pos;
    }

    public void store(DataOutput os) throws IOException {
        os.writeUTF(word.toString());
        os.writeUTF(pos.toString());
    }

    public static ItemData read(DataInput is) throws IOException {
        return new ItemData(is.readUTF(), is.readUTF());
    }
}
