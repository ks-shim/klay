package klay.es.plugin.analysis;

import org.apache.lucene.analysis.Analyzer;

public class KlayAnalyzer extends Analyzer {

    public KlayAnalyzer() {}

    @Override
    protected TokenStreamComponents createComponents(String s) {
        return null;
    }
}
