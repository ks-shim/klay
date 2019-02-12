package da.klay.dictionary.triebase.user;

import da.klay.common.pos.Pos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class UserTrieBaseDictionaryTest {

    static UserTrieBaseDictionary ud;
    @BeforeAll
    static void before() throws Exception {
        Path path = Paths.get("src/test/resources/test.dic.user");
        ud = new UserTrieBaseDictionary(path);
    }

    @Test
    void getFully() {
        CharSequence cs = ud.getFully("자연어");
        assertEquals(Pos.NNG.label(), cs);
    }
}