package da.klay.dictionary.triebase.system;

import da.klay.common.dictionary.structure.Trie;
import da.klay.common.dictionary.structure.TrieLoadSaveHelper;
import da.klay.common.parser.JasoParser;
import da.klay.dictionary.exception.DataFormatException;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryTextSource;
import da.klay.dictionary.triebase.AbstractTrieBaseDictionary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.DoubleBuffer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class EmissionTrieBaseDictionary extends AbstractTrieBaseDictionary {

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
    protected Trie loadText(DictionaryTextSource source) throws Exception {

        Trie trie = new Trie(true);
        loadText(trie, source);
        return trie;
    }

    private void loadText(Trie trie,
                          DictionaryTextSource source) throws Exception {

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(source.getFilePath()), source.getCharSet()))) {

            StringBuilder reformSb = new StringBuilder();
            String line = null;
            while((line = in.readLine()) != null) {
                line = line.trim();
                if(line.isEmpty()) continue;

                int tabIndex = line.indexOf('\t');
                if(tabIndex < 0 || tabIndex+1 >= line.length()) continue;

                reformSb.setLength(0);
                CharSequence word = line.substring(0, tabIndex);
                CharSequence morph = JasoParser.parseAsString(word);
                CharSequence data = (source.getDictionaryType() == DictionaryTextSource.DictionaryType.DIC_WORD) ?
                        validateAndReformForWord(line.substring(tabIndex+1), word, reformSb, source.getPosFreqMap()) :
                        validateAndReformForIrregular(line.substring(tabIndex+1), reformSb, trie, source.getTransitionMap());
                trie.addIfNotExist(morph, data);
            }
        }
    }

    @Override
    protected Trie loadText(DictionaryTextSource[] sources) throws Exception {
        Trie trie = new Trie(true);

        for(DictionaryTextSource source : sources)
            loadText(trie, source);

        return trie;
    }

    //*********************************************************************************************************
    // validateAndReformForWord method related ...
    //*********************************************************************************************************
    private CharSequence validateAndReformForWord(String data,
                                                  CharSequence word,
                                                  StringBuilder reformSb,
                                                  Map<CharSequence, Integer> posFreqMap) throws DataFormatException {

        String[] poses = data.split("\t");
        for(int i=0; i<poses.length; i++) {
            String[] values = poses[i].trim().split(":");

            if(values.length != 2 || values[0].isEmpty() || values[1].isEmpty()) throw new DataFormatException();

            changeProbability(poses, i, values, posFreqMap);
        }

        for(int i=0; i<poses.length; i++) {
            if(reformSb.length() != 0) reformSb.append('\t');

            reformSb.append(word).append('/').append(poses[i].trim());
        }
        return reformSb.toString();
    }

    private void changeProbability(String[] poses, int index,
                                   String[] values,
                                   Map<CharSequence, Integer> posFreqMap) {
        String pos = values[0];
        Integer freq = Integer.parseInt(values[1]);
        Integer totalFreq = posFreqMap.get(pos);
        double score = Math.log10((double)freq/(double)totalFreq);
        poses[index] = pos + ':' + score;
    }

    //*********************************************************************************************************
    // validateAndReformForIrregular method related ...
    //*********************************************************************************************************
    private CharSequence validateAndReformForIrregular(String data,
                                                       StringBuilder reformSb,
                                                       Trie trie,
                                                       Map<CharSequence, Map<CharSequence, Double>> transitionMap) throws DataFormatException {

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

            double score = scoreForIrregular(pos, freq, trie, transitionMap);
            poses[i] = pos + ':' + score;

            if(reformSb.length() != 0) reformSb.append('\t');
            reformSb.append(pos).append(':').append(score);
        }

        return reformSb.toString();
    }

    private double scoreForIrregular(String data,
                                     int freq,
                                     Trie trie,
                                     Map<CharSequence, Map<CharSequence, Double>> transitionMap) {
        double score = 0;

        String prePos = null;
        String[] morphAndTags = data.split(" ");
        for(int i=0; i<morphAndTags.length; i++) {
            String morphAndTag = morphAndTags[i];
            String[] columns = morphAndTag.split("/");
            CharSequence morphJaso = JasoParser.parseAsString(columns[0]);
            String pos = columns[1];

            CharSequence dicResult = trie.getFully(morphJaso);
            if(dicResult == null) dicResult = "";

            Map<String, Double> posScoreMap = parseWordDicResult((String)dicResult);
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

    private Map<String, Double> parseWordDicResult(String dicResult) {
        Map<String, Double> map = new HashMap<>();
        if(dicResult.isEmpty()) return map;

        String[] datas = dicResult.split("\t");
        for(String data : datas) {
            int slashIndex = data.lastIndexOf('/');
            int colonIndex = data.lastIndexOf(':');
            String pos = data.substring(slashIndex+1, colonIndex);
            double score = Double.parseDouble(data.substring(colonIndex+1));
            map.put(pos, score);
        }
        return map;
    }

    @Override
    protected Trie loadBinary(DictionaryBinarySource source) throws Exception {
        return TrieLoadSaveHelper.load(source.getFilePath());
    }
}
