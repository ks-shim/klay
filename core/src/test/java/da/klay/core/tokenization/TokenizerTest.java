package da.klay.core.tokenization;

import da.klay.core.tokenization.rule.ChainedTokenizationRule;
import da.klay.core.tokenization.rule.CharacterTypeAndLengthLimitRule;
import da.klay.core.tokenization.rule.UserDictionaryMatchRule;
import da.klay.dictionary.param.DictionaryTextSource;
import da.klay.dictionary.triebase.user.UserTrieBaseDictionary;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

    @Test
    void test() throws Exception {

        // 1. build rule chain.
        Path path = Paths.get("src/test/resources/test.dic.user");
        UserTrieBaseDictionary userDictionary = new UserTrieBaseDictionary(new DictionaryTextSource(path));
        ChainedTokenizationRule rule = new CharacterTypeAndLengthLimitRule(64);
        rule = new UserDictionaryMatchRule(userDictionary, rule);

        // 2. create Tokenizer
        String text = "dwayne! 너 거기서 뭐하니? 빨리 개발 안하고 ...  알리타볼래?";
        Tokenizer tokenizer = new Tokenizer(text, rule);
        while(tokenizer.hasNext()) {
            TokenResult result = tokenizer.next();
            System.out.print(result);
            System.out.println(" : " + text.substring(result.getStartPosition(), result.getEndPosition()));
        }
    }
}