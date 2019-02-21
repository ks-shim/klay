package da.klay.core.morphology.analysis.rule;

import da.klay.common.dictionary.structure.TrieResult;
import da.klay.core.morphology.analysis.sequence.Morph;
import da.klay.core.morphology.analysis.sequence.MorphSequence;
import da.klay.core.morphology.analysis.sequence.MultiMorphSequence;
import da.klay.dictionary.mapbase.TransitionMapBaseDictionary;
import da.klay.dictionary.param.DictionaryBinarySource;
import da.klay.dictionary.triebase.system.EmissionTrieBaseDictionary;

import java.nio.file.Paths;
import java.util.*;

public class AllPossibleCandidatesRule extends AbstractAnalysisRule {

    private final EmissionTrieBaseDictionary emissionDictionary;
    private final TransitionMapBaseDictionary transitionDictionary;
    public AllPossibleCandidatesRule(EmissionTrieBaseDictionary emissionDictionary,
                                     TransitionMapBaseDictionary transitionDictionary) {
        this.emissionDictionary = emissionDictionary;
        this.transitionDictionary = transitionDictionary;
    }

    public AllPossibleCandidatesRule(EmissionTrieBaseDictionary emissionDictionary,
                                     TransitionMapBaseDictionary transitionDictionary,
                                     AnalysisRule nextRule) {

        super(nextRule);
        this.emissionDictionary = emissionDictionary;
        this.transitionDictionary = transitionDictionary;
    }

    public void apply(AnalysisParam param) {

        // 1. initialize
        CharSequence jaso = param.jaso();
        int jasoLength = jaso.length();

        for(int i=0; i<jasoLength; i++) {

            MorphSequence currentMSeq = param.slotAt(i);
            if(i > 0 && currentMSeq == null) continue;

            TrieResult[] results = emissionDictionary.getAll(jaso, i);
            if(results == null && i == 0) break;
            else if(results == null) {
                param.removeSlot(i);
                continue;
            }

            assignSlotAndCalculateScore(i, results, param, currentMSeq);
        }

        MorphSequence lastMSeq = param.slotAt(jasoLength);
        if(lastMSeq != null) {
            param.setLastMSeq(lastMSeq);
            return;
        }
    }

    private void assignSlotAndCalculateScore(int currentJasoPos,
                                             TrieResult[] results,
                                             AnalysisParam param,
                                             MorphSequence currentMSeq) {

        for(TrieResult result : results) {
            if(!result.hasResult()) continue;

            int insertIndex = currentJasoPos + result.length();
            MorphSequence nextMSeq = param.slotAt(insertIndex);
            if(nextMSeq == null) nextMSeq = param.newSlotAt(insertIndex);

            nextMSeq = parseTrieResultAndCreateMSeqs(result.getData(), currentMSeq, nextMSeq);
            param.setSlotAt(insertIndex, nextMSeq);
        }
    }

    private MorphSequence parseTrieResultAndCreateMSeqs(CharSequence res,
                                               MorphSequence currentMSeq,
                                               MorphSequence nextMSeq) {

        MorphSequence vPreviousMSeq = nextMSeq;
        MorphSequence vNextMSeq = new MultiMorphSequence();

        // ex) 달/VV ㄴ/ETM:18	달/VA ㄴ/ETM:4
        int textStartIndex = 0;
        int slashIndex = 0;
        int colonIndex = 0;
        int resLength = res.length();
        for(int i=0; i<resLength; i++) {

            char ch = res.charAt(i);
            if(ch == '/') {
                slashIndex = i;
            } else if(ch == ' ') {
                CharSequence text = res.subSequence(textStartIndex, slashIndex);
                CharSequence pos = res.subSequence(slashIndex+1, i);
                textStartIndex = i+1;

                Morph morph = new Morph(text, pos);
                vNextMSeq.addMorph(morph);
            } else if (ch == ':') {
                CharSequence text = res.subSequence(textStartIndex, slashIndex);
                CharSequence pos = res.subSequence(slashIndex+1, i);
                colonIndex = i;

                Morph morph = new Morph(text, pos);
                vNextMSeq.addMorph(morph);
            } else if(ch == '\t') {
                textStartIndex = i+1;
                int emissionScore = Integer.parseInt((String)res.subSequence(colonIndex+1, i));
                vNextMSeq.setEmissionScore(emissionScore);
                calculateScore(currentMSeq, vNextMSeq);
                vNextMSeq.setVPreviousMSeq(vPreviousMSeq);
                vPreviousMSeq = vNextMSeq;

                vNextMSeq = new MultiMorphSequence();
            } else if(i == resLength-1) {
                int emissionScore = Integer.parseInt((String)res.subSequence(colonIndex+1, i+1));
                vNextMSeq.setEmissionScore(emissionScore);
                calculateScore(currentMSeq, vNextMSeq);
                vNextMSeq.setVPreviousMSeq(vPreviousMSeq);
                vPreviousMSeq = vNextMSeq;
            }
        }

        return vPreviousMSeq;
    }

    private void calculateScore(MorphSequence currentMSeq,
                                MorphSequence nextMSeq) {

        while(true) {
            nextMSeq.compareScoreAndSetPreviousMSeq(currentMSeq, transitionDictionary);

            if(!currentMSeq.hasVPreviousMSeq()) break;

            currentMSeq = currentMSeq.getVPreviousMSeq();
        }
    }

    public static void main(String[] args) throws Exception {
        EmissionTrieBaseDictionary emissionDictionary =
                new EmissionTrieBaseDictionary(
                        new DictionaryBinarySource(Paths.get("data/dictionary/binary/system/emission.bin")));

        TransitionMapBaseDictionary transitionDictionary =
                new TransitionMapBaseDictionary(
                        new DictionaryBinarySource(Paths.get("data/dictionary/binary/System/transition.bin")));

        AllPossibleCandidatesRule rule = new AllPossibleCandidatesRule(emissionDictionary, transitionDictionary);

        String text = "나쁜사람입니다";
        AnalysisParam param = new AnalysisParam();
        param.set(text, 0, text.length());
        rule.apply(param);

        //int end = jaso.length();
        //rule.print(candidates, end);
        /*for(int i=end; i>=0; i--) {
            CandidateNode candidate = candidates[i];
            if(candidate == null && i == end-1) break;
            else if(candidate == null) continue;

            while(candidate != null) {
                System.out.println(candidate.data);
                candidate = candidate.verticalPreNode;
            }
            System.out.println("-----------");
        }*/
        //System.out.println(Arrays.toString(candidates));
    }

}
