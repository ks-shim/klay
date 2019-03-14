package klay.es.plugin.analysis;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;

public final class KlayPosFilter extends TokenFilter {

    private final CharArraySet allowedPosSet;
    public KlayPosFilter(TokenStream input, CharArraySet allowedPosSet) {
        super(input);
        this.allowedPosSet = allowedPosSet;
    }

    private final TypeAttribute typeAtt = getAttribute(TypeAttribute.class);

    @Override
    public boolean incrementToken() throws IOException {

        while(input.incrementToken()) {

            String pos = typeAtt.type();
            if(!allowedPosSet.contains(pos)) continue;

            return true;
        }

        return false;
    }
}
