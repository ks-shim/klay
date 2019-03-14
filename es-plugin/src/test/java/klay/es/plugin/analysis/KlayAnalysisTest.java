package klay.es.plugin.analysis;

import org.apache.lucene.analysis.util.TokenizerFactory;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.test.ESTestCase;
import org.junit.jupiter.api.Test;

public class KlayAnalysisTest extends ESTestCase {

    void test() throws Exception {

        TestAnalysis analysis = createTestAnalysis();
    }

    private static TestAnalysis createTestAnalysis() throws Exception {
        return createTestAnalysis(
                new Index("test", "_na_"), Settings.EMPTY, new AnalysisKlayPlugin());
    }
}
