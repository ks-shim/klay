package da.klay.dictionary.build;

import da.klay.dictionary.mapbase.TransitionMapBaseDictionary;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;
import da.klay.dictionary.triebase.system.EmissionTrieBaseDictionary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DictionaryBuilder {

    private DictionaryTextSource posFreqSource;

    private DictionaryTextSource[] emissionSources;
    private DictionaryBinaryTarget emissionTarget;

    private DictionaryTextSource transitionSource;
    private DictionaryBinaryTarget transitionTarget;

    private DictionaryBuilder(DictionaryTextSource posFreqSource,
                              DictionaryTextSource[] emissionSources,
                              DictionaryBinaryTarget emissionTarget,
                              DictionaryTextSource transitionSource,
                              DictionaryBinaryTarget transitionTarget) {

        this.posFreqSource = posFreqSource;
        this.emissionSources = emissionSources;
        this.emissionTarget = emissionTarget;
        this.transitionSource = transitionSource;
        this.transitionTarget = transitionTarget;
    }

    public void buildAll() throws Exception {
        Map<CharSequence, Integer> posFreqMap = buildPosFrequencyMap();
        buildEmissionDictionary(posFreqMap);
        buildTransitionDictionary(posFreqMap);
    }

    private Map<CharSequence, Integer> buildPosFrequencyMap() throws Exception {

        Map<String, Integer> posFreqMap = new HashMap<>();

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(posFreqSource.getFilePath()), posFreqSource.getCharSet()))) {

            String line = null;
            while((line = in.readLine()) != null) {
                line = line.trim();
                if(line.isEmpty()) continue;

                int tabIndex = line.lastIndexOf('\t');
                if(tabIndex < 0 || tabIndex+1 >= line.length()) continue;

                String data = line.substring(tabIndex+1).replaceAll("\\s+", "");

                String[] nextPoses = data.split(",");
                if(nextPoses.length == 0) continue;

                for(int i=0; i<nextPoses.length; i++) {
                    String transitionData = nextPoses[i];
                    int colonIndex = transitionData.indexOf(':');
                    if(colonIndex < 0 || colonIndex+1 >= transitionData.length()) continue;
                    String nextPos = transitionData.substring(0, colonIndex);
                    Integer frequency = Integer.parseInt(transitionData.substring(colonIndex+1));

                    Integer sumFreq = posFreqMap.get(nextPos);
                    if(sumFreq == null) sumFreq = 0;

                    posFreqMap.put(nextPos, sumFreq + frequency);
                }
            }
        }

        return Collections.unmodifiableMap(posFreqMap);
    }

    private void buildEmissionDictionary(Map<CharSequence, Integer> posFreqMap) throws Exception {
        for(DictionaryTextSource source : emissionSources) {
            if(DictionaryTextSource.DictionaryType.DIC_WORD != source.getDictionaryType()) continue;
            source.setPosFreqMap(posFreqMap);
        }

        EmissionTrieBaseDictionary dictionary = new EmissionTrieBaseDictionary(emissionSources);
        dictionary.save(emissionTarget);
    }

    private void buildTransitionDictionary(Map<CharSequence, Integer> posFreqMap) throws Exception {
        transitionSource.setPosFreqMap(posFreqMap);

        TransitionMapBaseDictionary dictionary = new TransitionMapBaseDictionary(transitionSource);
        dictionary.save(transitionTarget);
    }

    public static class Builder {

        private DictionaryTextSource posFreqSource;

        private DictionaryTextSource[] emissionSources;
        private DictionaryBinaryTarget emissionTarget;

        private DictionaryTextSource transitionSource;
        private DictionaryBinaryTarget transitionTarget;

        public Builder posFreqSource(DictionaryTextSource posFreqSource) {
            this.posFreqSource = posFreqSource;
            return this;
        }

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
                    posFreqSource,
                    emissionSources, emissionTarget,
                    transitionSource, transitionTarget);
        }

    }

    public static void main(String[] args) throws Exception {

        // 1. configuration
        Properties config = new Properties();
        config.load(Files.newInputStream(Paths.get("data/configuration/klay.conf")));

        // 2. pos frequency related.
        DictionaryTextSource posFreqSource = new DictionaryTextSource(Paths.get(config.getProperty("dictionary.grammar.path")));

        // 3. emission related variables.
        DictionaryTextSource[] emissionSources = {
                new DictionaryTextSource(
                        Paths.get(config.getProperty("dictionary.irregular.path")), DictionaryTextSource.DictionaryType.DIC_IRREGULAR),
                new DictionaryTextSource(
                        Paths.get(config.getProperty("dictionary.word.path")), DictionaryTextSource.DictionaryType.DIC_WORD)
        };
        DictionaryBinaryTarget emissionTarget =
                new DictionaryBinaryTarget(Paths.get(config.getProperty("dictionary.emission.path")));

        // 4. transition related variables.
        DictionaryTextSource transitionSource =
                new DictionaryTextSource(
                        Paths.get(config.getProperty("dictionary.grammar.path")), DictionaryTextSource.DictionaryType.GRAMMAR);
        DictionaryBinaryTarget transitionTarget =
                new DictionaryBinaryTarget(Paths.get(config.getProperty("dictionary.transition.path")));

        DictionaryBuilder builder = new DictionaryBuilder.Builder()
                .posFreqSource(posFreqSource)
                .emissionSourcesAndTarget(emissionSources, emissionTarget)
                .transitionSourceAndTarget(transitionSource, transitionTarget)
                .build();

        builder.buildAll();
    }
}
