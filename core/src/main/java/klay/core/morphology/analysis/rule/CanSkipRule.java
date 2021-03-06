package klay.core.morphology.analysis.rule;

import klay.core.morphology.analysis.Morph;
import klay.core.morphology.analysis.rule.param.AnalysisParam;
import klay.core.morphology.analysis.sequence.MorphSequence;
import klay.core.morphology.analysis.sequence.SingleMorphSequence;
import klay.dictionary.mapbase.TransitionMapBaseDictionary;

public class CanSkipRule extends AbstractChainedAnalysisRule {

    private final TransitionMapBaseDictionary transitionDictionary;
    public CanSkipRule(TransitionMapBaseDictionary transitionDictionary) {
        this.transitionDictionary = transitionDictionary;
    }

    public CanSkipRule(TransitionMapBaseDictionary transitionDictionary,
                       ChainedAnalysisRule nextRule) {
        super(nextRule);
        this.transitionDictionary = transitionDictionary;
    }

    @Override
    public void apply(AnalysisParam param) {
        if(!param.canSkip()) {
            super.apply(param);
            return;
        }

        MorphSequence previousMSeq = param.lastMSeq();

        MorphSequence mSeq = new SingleMorphSequence(
                new Morph(param.getTokenNumber(), param.getSubCharSequence(), param.getPos(), param.getFrom(), param.getTo()-1));

        while(true) {
            mSeq.compareScoreAndSetPreviousMSeq(previousMSeq, transitionDictionary);
            if(!previousMSeq.hasVPreviousMSeq()) break;
            previousMSeq = previousMSeq.getVPreviousMSeq();
        }

        param.setLastMSeq(mSeq);
    }
}
