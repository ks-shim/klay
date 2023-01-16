package klay.dictionary.triebase.system;

import klay.common.dictionary.structure.*;
import klay.common.dictionary.structure.merger.ItemArrayCmdMerger;
import klay.common.parser.JasoParser;
import klay.dictionary.exception.DataFormatException;
import klay.dictionary.param.DictionaryBinarySource;
import klay.dictionary.param.DictionaryTextSource;
import klay.dictionary.triebase.AbstractTrieBaseDictionary;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.*;

public class EmissionTrieBaseDictionary extends AbstractTrieBaseDictionary<Item[]> {

    public EmissionTrieBaseDictionary(DictionaryTextSource source) throws Exception {
        super(source);
    }

    public EmissionTrieBaseDictionary(DictionaryTextSource[] sources) throws Exception {
        super(sources);
    }

    public EmissionTrieBaseDictionary(DictionaryBinarySource source) throws Exception {
        super(source);
    }

    @Override
    public void addWordAndPos(String word, String pos, double score) throws Exception {
        if(StringUtils.isBlank(word) || StringUtils.isBlank(pos)) return;

        CharSequence morph = JasoParser.parseAsString(word);
        Item item = new Item(1);
        item.setScore(score);
        item.addItemAt(0, new ItemData(word, pos));
        trie.add(morph, new Item[]{item});
    }

    @Override
    protected Trie<Item[]> loadText(DictionaryTextSource source) throws Exception {

        Trie<Item[]> trie = new ItemValueTrie(true);
        loadText(trie, source);
        return trie;
    }

    private void loadText(Trie<Item[]> trie,
                          DictionaryTextSource source) throws Exception {

        ItemArrayCmdMerger cmdMerger = new ItemArrayCmdMerger();

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(source.getFilePath()), source.getCharSet()))) {

            List<Item> itemList = new ArrayList<>();
            List<ItemData> itemDataList = new ArrayList<>();
            String line = null;
            while((line = in.readLine()) != null) {
                line = line.trim();
                if(line.isEmpty()) continue;

                int tabIndex = line.indexOf('\t');
                if(tabIndex < 0 || tabIndex+1 >= line.length()) continue;

                itemList.clear();
                CharSequence word = line.substring(0, tabIndex);
                CharSequence morph = JasoParser.parseAsString(word);
                Item[] data = (source.getDictionaryType() == DictionaryTextSource.DictionaryType.DIC_WORD) ?
                        validateAndReformForWord(line.substring(tabIndex+1), word, itemList, source.getPosFreqMap()) :
                        validateAndReformForIrregular(line.substring(tabIndex+1), itemList, itemDataList, trie, source.getTransitionMap());

                trie.append(morph, data, cmdMerger);
            }
        }
    }

    @Override
    protected Trie<Item[]> loadText(DictionaryTextSource[] sources) throws Exception {
        Trie<Item[]> trie = new ItemValueTrie(true);

        for(DictionaryTextSource source : sources)
            loadText(trie, source);

        return trie;
    }

    //*********************************************************************************************************
    // validateAndReformForWord method related ...
    //*********************************************************************************************************
    private Item[] validateAndReformForWord(String pos,
                                            CharSequence word,
                                            double score) throws DataFormatException {

        Item item = new Item(1);
        item.setScore(score);
        item.addItemAt(0, new ItemData(word, pos));
        return new Item[]{item};
    }

    private Item[] validateAndReformForWord(String data,
                                            CharSequence word,
                                            List<Item> itemList,
                                            Map<CharSequence, Integer> posFreqMap) throws DataFormatException {

        String[] poses = data.split("\t");
        for(int i=0; i<poses.length; i++) {
            String[] values = poses[i].trim().split(":");

            if(values.length != 2 || values[0].isEmpty() || values[1].isEmpty()) throw new DataFormatException();
            String pos = values[0].toUpperCase();
            double score = changeProbability(pos, values[1], posFreqMap);

            Item item = new Item(1);
            item.setScore(score);
            itemList.add(item);
            item.addItemAt(0, new ItemData(word, pos));
        }

        return itemList.toArray(new Item[itemList.size()]);
    }

    private double changeProbability(String pos,
                                     String freqStr,
                                     Map<CharSequence, Integer> posFreqMap) {
        Integer freq = Integer.parseInt(freqStr);
        Integer totalFreq = posFreqMap.get(pos);
        return Math.log10((double) freq / (double) totalFreq);
    }

    //*********************************************************************************************************
    // validateAndReformForIrregular method related ...
    //*********************************************************************************************************
    private Item[] validateAndReformForIrregular(String data,
                                                 List<Item> itemList,
                                                 List<ItemData> itemDataList,
                                                 Trie<Item[]> trie,
                                                 Map<CharSequence, Map<CharSequence, Double>> transitionMap) throws DataFormatException {

        // 앞당기/VV 어야/EC:1
        String[] poses = data.split("\t");
        for(int i=0; i<poses.length; i++) {
            String[] values = poses[i].trim().split(":");

            if(values.length != 2 || values[0].isEmpty() || values[1].isEmpty()) throw new DataFormatException();

            String pos = values[0];
            Integer freq = Integer.parseInt(values[1]);

            //******************************************
            // ** skip rule, but I don't know why...
            // ** This is from KOMORAN.
            //******************************************
            if(poses.length > 1 && freq < 2) continue;

            itemDataList.clear();
            double score = scoreForIrregular(pos, itemDataList, trie, transitionMap);

            Item item = new Item(itemDataList);
            itemList.add(item);
        }

        return itemList.toArray(new Item[itemList.size()]);
    }

    private double scoreForIrregular(String data,
                                   List<ItemData> itemDataList,
                                   Trie<Item[]> trie,
                                   Map<CharSequence, Map<CharSequence, Double>> transitionMap) {
        double score = 0;

        String prePos = null;
        String[] morphAndTags = data.split(" ");
        for(int i=0; i<morphAndTags.length; i++) {
            String morphAndTag = morphAndTags[i];
            String[] columns = morphAndTag.split("/");
            String word = columns[0];
            CharSequence morphJaso = JasoParser.parseAsString(word);
            String pos = columns[1];

            itemDataList.add(new ItemData(word, pos));

            Item[] searchedItems = trie.getFully(morphJaso);
            Map<String, Double> posScoreMap = parseWordDicResult(searchedItems);
            Double tmpScore = posScoreMap.get(pos);
            if(tmpScore != null) {
                // plus Emission score ...
                score += tmpScore;
            }

            if(prePos != null) {
                Map<CharSequence, Double> map = transitionMap.get(prePos);
                Double transitionScore = (map == null) ? 0.0 : map.get(pos);
                if(transitionScore != null) score += transitionScore;
            }

            prePos = pos;
        }

        return score;
    }

    private Map<String, Double> parseWordDicResult(Item[] items) {
        Map<String, Double> map = new HashMap<>();
        if(items == null) return map;

        for(Item item : items) {
            ItemData data = item.getLast();
            if(data == null) continue;

            map.put(data.getPos().toString(), item.getScore());
        }

        return map;
    }

    @Override
    protected Trie<Item[]> loadBinary(DictionaryBinarySource source) throws Exception {
        return TrieLoadSaveHelper.load(source.getFilePath(), TrieDataType.ITEM);
    }
}
