package klay.es.plugin.analysis;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

import java.io.IOException;

public class KlayFilter extends TokenFilter {

    protected KlayFilter(TokenStream input) {
        super(input);
    }

    @Override
    public boolean incrementToken() throws IOException {
        return false;
    }
}
