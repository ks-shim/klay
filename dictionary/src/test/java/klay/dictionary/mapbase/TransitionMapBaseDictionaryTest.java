package klay.dictionary.mapbase;

import klay.dictionary.param.DictionaryBinarySource;
import klay.dictionary.param.DictionaryBinaryTarget;
import klay.dictionary.param.DictionaryTextSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TransitionMapBaseDictionaryTest {

    static TransitionMapBaseDictionary tmbd;
    @Before
    public void before() throws Exception {
        Path path = Paths.get("src/test/resources/test.grammar.in");
        DictionaryTextSource source = new DictionaryTextSource(path, StandardCharsets.UTF_8);
        source.setPosFreqMap(new HashMap<>());
        source.setTransitionMap(new HashMap<>());
        tmbd = new TransitionMapBaseDictionary(
                source);
    }

    @Test
    public void loadText() throws Exception {

        Map<CharSequence, Double> resultMap = tmbd.getFully("XSV");
        Assert.assertEquals(resultMap.size(), 7);
    }

    @Test
    public void saveAndLoadBinary() throws Exception {
        Path path = Paths.get("src/test/resources/binary/test.transition.bin");
        tmbd.save(new DictionaryBinaryTarget(path));

        TransitionMapBaseDictionary newTmbd = new TransitionMapBaseDictionary(new DictionaryBinarySource(path));

        Map<CharSequence, Double> resultMap = newTmbd.getFully("XSV");
        System.out.println(resultMap);
        Assert.assertEquals(resultMap.size(), 7);
    }
}