package da.klay.dictionary.triebase.system;

import com.google.common.base.Stopwatch;
import da.klay.common.dictionary.structure.TrieLoadSaveHelper;
import da.klay.common.dictionary.structure.TrieResult;
import da.klay.common.parser.JasoParser;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EmissionTrieBaseDictionaryTest {

    static EmissionTrieBaseDictionary etd;
    @BeforeAll
    static void before() throws Exception {
        Path path = Paths.get("src/test/resources/test.dic.word");
        etd = new EmissionTrieBaseDictionary(new DictionaryTextSource(path, StandardCharsets.UTF_8));
    }

    @Test
    void get() {
        // 1. getFully test
        CharSequence key = JasoParser.parseAsString("대구일보");
        CharSequence result = etd.getFully(key);

        assertNotNull(result);

        // 2. partially matching test
        key = JasoParser.parseAsString("대구일보구로구");
        TrieResult trieResult = etd.getLastOnPath(key);

        assertEquals(true, trieResult.hasResult());
        assertEquals(8, trieResult.getEndPosition());

        trieResult = etd.getLastOnPath(key, trieResult.getEndPosition()+1);

        assertEquals(true, trieResult.hasResult());
        assertEquals(9, trieResult.getStartPosition());
        assertEquals(14, trieResult.getEndPosition());

        // 3. all matching test
        key = JasoParser.parseAsString("대구일보기자");
        TrieResult[] results = etd.getAll(key);

        assertEquals(2, results.length);
        for(TrieResult res : results)
            assertEquals(true, res.hasResult());
    }

    @Test
    void saveAndLoadBinary() throws Exception {

        Path filePath = Paths.get("src/test/resources/binary/test.emission.bin");
        if(Files.notExists(filePath)) etd.save(new DictionaryBinaryTarget(filePath));

        EmissionTrieBaseDictionary newETD = new EmissionTrieBaseDictionary(new DictionaryBinarySource(filePath));

        // 1. getFully test
        CharSequence key = JasoParser.parseAsString("대구일보");
        CharSequence result = newETD.getFully(key);

        assertNotNull(result);
    }
}