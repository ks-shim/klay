package klay.core.morphology.analysis.rule;

import klay.common.pos.Pos;
import klay.core.morphology.analysis.Morph;
import klay.core.morphology.analysis.rule.param.AnalysisParam;
import klay.core.morphology.analysis.sequence.MorphSequence;
import klay.core.morphology.analysis.sequence.SingleMorphSequence;
import klay.dictionary.mapbase.TransitionMapBaseDictionary;
import org.apache.commons.lang3.StringUtils;

public class NAOrPrePosRule extends AbstractChainedAnalysisRule {

    private final TransitionMapBaseDictionary transitionDictionary;
    public NAOrPrePosRule(TransitionMapBaseDictionary transitionDictionary) {
        super(null);
        this.transitionDictionary = transitionDictionary;
    }

    @Override
    public void apply(AnalysisParam param) {
        MorphSequence previousMSeq = param.lastMSeq();

        CharSequence finalPos = StringUtils.isBlank(param.getPos()) ?  Pos.NA.label() : param.getPos();
        MorphSequence mSeq = new SingleMorphSequence(
                new Morph(param.getTokenNumber(), param.getSubCharSequence(),
                        finalPos, param.getFrom(), param.getTo()-1));
        while(true) {

            mSeq.compareScoreAndSetPreviousMSeq(previousMSeq, transitionDictionary);

            if(!previousMSeq.hasVPreviousMSeq()) break;

            previousMSeq = previousMSeq.getVPreviousMSeq();
        }

        param.setLastMSeq(mSeq);
    }
}
