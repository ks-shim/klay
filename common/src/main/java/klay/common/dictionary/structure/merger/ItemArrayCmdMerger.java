package klay.common.dictionary.structure.merger;

import klay.common.dictionary.structure.Item;

import java.util.LinkedList;
import java.util.List;

public class ItemArrayCmdMerger implements CmdMerger<Item[]> {

    @Override
    public Item[] merge(Item[] preCmd, Item[] curCmd) {
        List<Item> itemList = new LinkedList<>();
        for(Item item : preCmd) {
            itemList.add(item);
        }
        for(Item item : curCmd) {
            itemList.add(item);
        }
        return itemList.toArray(new Item[itemList.size()]);
    }
}
