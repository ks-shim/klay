package klay.dictionary.mapbase;

import klay.common.pos.Pos;
import klay.dictionary.param.DictionaryBinarySource;
import klay.dictionary.param.DictionaryTextSource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
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

    public Map<CharSequence, Map<CharSequence, Double>> getTransitionMap() {
        return map;
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
                Integer prePosFreq = posFreqMap.get(prePos);
                if(prePosFreq == null) prePosFreq = 1;
                String data = line.substring(tabIndex+1).replaceAll("\\s+", "");

                String[] nextPoses = data.split(",");
                if(nextPoses.length == 0) continue;

                Map<CharSequence, Double> nextPosMap = new HashMap<>();
                for(int i=0; i<nextPoses.length; i++) {
                    String transitionData = nextPoses[i];
                    int colonIndex = transitionData.indexOf(':');
                    if(colonIndex < 0 || colonIndex+1 >= transitionData.length()) continue;
                    String nextPos = transitionData.substring(0, colonIndex);
                    Integer nextPosFreq = Integer.parseInt(transitionData.substring(colonIndex+1));

                    int tempPrePosFreq = prePosFreq;
                    if(Pos.NNP.label().equals(nextPos)) {
                        tempPrePosFreq += 100000;
                        nextPosFreq += 100000;
                    }

                    //System.out.println(prePos + " -> " + nextPos + " : " + tempPrePosFreq + " : " + nextPosFreq);
                    nextPosMap.put(nextPos, Math.log10(nextPosFreq/(double)tempPrePosFreq));
                }
                map.put(prePos, nextPosMap);
            }
        }

        return Collections.unmodifiableMap(map);
    }

    @Override
    protected Map<CharSequence, Map<CharSequence, Double>> loadBinary(DictionaryBinarySource source) throws Exception {
        Map<CharSequence, Map<CharSequence, Double>> map = new HashMap<>();

        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(Files.newInputStream(source.getFilePath())))) {
            int mapSize = dis.readInt();
            for(int i=0; i<mapSize; i++) {
                CharSequence key = dis.readUTF();
                Map<CharSequence, Double> innerMap = new HashMap<>();
                map.put(key, innerMap);
                int innerMapSize = dis.readInt();
                for(int m=0; m<innerMapSize; m++) {
                    CharSequence innerKey = dis.readUTF();
                    Double value = dis.readDouble();
                    innerMap.put(innerKey, value);
                }
            }
        }

        return Collections.unmodifiableMap(map);
    }
}
