package da.klay.core.tokenization.rule;

import da.klay.common.pos.Pos;
import da.klay.core.tokenization.TokenResult;

import java.util.HashSet;
import java.util.Set;

public class CharacterBlockTypeAndLengthLimitRule extends AbstractChainedTokenizationRule {

    private final static Set<Character.UnicodeBlock> HANGUL_UNICODE_BLOCK_SET;
    private final static Set<Character.UnicodeBlock> ENGLISH_UNICODE_BLOCK_SET;
    private final static Set<Character.UnicodeBlock> CHINESE_UNICODE_BLOCK_SET;
    private final static Set<Character.UnicodeBlock> JAPANESE_UNICODE_BLOCK_SET;

    static {
        HANGUL_UNICODE_BLOCK_SET = new HashSet<>();
        HANGUL_UNICODE_BLOCK_SET.add(Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO);
        HANGUL_UNICODE_BLOCK_SET.add(Character.UnicodeBlock.HANGUL_JAMO);
        HANGUL_UNICODE_BLOCK_SET.add(Character.UnicodeBlock.HANGUL_SYLLABLES);

        ENGLISH_UNICODE_BLOCK_SET = new HashSet<>();
        ENGLISH_UNICODE_BLOCK_SET.add(Character.UnicodeBlock.BASIC_LATIN);

        CHINESE_UNICODE_BLOCK_SET = new HashSet<>();
        CHINESE_UNICODE_BLOCK_SET.add(Character.UnicodeBlock.CJK_COMPATIBILITY);
        CHINESE_UNICODE_BLOCK_SET.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
        CHINESE_UNICODE_BLOCK_SET.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
        CHINESE_UNICODE_BLOCK_SET.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B);
        CHINESE_UNICODE_BLOCK_SET.add(Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);

        JAPANESE_UNICODE_BLOCK_SET = new HashSet<>();
        JAPANESE_UNICODE_BLOCK_SET.add(Character.UnicodeBlock.HIRAGANA);
        JAPANESE_UNICODE_BLOCK_SET.add(Character.UnicodeBlock.KATAKANA);
        JAPANESE_UNICODE_BLOCK_SET.add(Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS);
    }

    private final int lengthLimit;

    public CharacterBlockTypeAndLengthLimitRule(int lengthLimit,
                                                ChainedTokenizationRule nextRule) {
        super(nextRule);
        this.lengthLimit = lengthLimit;
    }

    @Override
    public void apply(CharSequence cs, TokenResult token) {

        Character.UnicodeBlock preUniBlock = null;
        char preCh = ' ';
        int startPosition = token.getEndPosition();
        for(int i=startPosition; i<token.getOriginTextLength(); i++) {
            char ch = cs.charAt(i);
            Character.UnicodeBlock curUniBlock = Character.UnicodeBlock.of(ch);

            if(!keepDoing(preUniBlock, curUniBlock, preCh, ch, startPosition, i+1, token)) break;
            //token.set(startPosition, i+1);
            if(token.length() >= lengthLimit) break;

            preUniBlock = curUniBlock;
            preCh = ch;
        }

        //super.apply(cs, token);
    }

    private enum CharacterType {

        KOREAN(null),
        ENGLISH(Pos.SL.label()),
        CHINESE(Pos.SH.label()),
        JAPANESE(Pos.SH.label()),
        NUMERIC(Pos.SN.label()),
        WHITE_SPACE(Pos.SW.label()),
        OTHERS(null);

        private String pos;
        private CharacterType(String _pos) {
            pos = _pos;
        }
    }

    private boolean keepDoing(Character.UnicodeBlock preUniBlock,
                              Character.UnicodeBlock curUniBlock,
                              char preCh,
                              char curCh,
                              int from,
                              int to,
                              TokenResult token) {

        // 1. no need to check whether continuous or not
        CharacterType curChType = getCharType(curUniBlock, curCh);
        if(curChType == CharacterType.OTHERS) {
            token.set(from, to, curChType.pos);
            return false;
        } else if(preUniBlock == null) { // 2. If not other and the first character
            token.set(from, to, curChType.pos);
            return true;
        }

        CharacterType preChType = getCharType(preUniBlock, preCh);
        if(preChType == CharacterType.OTHERS) return false;

        //CharacterType curChType = getCharType(curUniBlock, curCh);


        // 2. continuous Korean
        if(isKorean(preUniBlock) && isKorean(curUniBlock)) return true;

        // 3. continuous English
        if(isEnglish(preUniBlock, preCh) && isEnglish(curUniBlock, curCh)) return true;

        // 4. continuous Chinese
        if(isChinese(preUniBlock) && isChinese(curUniBlock)) return true;

        // 5. continuous Japanese
        if(isJapanese(preUniBlock) && isJapanese(curUniBlock)) return true;

        // 6. continuous Numeric values
        if(isNumeric(preCh) && isNumeric(curCh)) return true;

        // 7. continuous Whitespaces
        if(isWhiteSpace(preCh) && isWhiteSpace(curCh)) return true;

        return false;
    }

    private CharacterType getCharType(Character.UnicodeBlock unicodeBlock,
                                      char ch) {
        if(isKorean(unicodeBlock)) return CharacterType.KOREAN;
        else if(isEnglish(unicodeBlock, ch)) return CharacterType.ENGLISH;
        else if(isChinese(unicodeBlock)) return CharacterType.CHINESE;
        else if(isJapanese(unicodeBlock)) return CharacterType.JAPANESE;
        else if(isNumeric(ch)) return CharacterType.NUMERIC;
        else if(isWhiteSpace(ch)) return CharacterType.WHITE_SPACE;
        else return CharacterType.OTHERS;
    }


    private boolean isKorean(Character.UnicodeBlock unicodeBlock) {
        return HANGUL_UNICODE_BLOCK_SET.contains(unicodeBlock);
    }

    private boolean isEnglish(Character.UnicodeBlock unicodeBlock,
                              char ch) {
        return ENGLISH_UNICODE_BLOCK_SET.contains(unicodeBlock) && Character.isAlphabetic(ch);
    }

    private boolean isChinese(Character.UnicodeBlock unicodeBlock) {
        return CHINESE_UNICODE_BLOCK_SET.contains(unicodeBlock);
    }

    private boolean isJapanese(Character.UnicodeBlock unicodeBlock) {
        return JAPANESE_UNICODE_BLOCK_SET.contains(unicodeBlock);
    }

    private boolean isNumeric(char ch) {
        return Character.isDigit(ch);
    }

    private boolean isWhiteSpace(char ch) {
        return Character.isWhitespace(ch);
    }

    public static void main(String[] args) throws Exception {
        String text = "　\taSW:2\tSO:11안녕하세요 ㄴ입니다.\n\r";
        for(int i=0; i<text.length(); i++) {
            char ch = text.charAt(i);

            System.out.println(ch + " : " + Character.UnicodeBlock.of(ch) + " : " + Character.getType(ch) + " : " + Character.isWhitespace(ch) + " : " + Character.isAlphabetic(ch));
        }
    }
}
