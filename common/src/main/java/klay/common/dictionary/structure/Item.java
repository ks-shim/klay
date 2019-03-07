package klay.common.dictionary.structure;

import lombok.Data;

@Data
public class Item {

    private CharSequence word;
    private CharSequence pos;

    public Item(CharSequence word,
                CharSequence pos) {
        this.word = word;
        this.pos = pos;
    }
}
