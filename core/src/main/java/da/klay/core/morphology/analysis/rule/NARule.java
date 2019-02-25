package da.klay.core.morphology.analysis.rule;

import da.klay.common.pos.Pos;
import da.klay.core.morphology.analysis.rule.param.AnalysisParam;
import da.klay.core.morphology.analysis.Morph;
import da.klay.core.morphology.analysis.sequence.MorphSequence;
import da.klay.core.morphology.analysis.sequence.SingleMorphSequence;
import da.klay.dictionary.mapbase.TransitionMapBaseDictionary;

public class NARule extends AbstractAnalysisRule {

    private final TransitionMapBaseDictionary transitionDictionary;
    public NARule(TransitionMapBaseDictionary transitionDictionary) {
        super(null);
        this.transitionDictionary = transitionDictionary;
    }

    @Override
    public void apply(AnalysisParam param) {
        MorphSequence previousMSeq = param.lastMSeq();

        MorphSequence mSeq = new SingleMorphSequence(
                new Morph(param.getTokenNumber(), param.getSubCharSequence(), Pos.NA.label()));
        while(true) {

            mSeq.compareScoreAndSetPreviousMSeq(previousMSeq, transitionDictionary);

            if(!previousMSeq.hasVPreviousMSeq()) break;

            previousMSeq = previousMSeq.getVPreviousMSeq();
        }

        param.setLastMSeq(mSeq);
    }
}
