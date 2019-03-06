package klay.core.tokenization.rule;

import klay.core.tokenization.Token;

public interface ChainedTokenizationRule {

    void apply(CharSequence cs, Token token);
}
