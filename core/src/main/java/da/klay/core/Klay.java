package da.klay.core;

import da.klay.core.morphology.analysis.rule.param.AnalysisParam;
import da.klay.core.morphology.analysis.rule.AnalysisRule;
import da.klay.core.morphology.analysis.rule.CanSkipRule;
import da.klay.core.morphology.analysis.sequence.Morph;
import da.klay.core.morphology.analysis.rule.AllPossibleCandidatesRule;
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
import da.klay.dictionary.triebase.user.UserTrieBaseDictionary;
import org.apache.commons.lang3.time.StopWatch;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class Klay {

    private final ChainedTokenizationRule tokenizationRule;
    private final AnalysisRule analysisRule;
    private final TransitionMapBaseDictionary transitionDictionary;
    public Klay() throws Exception {

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
                new DictionaryTextSource(Paths.get("data/dictionary/text/user/dic.user"));

        UserTrieBaseDictionary userDictionary = new UserTrieBaseDictionary(userDicSource);
        ChainedTokenizationRule rule = new CharacterTypeAndLengthLimitRule(10);
        rule = new UserDictionaryMatchRule(userDictionary, rule);

        return rule;
    }

    private TransitionMapBaseDictionary buildTransitionDictionary() throws Exception {
        DictionaryBinarySource transitionSource =
                new DictionaryBinarySource(Paths.get("data/dictionary/binary/system/transition.bin"));
        return new TransitionMapBaseDictionary(transitionSource);
    }

    private AnalysisRule buildAnalysisRule() throws Exception {
        DictionaryBinarySource emissionSource =
                new DictionaryBinarySource(Paths.get("data/dictionary/binary/system/emission.bin"));
        EmissionTrieBaseDictionary emissionDictionary = new EmissionTrieBaseDictionary(emissionSource);

        return new CanSkipRule(
                new AllPossibleCandidatesRule(emissionDictionary, transitionDictionary),
                transitionDictionary);
    }

    public void doKlay(CharSequence text) throws Exception {

        // 1. create tokenizer
        Tokenizer tokenizer = new Tokenizer(text, tokenizationRule);

        // 3. create start morph-seq and analysis Param
        AnalysisParam param = new AnalysisParam();
        param.setLastMSeq(new SingleMorphSequence(Morph.newStartMorph()));

        // 4. analyze
        while(tokenizer.hasNext()) {
            Token token = tokenizer.next();

            param.set(text, token.getPos(), token.getStartPosition(), token.getEndPosition(), token.canSkipAnalysis());
            analysisRule.apply(param);
        }

        MorphSequence lastMSeq = param.lastMSeq();
        MorphSequence endMSeq = new SingleMorphSequence(Morph.newEndMorph());
        while(true) {
            endMSeq.compareScoreAndSetPreviousMSeq(lastMSeq, transitionDictionary);

            if(!lastMSeq.hasVPreviousMSeq()) break;
            lastMSeq = lastMSeq.getVPreviousMSeq();
        }

        System.out.println(endMSeq);
    }

    public static void main(String[] args) throws Exception {
        Klay klay = new Klay();
        klay.doKlay("대구일보는 나쁜사람입니다");
    }
}
