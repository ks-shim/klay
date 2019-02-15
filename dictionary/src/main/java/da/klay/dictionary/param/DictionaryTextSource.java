package da.klay.dictionary.param;

import lombok.Data;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Data
public class DictionaryTextSource {

    private Path filePath;
    private Charset charSet;

    public DictionaryTextSource(Path filePath) {
        this(filePath, StandardCharsets.UTF_8);
    }

    public DictionaryTextSource(Path filePath,
                                Charset charSet) {
        this.filePath = filePath;
        this.charSet = charSet;
    }
}
