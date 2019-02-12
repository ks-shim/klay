package da.klay.dictionary.user;

import da.klay.common.dictionary.structure.Trie;
import da.klay.common.pos.Pos;
import da.klay.dictionary.AbstractDictionary;
import da.klay.dictionary.DictionaryTextSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class UserDictionary extends AbstractDictionary {

    public UserDictionary(Path filePath) throws Exception {
        this(filePath, StandardCharsets.UTF_8);
    }

    public UserDictionary(Path filePath, Charset cs) throws Exception {
        super(new DictionaryTextSource(filePath, cs));
    }

    public Trie loadText(DictionaryTextSource source) throws Exception {

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
                    pos = Pos.NNP.name();
                } else {
                    word = line.substring(0, tabIndex);
                    pos = line.substring(tabIndex + 1);
                }

                trie.add(word, pos);
            }
        }

        return trie;
    }
}
