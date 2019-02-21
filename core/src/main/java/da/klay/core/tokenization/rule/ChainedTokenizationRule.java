package da.klay.core.tokenization.rule;

import da.klay.core.tokenization.Token;

public interface ChainedTokenizationRule {

    void apply(CharSequence cs, Token token);
}
