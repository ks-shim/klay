package klay.core;

import klay.core.morphology.analysis.Morph;
import klay.core.morphology.analysis.Morphs;
import klay.core.morphology.analysis.rule.*;
import klay.core.morphology.analysis.rule.param.AnalysisParam;
import klay.core.morphology.analysis.sequence.MorphSequence;
import klay.core.morphology.analysis.sequence.SingleMorphSequence;
import klay.core.tokenization.Token;
import klay.core.tokenization.Tokenizer;
import klay.core.tokenization.rule.ChainedTokenizationRule;
import klay.core.tokenization.rule.CharacterTypeAndLengthLimitRule;
import klay.core.tokenization.rule.UserDictionaryMatchRule;
import klay.dictionary.mapbase.TransitionMapBaseDictionary;
import klay.dictionary.param.DictionaryBinarySource;
import klay.dictionary.param.DictionaryTextSource;
import klay.dictionary.triebase.system.EmissionTrieBaseDictionary;
import klay.dictionary.triebase.user.FWDUserTrieBaseDictionary;
import klay.dictionary.triebase.user.UserTrieBaseDictionary;
import org.apache.commons.lang3.time.StopWatch;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

// TODO : 1. FWDAndEmissionMixRule 만들기 (FWD 사전 + Emission사전)
// TODO : 2. Testcase 작성하기
public class Klay {

    private final ChainedTokenizationRule tokenizationRule;
    private final ChainedAnalysisRule chainedAnalysisRule;
    private final TransitionMapBaseDictionary transitionDictionary;

    private final Properties config;
    public Klay(Path configFilePath) throws Exception {

        config = new Properties();
        config.load(Files.newInputStream(configFilePath));

        StopWatch watch = new StopWatch();
        watch.start();

        this.transitionDictionary = buildTransitionDictionary();
        this.tokenizationRule = buildTokenizationRule();
        this.chainedAnalysisRule = buildAnalysisRule();

        watch.stop();
        System.out.println("Dictionary Loading Time : " + watch.getTime(TimeUnit.MILLISECONDS) + " (ms)");
    }

    public Klay(Properties config) throws Exception {

        this.config = config;

        StopWatch watch = new StopWatch();
        watch.start();

        this.transitionDictionary = buildTransitionDictionary();
        this.tokenizationRule = buildTokenizationRule();
        this.chainedAnalysisRule = buildAnalysisRule();

        watch.stop();
        System.out.println("Dictionary Loading Time : " + watch.getTime(TimeUnit.MILLISECONDS) + " (ms)");
    }

    public Klay(String inUserDicFilePath,
                String inFwdDicFilePath,
                String inEmissionFilePath,
                String inTransitionFilePath) throws Exception {

        this.config = buildConfig(inUserDicFilePath, inFwdDicFilePath,
                inEmissionFilePath, inTransitionFilePath);

        StopWatch watch = new StopWatch();
        watch.start();

        this.transitionDictionary = buildTransitionDictionary();
        this.tokenizationRule = buildTokenizationRule();
        this.chainedAnalysisRule = buildAnalysisRule();

        watch.stop();
        System.out.println("Dictionary Loading Time : " + watch.getTime(TimeUnit.MILLISECONDS) + " (ms)");
    }

    private Properties buildConfig(String inUserDicFilePath,
                                   String inFwdDicFilePath,
                                   String inEmissionFilePath,
                                   String inTransitionFilePath) {
        Properties prop = new Properties();
        prop.put("dictionary.user.path", inUserDicFilePath);
        prop.put("dictionary.fwd.path", inFwdDicFilePath);
        prop.put("dictionary.emission.path", inEmissionFilePath);
        prop.put("dictionary.transition.path", inTransitionFilePath);
        return prop;
    }

    private ChainedTokenizationRule buildTokenizationRule() throws Exception {
        // 1. build user dictionary and rules .
        DictionaryTextSource userDicSource =
                new DictionaryTextSource(Paths.get(config.getProperty("dictionary.user.path")));

        // UserDictionaryRule --> CharacterTypeAndLengthLimitRule
        UserTrieBaseDictionary userDictionary = new UserTrieBaseDictionary(userDicSource);
        int tokenLengthLimit = Integer.parseInt(config.getProperty("tokenization.token.length_limit", "-1"));
        ChainedTokenizationRule rule = new CharacterTypeAndLengthLimitRule(tokenLengthLimit < 0 ? Integer.MAX_VALUE : tokenLengthLimit);
        rule = new UserDictionaryMatchRule(userDictionary, rule);

        return rule;
    }

    private TransitionMapBaseDictionary buildTransitionDictionary() throws Exception {
        DictionaryBinarySource transitionSource =
                new DictionaryBinarySource(Paths.get(config.getProperty("dictionary.transition.path")));
        return new TransitionMapBaseDictionary(transitionSource);
    }

    private ChainedAnalysisRule buildAnalysisRule() throws Exception {
        DictionaryBinarySource emissionSource =
                new DictionaryBinarySource(Paths.get(config.getProperty("dictionary.emission.path")));
        EmissionTrieBaseDictionary emissionDictionary = new EmissionTrieBaseDictionary(emissionSource);

        DictionaryTextSource fwdSource =
                new DictionaryTextSource(Paths.get(config.getProperty("dictionary.fwd.path")));
        FWDUserTrieBaseDictionary fwdDictionary = new FWDUserTrieBaseDictionary(fwdSource);

        // CanSkipRule --> FWDRule --> AllPossibleCandidateRule --> NARule
        return new CanSkipRule(transitionDictionary,
                new FWDRule(fwdDictionary, transitionDictionary,
                        new AllPossibleCandidatesRule(emissionDictionary, transitionDictionary,
                                new NAOrPrePosRule(transitionDictionary)
                        )
                )
        );
    }

    public Morphs doKlay(CharSequence text) {

        // 1. create tokenizer
        Tokenizer tokenizer = new Tokenizer(text, tokenizationRule);

        // 3. create start morph-seq and analysis Param
        MorphSequence startMSeq = new SingleMorphSequence(Morph.newStartMorph());
        AnalysisParam param = new AnalysisParam();
        param.setLastMSeq(startMSeq);

        // 4. analyze
        int tokenNumber = 0;
        while(tokenizer.hasNext()) {
            Token token = tokenizer.next();
            if(token.isWhiteSpace()) {
                tokenNumber++;
                continue;
            }

            param.set(tokenNumber++, text, token.getPos(), token.getStartPosition(), token.getEndPosition(), token.canSkipAnalysis());
            chainedAnalysisRule.apply(param);
        }

        // 5. connect lastMSeq -> endMSeq
        MorphSequence lastMSeq = param.lastMSeq();
        MorphSequence endMSeq = new SingleMorphSequence(Morph.newEndMorph());
        while(true) {
            endMSeq.compareScoreAndSetPreviousMSeq(lastMSeq, transitionDictionary);

            if(!lastMSeq.hasVPreviousMSeq()) break;
            lastMSeq = lastMSeq.getVPreviousMSeq();
        }

        // 6. create Morphs object lastly ...
        Morphs morphs = new Morphs(text);
        MorphSequence mSeq = endMSeq;
        while((mSeq = mSeq.getHPreviousMSeq()) != startMSeq) {
            morphs.addFirst(mSeq);
        }

        // 7. number morphs
        morphs.doNumbering();

        return morphs;
    }
}
