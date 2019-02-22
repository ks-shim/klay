package da.klay.core.morphology.analysis.rule;

import da.klay.common.dictionary.structure.TrieResult;
import da.klay.core.morphology.analysis.rule.param.AnalysisParam;
import da.klay.core.morphology.analysis.Morph;
import da.klay.core.morphology.analysis.sequence.MorphSequence;
import da.klay.core.morphology.analysis.sequence.MultiMorphSequence;
import da.klay.dictionary.mapbase.TransitionMapBaseDictionary;
import da.klay.dictionary.triebase.system.EmissionTrieBaseDictionary;

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

            MorphSequence currentMSeq = (i == 0) ? param.lastMSeq() : param.slotAt(i);
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
        if(lastMSeq == null) {
            super.apply(param);
            return;
        }

        param.setLastMSeq(lastMSeq);
    }

    private void assignSlotAndCalculateScore(int currentJasoPos,
                                             TrieResult[] results,
                                             AnalysisParam param,
                                             MorphSequence currentMSeq) {

        for(TrieResult result : results) {
            if(!result.hasResult()) continue;

            int insertIndex = currentJasoPos + result.length();
            MorphSequence nextMSeq = param.slotAt(insertIndex);
            nextMSeq = parseTrieResultAndCreateMSeqs(param, result.getData(), currentMSeq, nextMSeq);

            param.setSlotAt(insertIndex, nextMSeq);
        }
    }

    private MorphSequence parseTrieResultAndCreateMSeqs(AnalysisParam param,
                                                        CharSequence res,
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

                Morph morph = new Morph(param.getTokenNumber(), text, pos);
                vNextMSeq.addMorph(morph);
            } else if (ch == ':') {
                CharSequence text = res.subSequence(textStartIndex, slashIndex);
                CharSequence pos = res.subSequence(slashIndex+1, i);
                colonIndex = i;

                Morph morph = new Morph(param.getTokenNumber(), text, pos);
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
}
