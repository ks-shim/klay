package klay.dictionary.triebase.user;

import klay.common.dictionary.structure.Item;
import klay.common.dictionary.structure.ItemData;
import klay.common.dictionary.structure.ItemValueTrie;
import klay.common.dictionary.structure.Trie;
import klay.dictionary.param.DictionaryBinarySource;
import klay.dictionary.param.DictionaryTextSource;
import klay.dictionary.triebase.AbstractTrieBaseDictionary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FWDUserTrieBaseDictionary extends AbstractTrieBaseDictionary<Item[]> {

    public FWDUserTrieBaseDictionary(DictionaryTextSource source) throws Exception {
        super(source);
    }

    @Override
    protected Trie<Item[]> loadText(DictionaryTextSource source) throws Exception {

        Trie<Item[]> trie = new ItemValueTrie(true);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(source.getFilePath()), source.getCharSet()))) {

            List<ItemData> itemDataList = new ArrayList<>();
            String line = null;
            while((line = in.readLine()) != null) {
                line = line.trim();
                if(line.isEmpty() || line.startsWith("#")) continue;

                int tabIndex = line.indexOf('\t');
                if(tabIndex < 0 || tabIndex+1 >= line.length()) continue;

                String eojeol = line.substring(0, tabIndex);
                String answer = line.substring(tabIndex+1).replaceAll("\\s+"," ");
                itemDataList.clear();
                parseAnswer(answer, itemDataList);

                Item item = new Item(itemDataList);
                trie.addIfNotExist(eojeol, new Item[]{item});
            }
        }

        return trie;
    }

    private void parseAnswer(String answer,
                             List<ItemData> itemDataList) {
        String[] morphAndTags = answer.split("\\s+");
        for(String morphAndTag : morphAndTags) {
            String[] columns = morphAndTag.split("/");
            itemDataList.add(new ItemData(columns[0], columns[1]));
        }
    }

    @Override
    protected Trie loadText(DictionaryTextSource[] sources) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Trie loadBinary(DictionaryBinarySource source) throws Exception {
        throw new UnsupportedOperationException();
    }
}
