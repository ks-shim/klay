package da.klay.dictionary.param;

import lombok.Data;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Data
public class DictionaryTextSource {

    private final Path filePath;
    private final Charset charSet;

    private final boolean usePosFreq;
    private Map<CharSequence, Integer> posFreqMap;

    public DictionaryTextSource(Path filePath) {
        this(filePath, StandardCharsets.UTF_8);
    }

    public DictionaryTextSource(Path filePath,
                                Charset charSet) {
        this(filePath, charSet, false);
    }

    public DictionaryTextSource(Path filePath,
                                boolean usePosFreq) {
        this(filePath, StandardCharsets.UTF_8, usePosFreq);
    }

    public DictionaryTextSource(Path filePath,
                                Charset charSet,
                                boolean usePosFreq) {
        this.filePath = filePath;
        this.charSet = charSet;
        this.usePosFreq = usePosFreq;
    }
}
