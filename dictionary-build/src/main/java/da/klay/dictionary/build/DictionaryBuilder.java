package da.klay.dictionary.build;

import da.klay.dictionary.mapbase.TransitionMapBaseDictionary;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;
import da.klay.dictionary.triebase.system.EmissionTrieBaseDictionary;

import java.nio.file.Paths;

public class DictionaryBuilder {

    private DictionaryTextSource[] emissionSources;
    private DictionaryBinaryTarget emissionTarget;

    private DictionaryTextSource transitionSource;
    private DictionaryBinaryTarget transitionTarget;

    private DictionaryBuilder(DictionaryTextSource[] emissionSources,
                              DictionaryBinaryTarget emissionTarget,
                              DictionaryTextSource transitionSource,
                              DictionaryBinaryTarget transitionTarget) {
        this.emissionSources = emissionSources;
        this.emissionTarget = emissionTarget;
        this.transitionSource = transitionSource;
        this.transitionTarget = transitionTarget;
    }

    public void buildAll() throws Exception {
        buildEmissionDictionary(emissionSources, emissionTarget);
        buildTransitionDictionary(transitionSource, transitionTarget);
    }

    private void buildEmissionDictionary(DictionaryTextSource[] sources,
                                         DictionaryBinaryTarget target) throws Exception {
        EmissionTrieBaseDictionary dictionary = new EmissionTrieBaseDictionary(sources);
        dictionary.save(target);
    }

    private void buildTransitionDictionary(DictionaryTextSource source,
                                           DictionaryBinaryTarget target) throws Exception {
        TransitionMapBaseDictionary dictionary = new TransitionMapBaseDictionary(source);
        dictionary.save(target);
    }

    public static class Builder {

        private DictionaryTextSource[] emissionSources;
        private DictionaryBinaryTarget emissionTarget;

        private DictionaryTextSource transitionSource;
        private DictionaryBinaryTarget transitionTarget;

        public Builder emissionSourcesAndTarget(DictionaryTextSource[] sources,
                                                DictionaryBinaryTarget target) {
            this.emissionSources = sources;
            this.emissionTarget = target;
            return this;
        }

        public Builder transitionSourceAndTarget(DictionaryTextSource source,
                                                 DictionaryBinaryTarget target) {
            this.transitionSource = source;
            this.transitionTarget = target;
            return this;
        }

        public DictionaryBuilder build() {
            return new DictionaryBuilder(
                    emissionSources, emissionTarget,
                    transitionSource, transitionTarget);
        }

    }

    public static void main(String[] args) throws Exception {

        // 1. emission related variables.
        DictionaryTextSource[] emissionSources = {
                new DictionaryTextSource(Paths.get("data/dictionary/text/system/dic.word")),
                new DictionaryTextSource(Paths.get("data/dictionary/text/system/dic.irregular"))
        };
        DictionaryBinaryTarget emissionTarget = new DictionaryBinaryTarget(Paths.get("data/dictionary/binary/system/emission.bin"));

        // 2. transition related variables.
        DictionaryTextSource transitionSource = new DictionaryTextSource(Paths.get("data/dictionary/text/system/grammar.in"));
        DictionaryBinaryTarget transitionTarget = new DictionaryBinaryTarget(Paths.get("data/dictionary/binary/system/transition.bin"));

        DictionaryBuilder builder = new DictionaryBuilder.Builder()
                .emissionSourcesAndTarget(emissionSources, emissionTarget)
                .transitionSourceAndTarget(transitionSource, transitionTarget)
                .build();

        builder.buildAll();
    }
}
