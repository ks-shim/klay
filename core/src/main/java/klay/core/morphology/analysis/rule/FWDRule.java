package klay.core.morphology.analysis.rule;

import klay.common.dictionary.structure.Item;
import klay.common.dictionary.structure.ItemData;
import klay.core.morphology.analysis.Morph;
import klay.core.morphology.analysis.rule.param.AnalysisParam;
import klay.core.morphology.analysis.sequence.MorphSequence;
import klay.core.morphology.analysis.sequence.MultiMorphSequence;
import klay.dictionary.mapbase.TransitionMapBaseDictionary;
import klay.dictionary.triebase.user.FWDUserTrieBaseDictionary;

public class FWDRule extends AbstractChainedAnalysisRule {

    private final FWDUserTrieBaseDictionary fwdDictionary;
    private final TransitionMapBaseDictionary transitionDictionary;
    public FWDRule(FWDUserTrieBaseDictionary fwdDictionary,
                   TransitionMapBaseDictionary transitionDictionary) {
        this.fwdDictionary = fwdDictionary;
        this.transitionDictionary = transitionDictionary;
    }

    public FWDRule(FWDUserTrieBaseDictionary dictionary,
                   TransitionMapBaseDictionary transitionDictionary,
                   ChainedAnalysisRule nextRule) {
        super(nextRule);
        this.fwdDictionary = dictionary;
        this.transitionDictionary = transitionDictionary;
    }

    @Override
    public void apply(AnalysisParam param) {

        MorphSequence previousMSeq = param.lastMSeq();

        Item[] result = fwdDictionary.getFully(param.getText(), param.getFrom(), param.getKeyLength());
        if(result == null || result.length == 0) {
            super.apply(param);
            return;
        }

        MorphSequence lastMSeq = parseTrieResultAndCreateMSeqs(param, result, previousMSeq);
        param.setLastMSeq(lastMSeq);
    }

    private MorphSequence parseTrieResultAndCreateMSeqs(AnalysisParam param,
                                                        Item[] res,
                                                        MorphSequence previousMSeq) {
        MorphSequence currentMSeq = new MultiMorphSequence();

        // ex) 흘리/VV 었/EP 어요/EC
        int resLength = res.length;
        for(int i=0; i<resLength; i++) {
            Item item = res[i];
            int itemDataLength = item.size();

            for(int j=0; j<itemDataLength; j++) {
                ItemData itemData = item.getItemAt(j);
                currentMSeq.addMorph(new Morph(param.getTokenNumber(), itemData.getWord(), itemData.getPos(), param.getFrom(), param.getTo()-1));
            }
        }

        calculateScore(previousMSeq, currentMSeq);
        return currentMSeq;
    }

    private void calculateScore(MorphSequence previousMSeq,
                                MorphSequence currentMSeq) {

        while(true) {
            currentMSeq.compareScoreAndSetPreviousMSeq(previousMSeq, transitionDictionary);

            if(!previousMSeq.hasVPreviousMSeq()) break;

            previousMSeq = previousMSeq.getVPreviousMSeq();
        }
    }
}
