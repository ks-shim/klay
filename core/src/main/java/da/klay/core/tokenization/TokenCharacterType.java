package da.klay.core.tokenization;

import da.klay.common.pos.Pos;

import java.util.HashSet;
import java.util.Set;

public enum TokenCharacterType {

    KOREAN(null),
    ENGLISH(Pos.SL.label()),
    CHINESE(Pos.SH.label()),
    JAPANESE(Pos.SL.label()),
    NUMERIC(Pos.SN.label()),
    WHITE_SPACE(null),
    OTHERS(null);

    private String pos;
    private TokenCharacterType(String _pos) {
        pos = _pos;
    }

    public String pos() {
        return pos;
    }

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

    public static TokenCharacterType getCharType(Character.UnicodeBlock unicodeBlock,
                                           char ch) {
        if(HANGUL_UNICODE_BLOCK_SET.contains(unicodeBlock)) return KOREAN;
        else if(ENGLISH_UNICODE_BLOCK_SET.contains(unicodeBlock) && Character.isAlphabetic(ch)) return ENGLISH;
        else if(CHINESE_UNICODE_BLOCK_SET.contains(unicodeBlock)) return CHINESE;
        else if(JAPANESE_UNICODE_BLOCK_SET.contains(unicodeBlock)) return JAPANESE;
        else if(Character.isDigit(ch)) return NUMERIC;
        else if(Character.isWhitespace(ch)) return WHITE_SPACE;
        else return OTHERS;
    }
}
