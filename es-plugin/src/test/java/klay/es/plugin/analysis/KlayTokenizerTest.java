package klay.es.plugin.analysis;

import klay.core.Klay;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Paths;

public class KlayTokenizerTest {

    @Test
    public void test() throws Exception {
        String[] lines = {
                "심강섭과 하임준 입니다.",
                "박기림입니다.", "심강섭 입니다..", "하임준입니다."
        };

        Klay klay = new Klay(Paths.get("src/test/resources/configuration/klay.conf"));
        KlayTokenizer tokenizer = new KlayTokenizer(klay);

        for(String line : lines) {
            Reader reader = new StringReader(line);
            tokenizer.setReader(reader);
            tokenizer.reset();

            CharTermAttribute termAtt = tokenizer.getAttribute(CharTermAttribute.class);
            OffsetAttribute offsetAtt = tokenizer.getAttribute(OffsetAttribute.class);
            PositionIncrementAttribute posIncrAtt = tokenizer.getAttribute(PositionIncrementAttribute.class);
            while (tokenizer.incrementToken()) {
                System.out.println(
                        termAtt.toString() + " : " + offsetAtt.startOffset() + " ~ " + offsetAtt.endOffset()
                                + " :: " + posIncrAtt.getPositionIncrement());
            }
            tokenizer.close();
        }
    }
}