package klay.dictionary.triebase;

import klay.common.dictionary.structure.Trie;
import klay.common.dictionary.structure.TrieLoadSaveHelper;
import klay.common.dictionary.structure.TrieResult;
import klay.dictionary.Dictionary;
import klay.dictionary.param.DictionaryBinarySource;
import klay.dictionary.param.DictionaryBinaryTarget;
import klay.dictionary.param.DictionaryTextSource;
import lombok.Data;

@Data
public abstract class AbstractTrieBaseDictionary<T> implements Dictionary {

    protected final Trie<T> trie;

    protected AbstractTrieBaseDictionary(DictionaryTextSource source) throws Exception {
        this.trie = loadText(source);
    }

    protected AbstractTrieBaseDictionary(DictionaryTextSource[] sources) throws Exception {
        this.trie = loadText(sources);
    }

    protected AbstractTrieBaseDictionary(DictionaryBinarySource source) throws Exception {
        this.trie = loadBinary(source);

    }

    @Override
    public T getFully(CharSequence key) {
        return getFully(key, 0, key.length());
    }

    @Override
    public T getFully(CharSequence key, int from, int keyLength) {
        return trie.getFully(key, from, keyLength);
    }

    @Override
    public void save(DictionaryBinaryTarget target) throws Exception {
        TrieLoadSaveHelper.store(trie, target.getFilePath());
    }

    public TrieResult<T> getLastOnPath(CharSequence key) {
        return trie.getLastOnPath(key);
    }

    public TrieResult<T> getLastOnPath(CharSequence key, int from) {
        return trie.getLastOnPath(key, from);
    }

    public TrieResult<T>[] getAll(CharSequence key) {
        return getAll(key, 0);
    }

    public TrieResult<T>[] getAll(CharSequence key, int from) {
        return trie.getAll(key, from);
    }

    protected abstract Trie<T> loadText(DictionaryTextSource source) throws Exception;

    protected abstract Trie<T> loadText(DictionaryTextSource[] sources) throws Exception;

    protected abstract Trie<T> loadBinary(DictionaryBinarySource source) throws Exception;


}
