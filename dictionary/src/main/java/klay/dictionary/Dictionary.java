package klay.dictionary;

import klay.dictionary.param.DictionaryBinaryTarget;

public interface Dictionary<T> {

    void save(DictionaryBinaryTarget target) throws Exception;

    T getFully(CharSequence key);

    T getFully(CharSequence key, int from, int keyLength);
}
