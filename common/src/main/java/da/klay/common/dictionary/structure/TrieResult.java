package da.klay.common.dictionary.structure;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TrieResult {

    private CharSequence data;
    private int startPosition;
    private int endPosition;

    public TrieResult(CharSequence data,
                      int startPosition,
                      int endPosition) {
        this.data = data;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public boolean hasResult() {
        return data != null;
    }
}
