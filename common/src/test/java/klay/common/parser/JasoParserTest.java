package klay.common.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class JasoParserTest {

    @Test
    void parse() {
        String text = "KLAY입니다.";
        List<Character> jasoList = JasoParser.parseAsList(text);

        assertArrayEquals(
                new Character[]{'K', 'L', 'A', 'Y','ㅇ', 'ㅣ', 'ㅂ', 'ㄴ', 'ㅣ', 'ㄷ', 'ㅏ', '.'},
                jasoList.toArray(new Character[jasoList.size()]));
    }
}