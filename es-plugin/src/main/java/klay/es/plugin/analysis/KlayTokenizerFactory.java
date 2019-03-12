package klay.es.plugin.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import java.util.Map;

public class KlayTokenizerFactory extends TokenizerFactory {

    public KlayTokenizerFactory(Map<String, String> args) {
        super(args);
    }

    @Override
    public Tokenizer create(AttributeFactory factory) {
        return new KlayTokenizer(factory);
    }
}
