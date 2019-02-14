package da.klay.core.tokenization.rule;

import da.klay.core.tokenization.TokenResult;

public interface ChainedTokenizationRule {

    void apply(CharSequence cs, TokenResult token);
}
