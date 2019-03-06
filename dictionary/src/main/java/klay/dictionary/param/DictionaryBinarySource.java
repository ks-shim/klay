package klay.dictionary.param;

import lombok.Data;

import java.nio.file.Path;

@Data
public class DictionaryBinarySource {

    private Path filePath;

    public DictionaryBinarySource(Path filePath) {
        this.filePath = filePath;
    }
}
