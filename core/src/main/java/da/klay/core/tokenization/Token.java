package da.klay.core.tokenization;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Token {

    private int startPosition;
    private int endPosition;

    public Token() {}

    public Token(int startPosition,
                 int endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public void reset() {
        startPosition = -1;
        endPosition = -1;
    }

    public void set(int startPosition, int endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }
}
