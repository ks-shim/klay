package klay.dictionary.param;

import lombok.Data;

import java.nio.file.Path;

@Data
public class DictionaryBinaryTarget {

    private Path filePath;

    public DictionaryBinaryTarget(Path filePath) {
        this.filePath = filePath;
    }
}
