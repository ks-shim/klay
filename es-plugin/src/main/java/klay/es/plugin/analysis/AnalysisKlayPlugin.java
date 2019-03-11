package klay.es.plugin.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.index.analysis.AnalyzerProvider;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.indices.analysis.AnalysisModule.AnalysisProvider;

import java.util.Collections;
import java.util.Map;

public class AnalysisKlayPlugin extends Plugin implements AnalysisPlugin {

    @Override
    public Map<String, AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        return Collections.singletonMap("klay_analyzer", KlayAnalyzerProvider::new);
    }
}
