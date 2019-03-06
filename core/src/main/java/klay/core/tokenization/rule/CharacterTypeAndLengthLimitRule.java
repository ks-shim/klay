package klay.core.tokenization.rule;

import klay.core.tokenization.TokenCharacterType;
import klay.core.tokenization.Token;

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
    public void apply(CharSequence cs, Token token) {

        TokenCharacterType preCharType = null;
        char preCh = ' ';
        int startPosition = token.getEndPosition();
        for(int i=startPosition; i<token.getOriginTextLength(); i++) {
            char curCh = cs.charAt(i);
            Character.UnicodeBlock curUniBlock = Character.UnicodeBlock.of(curCh);
            TokenCharacterType curCharType = TokenCharacterType.getCharType(curUniBlock, curCh);

            if(!keepDoing(preCharType, preCh, curCharType, curCh)) break;

            token.set(startPosition, i+1, curCharType);
            if(token.length() >= lengthLimit) break;

            preCharType = curCharType;
            preCh = curCh;
        }
    }

    private boolean keepDoing(TokenCharacterType preCharType,
                              char preCh,
                              TokenCharacterType curCharType,
                              char curCh) {

        if(preCharType == null) return true;

        if(TokenCharacterType.isSpecialCharacterType(curCharType)) {
            if(preCh != curCh) return false;
            else return true; // keep doing for continuous special characters.
        }

        return (curCharType != TokenCharacterType.OTHERS) && (preCharType == curCharType);
    }
}
