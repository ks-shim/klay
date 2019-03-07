package klay.common.dictionary.structure;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TrieResult<T> {

    private T data;
    private int startPosition;
    private int endPosition;

    public TrieResult(T data,
                      int startPosition,
                      int endPosition) {
        this.data = data;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public boolean hasResult() {
        return data != null;
    }

    public int length() {
        return endPosition - startPosition;
    }
}
