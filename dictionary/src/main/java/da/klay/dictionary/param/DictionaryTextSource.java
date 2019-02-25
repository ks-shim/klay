package da.klay.dictionary.param;

import lombok.Data;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Data
public class DictionaryTextSource {

    public enum DictionaryType {
        DIC_WORD,
        DIC_IRREGULAR,
        GRAMMAR,
        OTHER
    }

    private final Path filePath;
    private final Charset charSet;

    private final DictionaryType dictionaryType;
    private Map<CharSequence, Integer> posFreqMap;

    public DictionaryTextSource(Path filePath) {
        this(filePath, StandardCharsets.UTF_8);
    }

    public DictionaryTextSource(Path filePath,
                                Charset charSet) {
        this(filePath, charSet, DictionaryType.OTHER);
    }

    public DictionaryTextSource(Path filePath,
                                DictionaryType dictionaryType) {
        this(filePath, StandardCharsets.UTF_8, dictionaryType);
    }

    public DictionaryTextSource(Path filePath,
                                Charset charSet,
                                DictionaryType dictionaryType) {
        this.filePath = filePath;
        this.charSet = charSet;
        this.dictionaryType = dictionaryType;
    }
}
