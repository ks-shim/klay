package klay.dictionary.triebase.user;

import klay.dictionary.param.DictionaryTextSource;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FWDUserTrieBaseDictionaryTest {

    static FWDUserTrieBaseDictionary fud;
    @Before
    public void before() throws Exception {
        Path path = Paths.get("src/test/resources/test.fwd.user");
        fud = new FWDUserTrieBaseDictionary(new DictionaryTextSource(path, StandardCharsets.UTF_8));
    }

    @Test
    public void getFully() {
        /*CharSequence cs = fud.getFully("흘렸어요", 0, 4);
        assertEquals("흘리/VV 었/EP 어요/EC", cs);*/
    }
}