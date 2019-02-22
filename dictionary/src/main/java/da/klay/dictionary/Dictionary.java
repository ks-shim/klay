package da.klay.dictionary;

import da.klay.common.dictionary.structure.Trie;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;

public interface Dictionary<T> {

    void save(DictionaryBinaryTarget target) throws Exception;

    T getFully(CharSequence key);

    T getFully(CharSequence key, int from, int keyLength);
}
