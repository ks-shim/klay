package klay.core.morphology.analysis.rule;

import klay.common.pos.Pos;
import klay.core.morphology.analysis.Morph;
import klay.core.morphology.analysis.rule.param.AnalysisParam;
import klay.core.morphology.analysis.sequence.MorphSequence;
import klay.core.morphology.analysis.sequence.SingleMorphSequence;
import klay.dictionary.mapbase.TransitionMapBaseDictionary;

public class NARule extends AbstractChainedAnalysisRule {

    private final TransitionMapBaseDictionary transitionDictionary;
    public NARule(TransitionMapBaseDictionary transitionDictionary) {
        super(null);
        this.transitionDictionary = transitionDictionary;
    }

    @Override
    public void apply(AnalysisParam param) {
        MorphSequence previousMSeq = param.lastMSeq();

        MorphSequence mSeq = new SingleMorphSequence(
                new Morph(param.getTokenNumber(), param.getSubCharSequence(), Pos.NA.label(), param.getFrom(), param.getTo()-1));
        while(true) {

            mSeq.compareScoreAndSetPreviousMSeq(previousMSeq, transitionDictionary);

            if(!previousMSeq.hasVPreviousMSeq()) break;

            previousMSeq = previousMSeq.getVPreviousMSeq();
        }

        param.setLastMSeq(mSeq);
    }
}
