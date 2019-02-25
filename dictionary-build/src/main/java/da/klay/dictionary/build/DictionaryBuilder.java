package da.klay.dictionary.build;

import da.klay.dictionary.mapbase.TransitionMapBaseDictionary;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;
import da.klay.dictionary.triebase.system.EmissionTrieBaseDictionary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DictionaryBuilder {

    private DictionaryTextSource posFreqSource;

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
            if(!source.isUsePosFreq()) continue;
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
                new DictionaryTextSource(Paths.get("data/dictionary/text/system/dic.irregular")),
                new DictionaryTextSource(Paths.get("data/dictionary/text/system/dic.word"), true)
        };
        DictionaryBinaryTarget emissionTarget = new DictionaryBinaryTarget(Paths.get("data/dictionary/binary/system/emission.bin"));

        // 2. transition related variables.
        DictionaryTextSource transitionSource = new DictionaryTextSource(Paths.get("data/dictionary/text/system/grammar.in"), true);
        DictionaryBinaryTarget transitionTarget = new DictionaryBinaryTarget(Paths.get("data/dictionary/binary/system/transition.bin"));

        DictionaryBuilder builder = new DictionaryBuilder.Builder()
                .emissionSourcesAndTarget(emissionSources, emissionTarget)
                .transitionSourceAndTarget(transitionSource, transitionTarget)
                .build();

        builder.buildAll();
    }
}
