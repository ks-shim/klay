package da.klay.dictionary.triebase;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import da.klay.common.dictionary.structure.Trie;
import da.klay.common.dictionary.structure.TrieLoadSaveHelper;
import da.klay.dictionary.Dictionary;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;
import lombok.Data;

@Data
public abstract class AbstractTrieBaseDictionary implements Dictionary<Trie, CharSequence> {

    protected final Trie trie;

    protected AbstractTrieBaseDictionary(DictionaryTextSource source) throws Exception {
        this.trie = loadText(source);
    }

    protected AbstractTrieBaseDictionary(DictionaryBinarySource source) throws Exception {
        this.trie = loadBinary(source);

    }

    @Override
    public CharSequence getFully(CharSequence cs) {
        return trie.getFully(cs);
    }

    @Override
    public CharSequence getLastOnPath(CharSequence cs) {
        return trie.getLastOnPath(cs);
    }

    @Override
    public CharSequence[] getAll(CharSequence cs) {
        return trie.getAll(cs);
    }

    @Override
    public Trie loadText(DictionaryTextSource source) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public Trie loadBinary(DictionaryBinarySource source) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(DictionaryBinaryTarget target) throws Exception {
        TrieLoadSaveHelper.store(trie, target.getFilePath());
    }
}
