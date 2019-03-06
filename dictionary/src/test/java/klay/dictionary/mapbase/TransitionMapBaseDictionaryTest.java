package klay.dictionary.mapbase;

import klay.dictionary.param.DictionaryBinarySource;
import klay.dictionary.param.DictionaryBinaryTarget;
import klay.dictionary.param.DictionaryTextSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransitionMapBaseDictionaryTest {

    static TransitionMapBaseDictionary tmbd;
    @BeforeAll
    static void before() throws Exception {
        Path path = Paths.get("src/test/resources/test.grammar.in");
        DictionaryTextSource source = new DictionaryTextSource(path, StandardCharsets.UTF_8);
        source.setPosFreqMap(new HashMap<>());
        source.setTransitionMap(new HashMap<>());
        tmbd = new TransitionMapBaseDictionary(
                source);
    }

    @Test
    void loadText() throws Exception {

        Map<CharSequence, Double> resultMap = tmbd.getFully("XSV");
        assertEquals(resultMap.size(), 7);
    }

    @Test
    void saveAndLoadBinary() throws Exception {
        Path path = Paths.get("src/test/resources/binary/test.transition.bin");
        tmbd.save(new DictionaryBinaryTarget(path));

        TransitionMapBaseDictionary newTmbd = new TransitionMapBaseDictionary(new DictionaryBinarySource(path));

        Map<CharSequence, Double> resultMap = newTmbd.getFully("XSV");
        System.out.println(resultMap);
        assertEquals(resultMap.size(), 7);
    }
}