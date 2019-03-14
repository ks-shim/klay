package klay.dictionary.triebase.user;

import klay.common.pos.Pos;
import klay.dictionary.param.DictionaryTextSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;


public class UserTrieBaseDictionaryTest {

    static UserTrieBaseDictionary ud;
    @Before
    public void before() throws Exception {
        Path path = Paths.get("src/test/resources/test.dic.user");
        ud = new UserTrieBaseDictionary(new DictionaryTextSource(path, StandardCharsets.UTF_8));
    }

    @Test
    public void getFully() {
        CharSequence cs = ud.getFully("자연어");
        Assert.assertEquals(Pos.NNG.label(), cs);
    }
}