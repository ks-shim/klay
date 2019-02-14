package da.klay.core.tokenization.rule;

import da.klay.core.tokenization.TokenResult;

public abstract class AbstractChainedTokenizationRule implements ChainedTokenizationRule {

    protected final ChainedTokenizationRule nextRule;

    protected AbstractChainedTokenizationRule(ChainedTokenizationRule nextRule) {
        this.nextRule = nextRule;
    }

    @Override
    public void apply(CharSequence cs, TokenResult token) {
        if(nextRule == null) return;
        nextRule.apply(cs, token);
    }
}
