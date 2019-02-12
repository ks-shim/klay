package da.klay.dictionary.param;

import da.klay.common.dictionary.structure.Optimizer;
import da.klay.common.dictionary.structure.Reduce;
import lombok.Data;

import java.nio.file.Path;

@Data
public class DictionaryBinaryTarget {

    private Path filePath;
    private Reduce reduce;

    public DictionaryBinaryTarget(Path filePath) {
        this(filePath, new Optimizer());
    }

    public DictionaryBinaryTarget(Path filePath,
                                  Reduce reduce) {
        this.filePath = filePath;
        this.reduce = reduce;
    }
}
