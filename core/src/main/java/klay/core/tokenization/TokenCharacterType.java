package klay.core.tokenization;

import klay.common.pos.Pos;

import java.util.HashSet;
import java.util.Set;

public enum TokenCharacterType {

    KOREAN(null),
    ENGLISH(Pos.SL.label()),
    CHINESE(Pos.SH.label()),
    JAPANESE(Pos.SL.label()),
    NUMERIC(Pos.SN.label()),
    WHITE_SPACE(null),
    MARK_SS(Pos.SS.label()), // 따옴표,괄호표,줄표
    MARK_SF(Pos.SF.label()), // 마침표,물음표,느낌표
    MARK_SW(Pos.SW.label()), // 기타기호 (논리수학기호,화폐기호)
    MARK_SP(Pos.SP.label()), // 쉽표,가운데점,콜론,빗금
    MARK_SO(Pos.SO.label()), // 붙임표(물결,숨김,빠짐)
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

    private final static Set<Character> MARK_SS_SET;
    private final static Set<Character> MARK_SF_SET;
    private final static Set<Character> MARK_SW_SET;
    private final static Set<Character> MARK_SP_SET;
    private final static Set<Character> MARK_SO_SET;

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

        MARK_SS_SET = new HashSet<>();
        MARK_SS_SET.add('<');
        MARK_SS_SET.add('>');
        MARK_SS_SET.add('(');
        MARK_SS_SET.add(')');
        MARK_SS_SET.add('{');
        MARK_SS_SET.add('}');
        MARK_SS_SET.add('[');
        MARK_SS_SET.add(']');
        MARK_SS_SET.add('`');

        MARK_SF_SET = new HashSet<>();
        MARK_SF_SET.add('.');
        MARK_SF_SET.add('!');
        MARK_SF_SET.add('?');

        MARK_SW_SET = new HashSet<>();
        MARK_SW_SET.add('@');
        MARK_SW_SET.add('#');
        MARK_SW_SET.add('$');
        MARK_SW_SET.add('%');
        MARK_SW_SET.add('^');
        MARK_SW_SET.add('&');
        MARK_SW_SET.add('*');
        MARK_SW_SET.add('\\');
        MARK_SW_SET.add('|');
        MARK_SW_SET.add('+');
        MARK_SW_SET.add('=');

        MARK_SP_SET = new HashSet<>();
        MARK_SP_SET.add('/');
        MARK_SP_SET.add(':');
        MARK_SP_SET.add(';');
        MARK_SP_SET.add('"');
        MARK_SP_SET.add('\'');
        MARK_SP_SET.add('·');
        MARK_SP_SET.add(',');

        MARK_SO_SET = new HashSet<>();
        MARK_SO_SET.add('~');
    }

    public static TokenCharacterType getCharType(Character.UnicodeBlock unicodeBlock,
                                                 char ch) {
        if(HANGUL_UNICODE_BLOCK_SET.contains(unicodeBlock)) return KOREAN;
        else if(ENGLISH_UNICODE_BLOCK_SET.contains(unicodeBlock) && Character.isAlphabetic(ch)) return ENGLISH;
        else if(CHINESE_UNICODE_BLOCK_SET.contains(unicodeBlock)) return CHINESE;
        else if(JAPANESE_UNICODE_BLOCK_SET.contains(unicodeBlock)) return JAPANESE;
        else if(Character.isDigit(ch)) return NUMERIC;
        else if(Character.isWhitespace(ch)) return WHITE_SPACE;
        else if(MARK_SS_SET.contains(ch)) return MARK_SS;
        else if(MARK_SF_SET.contains(ch)) return MARK_SF;
        else if(MARK_SW_SET.contains(ch)) return MARK_SW;
        else if(MARK_SP_SET.contains(ch)) return MARK_SP;
        else if(MARK_SO_SET.contains(ch)) return MARK_SO;
        else return OTHERS;
    }

    public static boolean isSpecialCharacterType(TokenCharacterType characterType) {
        return characterType == OTHERS || characterType == MARK_SS || characterType == MARK_SF ||
                characterType == MARK_SW || characterType == MARK_SP || characterType == MARK_SO;
    }
}
