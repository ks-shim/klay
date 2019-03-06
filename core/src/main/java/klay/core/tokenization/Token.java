package klay.core.tokenization;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Token {

    private int originTextLength;
    private int startPosition;
    private int endPosition;
    private CharSequence pos;
    private TokenCharacterType chType;

    public Token(int originTextLength) {
        this.originTextLength = originTextLength;
        reset();
    }

    public Token(int originTextLength,
                 int startPosition,
                 int endPosition) {
        this.originTextLength = originTextLength;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public void reset() {
        startPosition = 0;
        endPosition = 0;
        chType = null;
        pos = null;
    }

    public void set(int startPosition, int endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public void set(int startPosition, int endPosition, CharSequence pos) {
        this.set(startPosition, endPosition);
        this.pos = pos;
        this.chType = null;
    }

    public void set(int startPosition, int endPosition, TokenCharacterType chType) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.chType = chType;
        this.pos = chType.pos();
    }

    public boolean canSkipAnalysis() {
        return pos != null;
    }

    public boolean isWhiteSpace() {
        return TokenCharacterType.WHITE_SPACE == chType;
    }
    public int length() {
        return endPosition - startPosition;
    }
}
