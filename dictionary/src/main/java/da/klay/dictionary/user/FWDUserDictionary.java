package da.klay.dictionary.user;

import da.klay.common.dictionary.structure.Trie;
import da.klay.dictionary.AbstractDictionary;
import da.klay.dictionary.param.DictionaryTextSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FWDUserDictionary extends AbstractDictionary {

    public FWDUserDictionary(Path filePath) throws Exception {
        this(filePath, StandardCharsets.UTF_8);
    }

    public FWDUserDictionary(Path filePath, Charset cs) throws Exception {
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

                int tabIndex = line.indexOf('\t');
                if(tabIndex < 0 || tabIndex+1 >= line.length()) continue;

                String eojeol = line.substring(0, tabIndex);
                String answer = line.substring(tabIndex+1).replaceAll("\\s+"," ");

                trie.add(eojeol, answer);
            }
        }

        return trie;
    }
}
