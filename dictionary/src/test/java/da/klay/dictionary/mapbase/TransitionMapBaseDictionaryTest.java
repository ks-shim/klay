package da.klay.dictionary.mapbase;

import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransitionMapBaseDictionaryTest {

    static TransitionMapBaseDictionary tmbd;
    @BeforeAll
    static void before() throws Exception {
        Path path = Paths.get("src/test/resources/test.grammar.in");
        tmbd = new TransitionMapBaseDictionary(
                new DictionaryTextSource(path, StandardCharsets.UTF_8));
    }

    @Test
    void loadText() throws Exception {

        Map<CharSequence, Integer> resultMap = tmbd.getFully("XSV");
        assertEquals(resultMap.size(), 7);
    }

    @Test
    void saveAndLoadBinary() throws Exception {
        Path path = Paths.get("src/test/resources/binary/test.transition.bin");
        tmbd.save(new DictionaryBinaryTarget(path));

        TransitionMapBaseDictionary newTmbd = new TransitionMapBaseDictionary(new DictionaryBinarySource(path));

        Map<CharSequence, Integer> resultMap = newTmbd.getFully("XSV");
        System.out.println(resultMap);
        assertEquals(resultMap.size(), 7);
    }
}