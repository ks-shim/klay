package da.klay.core.tokenization;

import java.util.Iterator;

public class Tokenizer implements Iterator<Token> {

    private final String text;
    private int currentPosition;

    public Tokenizer(String text) {
        this.text = text;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Token next() {
        return null;
    }
}
