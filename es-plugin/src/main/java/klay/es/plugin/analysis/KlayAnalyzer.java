package klay.es.plugin.analysis;

import klay.common.pos.Pos;
import klay.core.Klay;
import klay.core.morphology.analysis.Morphs;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.elasticsearch.SpecialPermission;

import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

public class KlayAnalyzer extends Analyzer {

    private final static CharArraySet ENGLISH_STOPWORD_SET;
    private final static CharArraySet DEFAULT_ALLOWED_POS_SET;

    static {
        CharArraySet stopSet = new CharArraySet(Arrays.asList(
                "a", "an", "and", "are", "as", "at", "be", "but", "by",
                "for", "if", "in", "into", "is", "it",
                "no", "not", "of", "on", "or", "such",
                "that", "the", "their", "then", "there", "these",
                "they", "this", "to", "was", "will", "with"
        ), false);
        ENGLISH_STOPWORD_SET = CharArraySet.unmodifiableSet(stopSet);

        DEFAULT_ALLOWED_POS_SET = CharArraySet.unmodifiableSet(
                new CharArraySet(
                        Arrays.asList(
                                new String[]{
                                        Pos.NNG.label(), Pos.NNP.label(), Pos.VV.label(), Pos.VA.label(),
                                        Pos.SL.label(), Pos.SH.label(), Pos.NA.label()}),
                        true));
    }

    private final boolean usePosFilter;
    private final CharArraySet allowedPosSet;
    private final Klay klay;

    private KlayAnalyzer(Properties config,
                         boolean usePosFilter,
                         CharArraySet allowedPosSet) {
        try {
            this.klay = new Klay(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.usePosFilter = usePosFilter;
        this.allowedPosSet = (allowedPosSet == null || allowedPosSet.isEmpty()) ? DEFAULT_ALLOWED_POS_SET : allowedPosSet;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        KlayTokenizer tokenizer = new KlayTokenizer(klay);
        TokenStream filter = new LowerCaseFilter(tokenizer);
        filter = new StopFilter(filter, ENGLISH_STOPWORD_SET);
        if(usePosFilter) filter = new KlayPosFilter(filter, allowedPosSet);

        return new TokenStreamComponents(tokenizer, filter);
    }

    public static class Builder {

        private Properties config;
        private boolean usePosFilter;
        private CharArraySet allowedPosSet;

        public Builder config(Properties config) {
            this.config = config;
            return this;
        }

        public Builder usePosFilter(boolean enable) {
            this.usePosFilter = enable;
            return this;
        }

        public Builder setAllowedPoses(List<String> allowedPoses) {
            if(allowedPoses == null) allowedPoses = new ArrayList<>();
            this.allowedPosSet = CharArraySet.unmodifiableSet(new CharArraySet(allowedPoses, true));
            return this;
        }

        public KlayAnalyzer build() {
            SecurityManager sm = System.getSecurityManager();
            if(sm != null) sm.checkPermission(new SpecialPermission());

            return AccessController.doPrivileged(
                    new PrivilegedAction<KlayAnalyzer>() {
                        @Override
                        public KlayAnalyzer run() {
                            return new KlayAnalyzer(config, usePosFilter, allowedPosSet);
                        }
                    }
            );
        }
    }
}
