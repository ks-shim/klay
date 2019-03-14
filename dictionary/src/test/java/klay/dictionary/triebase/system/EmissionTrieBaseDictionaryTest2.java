package klay.dictionary.triebase.system;

import klay.common.parser.JasoParser;
import klay.dictionary.param.DictionaryBinarySource;
import klay.dictionary.param.DictionaryBinaryTarget;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EmissionTrieBaseDictionaryTest2 {

    static EmissionTrieBaseDictionary etd;
    @Before
    public void before() throws Exception {
        /*Path path1 = Paths.get("src/test/resources/test.dic.word");
        Path path2 = Paths.get("src/test/resources/test.dic.irregular");
        etd = new EmissionTrieBaseDictionary(
                new DictionaryTextSource[]{
                        new DictionaryTextSource(path1, StandardCharsets.UTF_8),
                        new DictionaryTextSource(path2, StandardCharsets.UTF_8)});*/
    }

    @Test
    public void get() {
        // 1. getFully test
        /*CharSequence key = JasoParser.parseAsString("대구일보");
        CharSequence result = etd.getFully(key);

        assertNotNull(result);

        // 2. partially matching test
        key = JasoParser.parseAsString("대구일보구로구");
        TrieResult trieResult = etd.getLastOnPath(key);

        assertEquals(true, trieResult.hasResult());
        assertEquals(9, trieResult.getEndPosition());

        trieResult = etd.getLastOnPath(key, trieResult.getEndPosition());

        assertEquals(true, trieResult.hasResult());
        assertEquals(9, trieResult.getStartPosition());
        assertEquals(15, trieResult.getEndPosition());

        // 3. all matching test
        key = JasoParser.parseAsString("대구일보기자");
        TrieResult[] results = etd.getAll(key);

        assertEquals(2, results.length);
        for(TrieResult res : results)
            assertEquals(true, res.hasResult());

        key = JasoParser.parseAsString("하였거나");
        result = etd.getFully(key);

        assertEquals(true, result != null);*/
    }

    @Test
    public void saveAndLoadBinary() throws Exception {

        /*Path filePath = Paths.get("src/test/resources/binary/test.emission2.bin");
        if(Files.notExists(filePath)) etd.save(new DictionaryBinaryTarget(filePath));

        EmissionTrieBaseDictionary newETD = new EmissionTrieBaseDictionary(new DictionaryBinarySource(filePath));

        // 1. getFully test
        CharSequence key = JasoParser.parseAsString("대구일보");
        CharSequence result = newETD.getFully(key);

        assertNotNull(result);*/
    }
}
