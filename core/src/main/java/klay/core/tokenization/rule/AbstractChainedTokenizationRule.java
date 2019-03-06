package klay.core.tokenization.rule;

import klay.core.tokenization.Token;

public abstract class AbstractChainedTokenizationRule implements ChainedTokenizationRule {

    protected final ChainedTokenizationRule nextRule;

    protected AbstractChainedTokenizationRule(ChainedTokenizationRule nextRule) {
        this.nextRule = nextRule;
    }

    @Override
    public void apply(CharSequence cs, Token token) {
        if(nextRule == null) return;
        nextRule.apply(cs, token);
    }
}
