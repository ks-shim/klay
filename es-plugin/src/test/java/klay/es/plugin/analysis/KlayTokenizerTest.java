package klay.es.plugin.analysis;

import klay.core.Klay;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Paths;

class KlayTokenizerTest {

    //@Test
    void test() throws Exception {
        String[] lines = {
                "(‘14.8월)의 일환으로 ’15.3.3 상반기중(행복서울대학교 등 활용)",
                "ㄱㅐOOO같은영화 뭐가무섭다는건지ㅡㅡ"
        };

        Klay klay = new Klay(Paths.get("src/test/resources/configuration/klay.conf"));
        KlayTokenizer tokenizer = new KlayTokenizer(klay);

        for(String line : lines) {
            Reader reader = new StringReader(line);
            tokenizer.setReader(reader);
            tokenizer.reset();

            CharTermAttribute termAtt = tokenizer.getAttribute(CharTermAttribute.class);
            OffsetAttribute offsetAtt = tokenizer.getAttribute(OffsetAttribute.class);
            while (tokenizer.incrementToken()) {
                System.out.println(termAtt.toString() + " : " + offsetAtt.startOffset() + " ~ " + offsetAtt.endOffset());
            }
            tokenizer.close();
        }
    }
}