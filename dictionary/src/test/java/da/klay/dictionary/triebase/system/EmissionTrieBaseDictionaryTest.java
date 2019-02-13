package da.klay.dictionary.triebase.system;

import da.klay.dictionary.param.DictionaryTextSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class EmissionTrieBaseDictionaryTest {

    static EmissionTrieBaseDictionary etd;
    @BeforeAll
    static void before() throws Exception {
        Path path = Paths.get("src/test/resources/test.dic.word");
        etd = new EmissionTrieBaseDictionary(new DictionaryTextSource(path, StandardCharsets.UTF_8));
    }

    @Test
    void loadText() {
    }

    @Test
    void loadBinary() {
    }
}