package da.klay.core.tokenization.rule;

import da.klay.core.tokenization.TokenCharacterType;
import da.klay.core.tokenization.TokenResult;

/**
 * This rule must be the last one.
 */
public class CharacterTypeAndLengthLimitRule extends AbstractChainedTokenizationRule {

    private final int lengthLimit;

    public CharacterTypeAndLengthLimitRule(int lengthLimit) {
        super(null);
        this.lengthLimit = lengthLimit;
    }

    @Override
    public void apply(CharSequence cs, TokenResult token) {

        TokenCharacterType preCharType = null;
        int startPosition = token.getEndPosition();
        for(int i=startPosition; i<token.getOriginTextLength(); i++) {
            char ch = cs.charAt(i);
            Character.UnicodeBlock curUniBlock = Character.UnicodeBlock.of(ch);
            TokenCharacterType curCharType = TokenCharacterType.getCharType(curUniBlock, ch);

            if(!keepDoing(preCharType, curCharType)) break;

            token.set(startPosition, i+1, curCharType);
            if(token.length() >= lengthLimit) break;

            preCharType = curCharType;
        }
    }

    private boolean keepDoing(TokenCharacterType preCharType,
                              TokenCharacterType curCharType) {

        if(preCharType == null) return true;

        if(curCharType == TokenCharacterType.OTHERS) return false;

        return (curCharType != TokenCharacterType.OTHERS) && (preCharType == curCharType);
    }
}
