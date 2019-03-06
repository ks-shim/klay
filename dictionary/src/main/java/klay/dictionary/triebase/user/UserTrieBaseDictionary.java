package klay.dictionary.triebase.user;

import klay.common.dictionary.structure.Trie;
import klay.common.pos.Pos;
import klay.dictionary.param.DictionaryBinarySource;
import klay.dictionary.param.DictionaryTextSource;
import klay.dictionary.triebase.AbstractTrieBaseDictionary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;

public class UserTrieBaseDictionary extends AbstractTrieBaseDictionary {

    public UserTrieBaseDictionary(DictionaryTextSource source) throws Exception {
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
                if(line.isEmpty() || line.startsWith("#")) continue;

                String word;
                String pos;
                int tabIndex = line.lastIndexOf('\t');
                if(tabIndex < 0) {
                    word = line;
                    pos = Pos.NNP.label();
                } else {
                    word = line.substring(0, tabIndex);
                    pos = line.substring(tabIndex + 1);
                }

                trie.add(word, pos);
            }
        }

        return trie;
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
