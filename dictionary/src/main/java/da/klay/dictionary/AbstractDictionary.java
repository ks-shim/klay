package da.klay.dictionary;

import da.klay.common.dictionary.structure.Trie;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;
import lombok.Data;

@Data
public abstract class AbstractDictionary implements Dictionary {

    protected final Trie trie;

    protected AbstractDictionary(DictionaryTextSource source) throws Exception {
        this.trie = loadText(source);
    }

    protected AbstractDictionary(DictionaryBinarySource source) throws Exception {
        this.trie = loadBinary(source);
    }

    @Override
    public CharSequence getFully(CharSequence cs) {
        return trie.getFully(cs);
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
        throw new UnsupportedOperationException();
    }
}
