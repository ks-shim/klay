package da.klay.dictionary.mapbase;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import da.klay.dictionary.Dictionary;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;

import java.nio.file.Files;
import java.util.Map;

public abstract class AbstractMapBaseDictionary
        implements Dictionary<Map<CharSequence, Map<CharSequence, Integer>>, Map<CharSequence, Integer>> {

    protected final Map<CharSequence, Map<CharSequence, Integer>> map;

    protected AbstractMapBaseDictionary(DictionaryTextSource source) throws Exception {
        this.map = loadText(source);
    }

    protected AbstractMapBaseDictionary(DictionaryBinarySource source) throws Exception {
        this.map = loadBinary(source);
    }

    protected abstract Map<CharSequence, Map<CharSequence, Integer>> loadText(DictionaryTextSource source) throws Exception;

    protected abstract Map<CharSequence, Map<CharSequence, Integer>> loadBinary(DictionaryBinarySource source) throws Exception;

    @Override
    public void save(DictionaryBinaryTarget target) throws Exception {
        Kryo kryo = new Kryo();
        try (Output out = new Output(Files.newOutputStream(target.getFilePath()))) {
            kryo.writeObject(out, map);
        }
    }

    @Override
    public Map<CharSequence, Integer> getFully(CharSequence cs) {
        return map.get(cs);
    }

    @Override
    public Map<CharSequence, Integer> getLastOnPath(CharSequence cs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<CharSequence, Integer>[] getAll(CharSequence cs) {
        throw new UnsupportedOperationException();
    }
}
