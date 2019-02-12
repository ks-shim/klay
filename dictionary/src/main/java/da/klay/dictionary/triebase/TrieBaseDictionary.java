package da.klay.dictionary.triebase;

import da.klay.common.dictionary.structure.Trie;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;

public interface TrieBaseDictionary {

    Trie loadText(DictionaryTextSource source) throws Exception;

    Trie loadBinary(DictionaryBinarySource source) throws Exception;

    void save(DictionaryBinaryTarget target) throws Exception;

    CharSequence getFully(CharSequence cs);
}
