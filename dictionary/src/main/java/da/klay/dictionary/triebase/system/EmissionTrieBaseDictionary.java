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
import java.nio.file.Files;

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
                CharSequence data = validateAndReform(line.substring(tabIndex+1), word, reformSb) ;
                trie.add(morph, data);
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

    private CharSequence validateAndReform(String data, CharSequence word, StringBuilder reformSb) throws DataFormatException {

        String[] poses = data.split("\t");
        for(int i=0; i<poses.length; i++) {
            String[] values = poses[i].trim().split(":");

            if(values.length != 2 || values[0].isEmpty() || values[1].isEmpty()) throw new DataFormatException();
        }

        if(data.indexOf('/') >= 0) return data;

        for(int i=0; i<poses.length; i++) {
            if(reformSb.length() != 0) reformSb.append('\t');

            reformSb.append(word).append('/').append(poses[i].trim());
        }
        return reformSb.toString();
    }

    @Override
    protected Trie loadBinary(DictionaryBinarySource source) throws Exception {
        return TrieLoadSaveHelper.load(source.getFilePath());
    }
}
