package da.klay.core.tokenization;

import da.klay.core.tokenization.rule.ChainedTokenizationRule;

import java.util.Iterator;

public class Tokenizer implements Iterator<TokenResult> {

    private final String text;
    private final ChainedTokenizationRule rule;
    private final TokenResult token;

    public Tokenizer(String text, ChainedTokenizationRule rule) {
        this.text = text;
        this.rule = rule;
        this.token = new TokenResult(text.length());
    }

    @Override
    public boolean hasNext() {
        return token.getOriginTextLength() > token.getEndPosition();
    }

    @Override
    public TokenResult next() {
        rule.apply(text, token);
        return token;
    }
}
