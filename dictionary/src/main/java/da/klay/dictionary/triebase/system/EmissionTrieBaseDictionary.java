package da.klay.dictionary.triebase.system;

import da.klay.common.dictionary.structure.Trie;
import da.klay.common.pos.Pos;
import da.klay.dictionary.param.DictionaryBinarySource;
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
    public Trie loadText(DictionaryTextSource source) throws Exception {

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

                String morph;
                String data;
                //trie.add(word, pos);
            }
        }

        return trie;
    }

    //private void vali
    @Override
    public Trie loadBinary(DictionaryBinarySource source) throws Exception {
        return super.loadBinary(source);
    }
}
