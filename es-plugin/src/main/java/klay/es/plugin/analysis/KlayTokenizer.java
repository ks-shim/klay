package klay.es.plugin.analysis;

import klay.core.morphology.analysis.Morph;
import klay.core.morphology.analysis.Morphs;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.util.AttributeFactory;

import java.io.IOException;
import java.util.Iterator;

public class KlayTokenizer extends Tokenizer {

    private Iterator<Morph> morphIterator;

    public KlayTokenizer() {}

    public KlayTokenizer(AttributeFactory factory) {
        super(factory);
    }

    @Override
    public boolean incrementToken() throws IOException {
        if(morphIterator.hasNext()) {
            Morph morph = morphIterator.next();
        }

        return false;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        morphIterator = null;
    }
}
