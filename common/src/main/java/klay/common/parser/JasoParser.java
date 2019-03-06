package klay.common.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JasoParser {

    public final static char[] CHO_SUNG = {0x3131, 0x3132, 0x3134, 0x3137, 0x3138,
            0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148,
            0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};
    public final static char[] JOONG_SUNG = {0x314f, 0x3150, 0x3151, 0x3152, 0x3153,
            0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a, 0x315b,
            0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162, 0x3163};
    public final static char[] JONG_SUNG = {0x0000, 0x3131, 0x3132, 0x3133, 0x3134,
            0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c, 0x313d,
            0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145, 0x3146,
            0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};

    public static CharSequence parseAsString(CharSequence s, int from, int to) {
        StringBuilder sb = new StringBuilder();

        for (int i = from; i < to; i++) {
            char ch = s.charAt(i);

            Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);
            // cases no needed to parseAsList ...
            if (block != Character.UnicodeBlock.HANGUL_SYLLABLES) {
                sb.append(ch);
                continue;
            }

            int tmp = ch - 0xAC00;
            int cho = tmp / (21 * 28);
            tmp = tmp % (21 * 28);
            int joong = tmp / 28;
            int jong = tmp % 28;
            sb.append(CHO_SUNG[cho]);
            sb.append(JOONG_SUNG[joong]);
            if (jong != 0) sb.append(JONG_SUNG[jong]);
        }

        return sb.toString();
    }

    public static CharSequence parseAsString(CharSequence s) {
        return parseAsString(s, 0, s.length());
    }

    public static List<Character> parseAsList(CharSequence s) {
        List<Character> list = new ArrayList<>();

        s = parseAsString(s);
        for(int i=0; i<s.length(); i++)
            list.add(s.charAt(i));

        return list;
    }
}
