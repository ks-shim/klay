package klay.es.plugin.analysis;

import klay.core.Klay;
import klay.core.morphology.analysis.Morph;
import klay.core.morphology.analysis.Morphs;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.util.RollingCharBuffer;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.AttributeFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public final class KlayTokenizer extends Tokenizer {

    private final Klay klay;

    private Iterator<Morph> morphIterator;

    private final RollingCharBuffer buffer = new RollingCharBuffer();

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

    private int finalOffset = 0;

    public KlayTokenizer(Klay klay) {
        this.klay = klay;
    }

    @Override
    public boolean incrementToken() throws IOException {
        if(morphIterator == null) {
            try {
                boolean readSome = readNewData();
                if (!readSome) return false;
            } catch (AssertionError e) {
                return false;
            }
        }

        if(morphIterator.hasNext()) {
            Morph morph = morphIterator.next();
            termAtt.setEmpty().append(morph.getText());
            offsetAtt.setOffset(correctOffset(morph.getStartOffset()), finalOffset = correctOffset(morph.getEndOffset()+1));
            posIncrAtt.setPositionIncrement(1);
            typeAtt.setType(morph.getPos().toString());
            return true;
        }

        return false;
    }

    private boolean readNewData() throws IOException {
        int ch;
        StringBuilder sb = new StringBuilder();
        int count = 0;
        while((ch = buffer.get(count++)) > 0) {
            sb.append((char) ch);
            buffer.freeBefore(count);
        }

        if(sb.length() == 0) return false;
        Morphs morphs = klay.doKlay(sb.toString());
        morphIterator = morphs.iterator();
        return true;
    }

    @Override
    public void end() throws IOException {
        super.end();
        offsetAtt.setOffset(finalOffset, finalOffset);
    }

    @Override
    public void close() throws IOException {
        super.close();
        buffer.reset(input);
        morphIterator = null;
        finalOffset = 0;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        buffer.reset(input);
        morphIterator = null;
        finalOffset = 0;
    }
}
