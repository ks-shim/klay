package da.klay.core.morphology.analysis.sequence;

import da.klay.dictionary.mapbase.TransitionMapBaseDictionary;
import lombok.Data;

import java.util.Map;

public abstract class AbstractMorphSequence implements MorphSequence {

    protected MorphSequence vNextMSeq;
    protected MorphSequence vPreviousMSeq;

    protected MorphSequence hPreviousMSeq;

    protected int emissionScore;
    protected long score;

    protected AbstractMorphSequence() {}

    @Override
    public long score() {
        return score;
    }

    @Override
    public void setEmissionScore(int score) {
        this.emissionScore = score;
    }

    @Override
    public void compareScoreAndSetPreviousMSeq(MorphSequence newPreviousMSeq,
                                               TransitionMapBaseDictionary dictionary) {
        Map<CharSequence, Integer> transitionMap = dictionary.getFully(newPreviousMSeq.last().getPos());
        Integer transitionScore;
        if(transitionMap == null || (transitionScore = transitionMap.get(first().getPos())) == null) transitionScore = -1;

        long newTotalScore = newPreviousMSeq.score() + transitionScore + emissionScore;
        if(newTotalScore < score) return;

        score = newTotalScore;
        hPreviousMSeq = newPreviousMSeq;
    }

    @Override
    public boolean hasVNextMSeq() {
        return vNextMSeq != null;
    }

    @Override
    public MorphSequence setVNextMSeq(MorphSequence vNextMSeq) {
        this.vNextMSeq = vNextMSeq;
        return this.vNextMSeq;
    }

    @Override
    public MorphSequence getVNextMSeq() {
        return vNextMSeq;
    }

    @Override
    public boolean hasVPreviousMSeq() {
        return vPreviousMSeq != null;
    }

    @Override
    public MorphSequence setVPreviousMSeq(MorphSequence vPreviousMSeq) {
        this.vPreviousMSeq = vPreviousMSeq;
        return this.vPreviousMSeq;
    }

    @Override
    public MorphSequence getVPreviousMSeq() {
        return vPreviousMSeq;
    }
}
