package klay.core.morphology.analysis.rule;

import klay.common.dictionary.structure.Item;
import klay.common.dictionary.structure.ItemData;
import klay.common.dictionary.structure.TrieResult;
import klay.core.morphology.analysis.Morph;
import klay.core.morphology.analysis.rule.param.AnalysisParam;
import klay.core.morphology.analysis.sequence.MorphSequence;
import klay.core.morphology.analysis.sequence.MultiMorphSequence;
import klay.dictionary.mapbase.TransitionMapBaseDictionary;
import klay.dictionary.triebase.system.EmissionTrieBaseDictionary;

public class AllPossibleCandidatesRule extends AbstractChainedAnalysisRule {

    private final EmissionTrieBaseDictionary emissionDictionary;
    private final TransitionMapBaseDictionary transitionDictionary;
    public AllPossibleCandidatesRule(EmissionTrieBaseDictionary emissionDictionary,
                                     TransitionMapBaseDictionary transitionDictionary) {
        this.emissionDictionary = emissionDictionary;
        this.transitionDictionary = transitionDictionary;
    }

    public AllPossibleCandidatesRule(EmissionTrieBaseDictionary emissionDictionary,
                                     TransitionMapBaseDictionary transitionDictionary,
                                     ChainedAnalysisRule nextRule) {

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

            TrieResult<Item[]>[] results = emissionDictionary.getAll(jaso, i);
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
                                             TrieResult<Item[]>[] results,
                                             AnalysisParam param,
                                             MorphSequence currentMSeq) {

        int startSyllableIndex = param.getSyllableOffsetAt(currentJasoPos);

        int resultLength = results.length;
        for(int i=0; i<resultLength; i++) {
            TrieResult<Item[]> result = results[i];
            if(!result.hasResult()) continue;

            int insertIndex = currentJasoPos + result.length();
            int endSyllableIndex = param.getSyllableOffsetAt(insertIndex-1);
            MorphSequence nextMSeq = param.slotAt(insertIndex);
            nextMSeq = parseTrieResultAndCreateMSeqs(param, result.getData(), currentMSeq, nextMSeq, startSyllableIndex, endSyllableIndex);

            param.setSlotAt(insertIndex, nextMSeq);
        }
    }

    private MorphSequence parseTrieResultAndCreateMSeqs(AnalysisParam param,
                                                        Item[] res,
                                                        MorphSequence currentMSeq,
                                                        MorphSequence nextMSeq,
                                                        int startSyllableIndex,
                                                        int endSyllableIndex) {

        MorphSequence vPreviousMSeq = nextMSeq;
        MorphSequence vNextMSeq = new MultiMorphSequence();

        // ex) 달/VV ㄴ/ETM:18	달/VA ㄴ/ETM:4
        int resLength = res.length;
        for(int i=0; i<resLength; i++) {

            Item item = res[i];
            int itemDataLength = item.size();
            for(int j=0; j<itemDataLength; j++) {
                ItemData itemData = item.getItemAt(j);
                vNextMSeq.addMorph(new Morph(param.getTokenNumber(), itemData.getWord(), itemData.getPos(), startSyllableIndex, endSyllableIndex));
            }

            vNextMSeq.setEmissionScore(item.getScore());
            calculateScore(currentMSeq, vNextMSeq);
            vNextMSeq.setVPreviousMSeq(vPreviousMSeq);
            vPreviousMSeq = vNextMSeq;

            if(i < resLength-1) vNextMSeq = new MultiMorphSequence();
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
