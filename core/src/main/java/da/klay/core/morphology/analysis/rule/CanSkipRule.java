package da.klay.core.morphology.analysis.rule;

import da.klay.core.morphology.analysis.sequence.Morph;
import da.klay.core.morphology.analysis.sequence.MorphSequence;
import da.klay.core.morphology.analysis.sequence.SingleMorphSequence;

public class CanSkipRule extends AbstractAnalysisRule {

    public CanSkipRule() {}

    public CanSkipRule(AnalysisRule nextRule) {
        super(nextRule);
    }

    @Override
    public void apply(AnalysisParam param) {
        if(!param.canSkip()) {
            super.apply(param);
            return;
        }

        MorphSequence mSeq = new SingleMorphSequence(new Morph(param.getSubCharSequence(), param.getPos()));
        mSeq.compareScoreAndSetPreviousMSeq();
    }
}
