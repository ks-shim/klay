package da.klay.dictionary.triebase.system;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CachedEmissionTrieBaseDictionary {

    //protected final LoadingCache<CharSequence, Object> cache;

    private LoadingCache<CharSequence, Object> newCache(int maxSize) {
        LoadingCache<CharSequence, Object> cache;
        cache = CacheBuilder.newBuilder().maximumSize(maxSize).build(
                new CacheLoader<CharSequence, Object>() {
                    @Override
                    public Object load(CharSequence key) throws Exception {
                        return null;//trie.getFully(key);
                    }
                }
        );
        return cache;
    }
}
