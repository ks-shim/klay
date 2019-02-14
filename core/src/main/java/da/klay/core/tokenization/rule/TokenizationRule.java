package da.klay.core.tokenization.rule;

import da.klay.core.tokenization.Token;

public interface TokenizationRule {

    void apply(CharSequence cs, Token token);
}
