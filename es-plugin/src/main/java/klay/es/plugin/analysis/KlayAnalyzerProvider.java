package klay.es.plugin.analysis;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;

import java.nio.file.Paths;

public class KlayAnalyzerProvider extends AbstractIndexAnalyzerProvider<KlayAnalyzer> {

    private final KlayAnalyzer analyzer;

    @Inject
    public KlayAnalyzerProvider(IndexSettings indexSettings, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(indexSettings, name, settings);
        this.analyzer = new KlayAnalyzer(Paths.get("./klay.conf"));
    }

    @Override
    public KlayAnalyzer get() {
        return analyzer;
    }
}
