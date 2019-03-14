package klay.common.parser;



import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class JasoParserTest {

    @Test
    public void parse() {
        String text = "KLAY입니다.";
        List<Character> jasoList = JasoParser.parseAsList(text);

        Assert.assertArrayEquals(
                new Character[]{'K', 'L', 'A', 'Y','ㅇ', 'ㅣ', 'ㅂ', 'ㄴ', 'ㅣ', 'ㄷ', 'ㅏ', '.'},
                jasoList.toArray(new Character[jasoList.size()]));
    }
}