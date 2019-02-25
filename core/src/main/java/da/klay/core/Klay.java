package da.klay.core;

import da.klay.core.morphology.analysis.Morphs;
import da.klay.core.morphology.analysis.rule.*;
import da.klay.core.morphology.analysis.rule.param.AnalysisParam;
import da.klay.core.morphology.analysis.Morph;
import da.klay.core.morphology.analysis.sequence.MorphSequence;
import da.klay.core.morphology.analysis.sequence.SingleMorphSequence;
import da.klay.core.tokenization.Token;
import da.klay.core.tokenization.Tokenizer;
import da.klay.core.tokenization.rule.ChainedTokenizationRule;
import da.klay.core.tokenization.rule.CharacterTypeAndLengthLimitRule;
import da.klay.core.tokenization.rule.UserDictionaryMatchRule;
import da.klay.dictionary.mapbase.TransitionMapBaseDictionary;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.param.DictionaryTextSource;
import da.klay.dictionary.triebase.system.EmissionTrieBaseDictionary;
import da.klay.dictionary.triebase.user.FWDUserTrieBaseDictionary;
import da.klay.dictionary.triebase.user.UserTrieBaseDictionary;
import org.apache.commons.lang3.time.StopWatch;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

// TODO : 1. 특수문자 pos 맞추기
public class Klay {

    private final ChainedTokenizationRule tokenizationRule;
    private final AnalysisRule analysisRule;
    private final TransitionMapBaseDictionary transitionDictionary;

    private final Properties config;
    public Klay(Path configFilePath) throws Exception {

        config = new Properties();
        config.load(Files.newInputStream(configFilePath));

        StopWatch watch = new StopWatch();
        watch.start();

        this.transitionDictionary = buildTransitionDictionary();
        this.tokenizationRule = buildTokenizationRule();
        this.analysisRule = buildAnalysisRule();

        watch.stop();
        System.out.println("Dictionary Loading Time : " + watch.getTime(TimeUnit.MILLISECONDS) + " (ms)");
    }

    private ChainedTokenizationRule buildTokenizationRule() throws Exception {
        // 1. build user dictionary and rules .
        DictionaryTextSource userDicSource =
                new DictionaryTextSource(Paths.get(config.getProperty("dictionary.user.path")));

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

    private AnalysisRule buildAnalysisRule() throws Exception {
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
                                new NARule(transitionDictionary)
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

            param.set(tokenNumber++, text, token.getPos(), token.getStartPosition(), token.getEndPosition(), token.canSkipAnalysis());
            analysisRule.apply(param);
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

    public static void main(String[] args) throws Exception {
        Klay klay = new Klay(Paths.get("data/configuration/klay.conf"));
        Morphs morphs = klay.doKlay("나는 dwayne입니다.");
        Iterator<Morph> iter = morphs.iterator();
        while(iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
