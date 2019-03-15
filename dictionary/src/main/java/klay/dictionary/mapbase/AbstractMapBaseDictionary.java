package klay.dictionary.mapbase;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import klay.dictionary.Dictionary;
import klay.dictionary.param.DictionaryBinarySource;
import klay.dictionary.param.DictionaryBinaryTarget;
import klay.dictionary.param.DictionaryTextSource;

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
        Kryo kryo = new Kryo();
        kryo.register(Map.class, 24253);
        try (Output out = new Output(Files.newOutputStream(target.getFilePath()))) {
            kryo.writeObject(out, map);
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
