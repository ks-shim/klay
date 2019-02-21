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
        ChainedTokenizationRule rule = new CharacterTypeAndLengthLimitRule(10);
        rule = new UserDictionaryMatchRule(userDictionary, rule);

        // 2. create Tokenizer
        String text = "dwayne! 너 거기서 뭐하니? 빨리 개발 안하고 ... \n [[[알리타]]볼래볼래볼래볼래볼래볼래2볼래볼래볼래볼래볼래볼래볼래?";
        Tokenizer tokenizer = new Tokenizer(text, rule);

        // 3. get started test
        int tokenCount = 0;
        while(tokenizer.hasNext()) {
            Token result = tokenizer.next();
            //System.out.println(result + " : " + text.substring(result.getStartPosition(), result.getEndPosition()));
            tokenCount++;
        }

        assertEquals(27, tokenCount);
    }
}