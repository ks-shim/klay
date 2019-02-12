package da.klay.dictionary.user;

import da.klay.common.pos.Pos;
import da.klay.dictionary.user.UserDictionary;
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
    void getFully() {
        CharSequence cs = ud.getFully("자연어");
        assertEquals(Pos.NNG.name(), cs);
    }
}