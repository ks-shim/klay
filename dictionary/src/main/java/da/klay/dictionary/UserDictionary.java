package da.klay.dictionary;

import da.klay.common.dictionary.structure.Trie;
import da.klay.common.pos.Pos;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class UserDictionary {

    private final Trie trie;

    public UserDictionary(Path filePath) throws Exception {
        this.trie = load(filePath);
    }

    private Trie load(Path filePath) throws Exception {

        Trie trie = new Trie(true);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(Files.newInputStream(filePath), StandardCharsets.UTF_8))) {
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

    public CharSequence lookup(CharSequence cs) {
        return trie.getFully(cs);
    }
}
