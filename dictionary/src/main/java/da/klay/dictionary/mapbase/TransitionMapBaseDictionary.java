package da.klay.dictionary.mapbase;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import da.klay.common.pos.Pos;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TransitionMapBaseDictionary extends AbstractMapBaseDictionary {

    public TransitionMapBaseDictionary(DictionaryTextSource source) throws Exception {
        super(source);
    }

    public TransitionMapBaseDictionary(DictionaryBinarySource source) throws Exception {
        super(source);
    }

    @Override
    protected Map<CharSequence, Map<CharSequence, Double>> loadText(DictionaryTextSource source) throws Exception {

        Map<CharSequence, Integer> posFreqMap = source.getPosFreqMap();
        Map<CharSequence, Map<CharSequence, Double>> map = new HashMap<>();

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(source.getFilePath()), source.getCharSet()))) {

            String line = null;
            while((line = in.readLine()) != null) {
                line = line.trim();
                if(line.isEmpty()) continue;

                int tabIndex = line.lastIndexOf('\t');
                if(tabIndex < 0 || tabIndex+1 >= line.length()) continue;

                String prePos = line.substring(0, tabIndex);
                int prePosFreq = posFreqMap.get(prePos);
                String data = line.substring(tabIndex+1).replaceAll("\\s+", "");

                String[] nextPoses = data.split(",");
                if(nextPoses.length == 0) continue;

                Map<CharSequence, Double> nextPosMap = new HashMap<>();
                for(int i=0; i<nextPoses.length; i++) {
                    String transitionData = nextPoses[i];
                    int colonIndex = transitionData.indexOf(':');
                    if(colonIndex < 0 || colonIndex+1 >= transitionData.length()) continue;
                    String nextPos = transitionData.substring(0, colonIndex);
                    int nextPosFreq = posFreqMap.get(nextPos);
                    Integer freq = Integer.parseInt(transitionData.substring(colonIndex+1));

                    if(Pos.NNP.label().equals(nextPos)) {
                        prePosFreq += 100000;
                        nextPosFreq += 100000;
                    }

                    nextPosMap.put(nextPos, Math.log10(nextPosFreq/(double)prePosFreq));
                }
                map.put(prePos, nextPosMap);
            }
        }

        return Collections.unmodifiableMap(map);
    }

    @Override
    protected Map<CharSequence, Map<CharSequence, Double>> loadBinary(DictionaryBinarySource source) throws Exception {

        Map<CharSequence, Map<CharSequence, Double>> map;

        Kryo kryo = new Kryo();
        try (Input in = new Input(Files.newInputStream(source.getFilePath()))) {
            map = kryo.readObject(in, HashMap.class);
        }

        return Collections.unmodifiableMap(map);
    }
}
