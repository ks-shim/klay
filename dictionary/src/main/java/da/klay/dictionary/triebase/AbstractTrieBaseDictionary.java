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

    protected AbstractTrieBaseDictionary(DictionaryBinarySource source) throws Exception {
        this.trie = loadBinary(source);

    }

    @Override
    public CharSequence getFully(CharSequence key) {
        return trie.getFully(key);
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

    protected abstract Trie loadBinary(DictionaryBinarySource source) throws Exception;


}
