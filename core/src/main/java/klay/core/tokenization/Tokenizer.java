package klay.core.tokenization;

import klay.core.tokenization.rule.ChainedTokenizationRule;

import java.util.Iterator;

public class Tokenizer implements Iterator<Token> {

    private final CharSequence text;
    private final ChainedTokenizationRule rule;
    private final Token token;

    public Tokenizer(CharSequence text, ChainedTokenizationRule rule) {
        this.text = text;
        this.rule = rule;
        this.token = new Token(text.length());
    }

    @Override
    public boolean hasNext() {
        return token.getOriginTextLength() > token.getEndPosition();
    }

    @Override
    public Token next() {
        rule.apply(text, token);
        return token;
    }
}
