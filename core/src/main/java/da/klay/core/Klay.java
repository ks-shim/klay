package da.klay.core;

import da.klay.common.parser.JasoParser;
import da.klay.core.tokenization.TokenResult;
import da.klay.core.tokenization.Tokenizer;
import da.klay.core.tokenization.rule.ChainedTokenizationRule;
import da.klay.core.tokenization.rule.CharacterTypeAndLengthLimitRule;
import da.klay.core.tokenization.rule.UserDictionaryMatchRule;
import da.klay.dictionary.mapbase.TransitionMapBaseDictionary;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryBinaryTarget;
import da.klay.dictionary.param.DictionaryTextSource;
import da.klay.dictionary.triebase.system.EmissionTrieBaseDictionary;
import da.klay.dictionary.triebase.user.UserTrieBaseDictionary;
import org.apache.commons.lang3.time.StopWatch;

import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class Klay {

    private final ChainedTokenizationRule tokenizationRule;

    public Klay() throws Exception {

        StopWatch watch = new StopWatch();
        watch.start();

        this.tokenizationRule = buildTokenizationRule();
        buildAnalysisRule();

        watch.stop();
        System.out.println("Dictionary Loading Time : " + watch.getTime(TimeUnit.MILLISECONDS) + " (ms)");
    }

    private ChainedTokenizationRule buildTokenizationRule() throws Exception {
        // 1. build user dictionary and rules .
        DictionaryTextSource userDicSource =
                new DictionaryTextSource(Paths.get("data/dictionary/text/user/dic.user"));

        UserTrieBaseDictionary userDictionary = new UserTrieBaseDictionary(userDicSource);
        ChainedTokenizationRule rule = new CharacterTypeAndLengthLimitRule(10);
        rule = new UserDictionaryMatchRule(userDictionary, rule);

        return rule;
    }

    private void buildAnalysisRule() throws Exception {
        DictionaryBinarySource emissionSource =
                new DictionaryBinarySource(Paths.get("data/dictionary/binary/system/emission.bin"));
        EmissionTrieBaseDictionary emissionDictionary = new EmissionTrieBaseDictionary(emissionSource);

        DictionaryBinarySource transitionSource =
                new DictionaryBinarySource(Paths.get("data/dictionary/binary/system/transition.bin"));
        TransitionMapBaseDictionary transitionDictionary = new TransitionMapBaseDictionary(transitionSource);
    }

    public void doKlay(CharSequence text) throws Exception {

        // 1. tokenization
        Tokenizer tokenizer = new Tokenizer(text, tokenizationRule);
        while(tokenizer.hasNext()) {
            TokenResult token = tokenizer.next();
            System.out.println(token + " : " + text.subSequence(token.getStartPosition(), token.getEndPosition()));
        }
    }

    public static void main(String[] args) throws Exception {
        Klay klay = new Klay();
        klay.doKlay("안녕하세요. dwayne입니다... klay를 개발하고 있습니다...!!");
    }
}
