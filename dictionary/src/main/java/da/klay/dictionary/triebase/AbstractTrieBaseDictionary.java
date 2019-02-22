package da.klay.dictionary.triebase;

import da.klay.common.dictionary.structure.TrieResult;
import da.klay.common.dictionary.structure.Trie;
import da.klay.common.dictionary.structure.TrieLoadSaveHelper;
import da.klay.dictionary.Dictionary;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;
import lombok.Data;

@Data
public abstract class AbstractTrieBaseDictionary implements Dictionary<CharSequence> {

    protected final Trie trie;

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
    public CharSequence getFully(CharSequence key) {
        return getFully(key, 0, key.length());
    }

    @Override
    public CharSequence getFully(CharSequence key, int from, int keyLength) {
        return trie.getFully(key, from, keyLength);
    }

    @Override
    public void save(DictionaryBinaryTarget target) throws Exception {
        TrieLoadSaveHelper.store(trie, target.getFilePath());
    }

    public TrieResult getLastOnPath(CharSequence key) {
        return trie.getLastOnPath(key);
    }

    public TrieResult getLastOnPath(CharSequence key, int from) {
        return trie.getLastOnPath(key, from);
    }

    public TrieResult[] getAll(CharSequence key) {
        return getAll(key, 0);
    }

    public TrieResult[] getAll(CharSequence key, int from) {
        return trie.getAll(key, from);
    }

    protected abstract Trie loadText(DictionaryTextSource source) throws Exception;

    protected abstract Trie loadText(DictionaryTextSource[] sources) throws Exception;

    protected abstract Trie loadBinary(DictionaryBinarySource source) throws Exception;


}
