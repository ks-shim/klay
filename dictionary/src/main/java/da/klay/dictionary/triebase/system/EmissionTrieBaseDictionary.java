package da.klay.dictionary.triebase.system;

import com.google.common.base.Joiner;
import da.klay.common.dictionary.structure.Trie;
import da.klay.common.dictionary.structure.TrieLoadSaveHelper;
import da.klay.common.parser.JasoParser;
import da.klay.common.pos.Pos;
import da.klay.dictionary.exception.DataFormatException;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;
import da.klay.dictionary.triebase.AbstractTrieBaseDictionary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class EmissionTrieBaseDictionary extends AbstractTrieBaseDictionary {

    public EmissionTrieBaseDictionary(DictionaryTextSource source) throws Exception {
        super(source);
    }

    @Override
    protected Trie loadText(DictionaryTextSource source) throws Exception {

        Trie trie = new Trie(true);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(source.getFilePath()), source.getCharSet()))) {

            String line = null;
            while((line = in.readLine()) != null) {
                line = line.trim();
                if(line.isEmpty()) continue;

                int tabIndex = line.indexOf('\t');
                if(tabIndex < 0 || tabIndex+1 >= line.length()) continue;

                String morph = JasoParser.parseAsString(line.substring(0, tabIndex));
                String data = validate(line.substring(tabIndex+1)) ;

                trie.add(morph, data);
            }
        }

        return trie;
    }

    private String validate(String data) throws DataFormatException {

        String[] poses = data.split("\t");
        for(int i=0; i<poses.length; i++) {
            String[] values = poses[i].trim().split(":");

            if(values.length != 2 || values[0].isEmpty() || values[1].isEmpty()) throw new DataFormatException();
        }
        return data;
    }

    @Override
    protected Trie loadBinary(DictionaryBinarySource source) throws Exception {
        return TrieLoadSaveHelper.load(source.getFilePath());
    }
}
