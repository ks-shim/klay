package da.klay.core.tokenization.rule;

import da.klay.common.dictionary.structure.TrieResult;
import da.klay.core.tokenization.Token;
import da.klay.dictionary.triebase.user.UserTrieBaseDictionary;

public class UserDictionaryMatchRule extends AbstractChainedTokenizationRule {

    private final UserTrieBaseDictionary dictionary;

    public UserDictionaryMatchRule(UserTrieBaseDictionary dictionary,
                                   ChainedTokenizationRule nextRule) {
        super(nextRule);
        this.dictionary = dictionary;
    }

    @Override
    public void apply(CharSequence cs, Token token) {

        TrieResult result = dictionary.getLastOnPath(cs, token.getEndPosition());
        if(result.hasResult()) {
            token.set(result.getStartPosition(), result.getEndPosition(), result.getData());
            return;
        }

        // If not found ...
        super.apply(cs, token);
    }
}
