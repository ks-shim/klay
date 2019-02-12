package da.klay.dictionary.user;

import da.klay.common.pos.Pos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FWDUserDictionaryTest {

    static FWDUserDictionary fud;
    @BeforeAll
    static void before() throws Exception {
        Path path = Paths.get("src/test/resources/test.fwd.user");
        fud = new FWDUserDictionary(path);
    }

    @Test
    void getFully() {
        CharSequence cs = fud.getFully("흘렸어요");
        assertEquals("흘리/VV 었/EP 어요/EC", cs);
    }
}