package klay.core.morphology.analysis.rule;

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
        int resultLength = results.length;
        for(int i=0; i<resultLength; i++) {
            TrieResult result = results[i];
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

        StringBuilder infoSb = param.getTextSb();
        infoSb.setLength(0);

        // ex) 달/VV ㄴ/ETM:18	달/VA ㄴ/ETM:4
        int resLength = res.length();
        for(int i=0; i<resLength; i++) {

            char ch = res.charAt(i);
            if(ch == '/') {
                infoSb = param.getPosSb();
                infoSb.setLength(0);
            } else if(ch == ' ') {
                CharSequence text = param.getTextSb().toString();
                CharSequence pos = param.getPosSb().toString();
                infoSb = param.getTextSb();
                infoSb.setLength(0);

                Morph morph = new Morph(param.getTokenNumber(), text, pos);
                vNextMSeq.addMorph(morph);
            } else if (ch == ':') {
                CharSequence text = param.getTextSb().toString();
                CharSequence pos = param.getPosSb().toString();
                infoSb = param.getScoreSb();
                infoSb.setLength(0);

                Morph morph = new Morph(param.getTokenNumber(), text, pos);
                vNextMSeq.addMorph(morph);
            } else if(ch == '\t') {
                double emissionScore = Double.parseDouble(infoSb.toString());
                vNextMSeq.setEmissionScore(emissionScore);
                calculateScore(currentMSeq, vNextMSeq);
                vNextMSeq.setVPreviousMSeq(vPreviousMSeq);
                vPreviousMSeq = vNextMSeq;

                infoSb = param.getTextSb();
                infoSb.setLength(0);

                vNextMSeq = new MultiMorphSequence();
            } else if(i == resLength-1) {
                infoSb.append(ch);
                double emissionScore = Double.parseDouble(infoSb.toString());
                vNextMSeq.setEmissionScore(emissionScore);
                calculateScore(currentMSeq, vNextMSeq);
                vNextMSeq.setVPreviousMSeq(vPreviousMSeq);
                vPreviousMSeq = vNextMSeq;
            } else {
                infoSb.append(ch);
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
