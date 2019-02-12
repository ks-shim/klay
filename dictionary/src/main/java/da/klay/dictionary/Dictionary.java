package da.klay.dictionary;

import da.klay.common.dictionary.structure.Trie;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;

public interface Dictionary<T, K> {

    T loadText(DictionaryTextSource source) throws Exception;

    T loadBinary(DictionaryBinarySource source) throws Exception;

    void save(DictionaryBinaryTarget target) throws Exception;

    K getFully(CharSequence cs);
}
