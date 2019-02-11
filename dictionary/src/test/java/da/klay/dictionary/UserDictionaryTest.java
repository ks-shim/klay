package da.klay.dictionary;

import da.klay.common.pos.Pos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class UserDictionaryTest {

    static UserDictionary ud;
    @BeforeAll
    static void before() throws Exception {
        Path path = Paths.get("src/test/resources/test.dic.user");
        ud = new UserDictionary(path);
    }

    @Test
    void lookup() {
        CharSequence cs = ud.lookup("자연어");
        assertEquals(Pos.NNG.name(), cs);
    }
}