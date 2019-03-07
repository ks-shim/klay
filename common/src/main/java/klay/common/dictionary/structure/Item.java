package klay.common.dictionary.structure;

import lombok.Data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Data
public class Item implements Iterable<ItemData> {

    private final ItemData[] itemDatas;
    private double score;

    public Item(int nItemDatas) {
        itemDatas = new ItemData[nItemDatas];
    }

    public Item(List<ItemData> itemDataList) {
        itemDatas = itemDataList.toArray(new ItemData[itemDataList.size()]);
    }

    public int size() {
        return itemDatas.length;
    }

    public void addItemAt(int index, ItemData itemData) {
        this.itemDatas[index] = itemData;
    }

    public ItemData getItemAt(int index) {
        return itemDatas[index];
    }

    public ItemData getLast() {
        return (itemDatas == null || itemDatas.length == 0) ? null : itemDatas[itemDatas.length - 1];
    }

    public void store(DataOutput os) throws IOException {
        os.writeInt(itemDatas.length);
        for(int i=0; i<itemDatas.length; i++) {
            ItemData data = itemDatas[i];
            data.store(os);
        }
        os.writeDouble(score);
    }

    public static Item read(DataInput is) throws IOException {
        int nData = is.readInt();
        Item item = new Item(nData);
        for(int i=0; i<nData; i++) {
            item.addItemAt(i, ItemData.read(is));
        }
        item.score = is.readDouble();
        return item;
    }

    @Override
    public Iterator<ItemData> iterator() {
        return Arrays.asList(itemDatas).iterator();
    }
}
