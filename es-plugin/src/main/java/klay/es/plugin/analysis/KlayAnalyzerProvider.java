package klay.es.plugin.analysis;

import org.apache.lucene.analysis.CharArraySet;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;

import java.io.File;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class KlayAnalyzerProvider extends AbstractIndexAnalyzerProvider<KlayAnalyzer> {

    private final KlayAnalyzer analyzer;

    @Inject
    public KlayAnalyzerProvider(IndexSettings indexSettings, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(indexSettings, name, settings);

        Properties config = new Properties();
        config.put("dictionary.emission.path", "plugins/analysis-klay/dictionary/binary/system/emission.bin");
        config.put("dictionary.transition.path", "plugins/analysis-klay/dictionary/binary/system/transition.bin");
        config.put("dictionary.user.path", "plugins/analysis-klay/dictionary/text/user/dic.user");
        config.put("dictionary.fwd.path", "plugins/analysis-klay/dictionary/text/user/fwd.user");
        config.put("tokenization.token.length_limit", "-1");

        boolean usePosFilter = settings.getAsBoolean("usePosFilter", true);
        List<String> allowedPoses = settings.getAsList("allowedPoses");
        this.analyzer = new KlayAnalyzer.Builder()
                .config(config)
                .usePosFilter(usePosFilter)
                .setAllowedPoses(allowedPoses)
                .build();
    }

    @Override
    public KlayAnalyzer get() {
        return analyzer;
    }
}
