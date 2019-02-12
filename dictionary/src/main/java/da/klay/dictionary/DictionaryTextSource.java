package da.klay.dictionary;

import lombok.Data;

import java.nio.charset.Charset;
import java.nio.file.Path;

@Data
public class DictionaryTextSource {

    private Path filePath;
    private Charset charSet;

    public DictionaryTextSource(Path filePath,
                                Charset charSet) {
        this.filePath = filePath;
        this.charSet = charSet;
    }
}
