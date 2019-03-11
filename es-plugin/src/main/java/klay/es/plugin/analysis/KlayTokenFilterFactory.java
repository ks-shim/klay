package klay.es.plugin.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;

public class KlayTokenFilterFactory extends AbstractTokenFilterFactory {

    @Inject
    public KlayTokenFilterFactory(IndexSettings indexSettings, String name, @Assisted Settings settings) {
        super(indexSettings, name, settings);
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new KlayFilter(tokenStream);
    }

    @Override
    public boolean breaksFastVectorHighlighter() {
        return false;
    }
}
