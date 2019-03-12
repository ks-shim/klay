package klay.es.plugin.analysis;

import klay.core.Klay;
import klay.core.morphology.analysis.Morphs;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;

import java.nio.file.Path;

public class KlayAnalyzer extends Analyzer {

    private final Klay klay;
    public KlayAnalyzer(Path configFilePath) {
        try {
            klay = new Klay(configFilePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        KlayTokenizer tokenizer = new KlayTokenizer(klay);
        return new TokenStreamComponents(tokenizer, new LowerCaseFilter(tokenizer));
    }

    @Override
    protected TokenStream normalize(String fieldName, TokenStream in) {
        return new LowerCaseFilter(in);
    }
}
