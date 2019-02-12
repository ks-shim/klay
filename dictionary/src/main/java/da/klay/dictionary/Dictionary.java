package da.klay.dictionary;

import da.klay.common.dictionary.structure.Trie;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;

public interface Dictionary {

    Trie loadText(DictionaryTextSource source) throws Exception;

    Trie loadBinary(DictionaryBinarySource source) throws Exception;

    void save(DictionaryBinaryTarget target) throws Exception;

    CharSequence getFully(CharSequence cs);
}
