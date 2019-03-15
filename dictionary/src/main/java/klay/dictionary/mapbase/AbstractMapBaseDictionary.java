package klay.dictionary.mapbase;

import klay.dictionary.Dictionary;
import klay.dictionary.param.DictionaryBinarySource;
import klay.dictionary.param.DictionaryBinaryTarget;
import klay.dictionary.param.DictionaryTextSource;

import java.io.BufferedOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;

public abstract class AbstractMapBaseDictionary
        implements Dictionary<Map<CharSequence, Double>> {

    protected final Map<CharSequence, Map<CharSequence, Double>> map;

    protected AbstractMapBaseDictionary(DictionaryTextSource source) throws Exception {
        this.map = loadText(source);
    }

    protected AbstractMapBaseDictionary(DictionaryBinarySource source) throws Exception {
        this.map = loadBinary(source);
    }

    @Override
    public void save(DictionaryBinaryTarget target) throws Exception {

        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(Files.newOutputStream(target.getFilePath())))) {

            int mapSize = map.size();
            dos.writeInt(mapSize);
            for(CharSequence key : map.keySet()) {
                dos.writeUTF(key.toString());

                Map<CharSequence, Double> innerMap = map.get(key);
                int innerMapSize = innerMap.size();
                dos.writeInt(innerMapSize);
                for(CharSequence innerKey : innerMap.keySet()) {
                    dos.writeUTF(innerKey.toString());
                    dos.writeDouble(innerMap.get(innerKey));
                }
            }
        }
    }

    @Override
    public Map<CharSequence, Double> getFully(CharSequence key) {
        return map.get(key);
    }

    @Override
    public Map<CharSequence, Double> getFully(CharSequence key, int from, int keyLength) {
        throw new UnsupportedOperationException();
    }

    protected abstract Map<CharSequence, Map<CharSequence, Double>> loadText(DictionaryTextSource source) throws Exception;

    protected abstract Map<CharSequence, Map<CharSequence, Double>> loadBinary(DictionaryBinarySource source) throws Exception;
}
