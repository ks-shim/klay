package da.klay.core.morphology.analysis.sequence;

import da.klay.dictionary.mapbase.TransitionMapBaseDictionary;
import lombok.Data;

import java.util.Map;

public abstract class AbstractMorphSequence implements MorphSequence {

    protected MorphSequence vNextMSeq;
    protected MorphSequence vPreviousMSeq;

    protected MorphSequence hPreviousMSeq;

    protected double emissionScore;
    protected double score;

    protected AbstractMorphSequence() {}

    @Override
    public boolean hasHPreviousMSeq() {
        return hPreviousMSeq != null;
    }

    @Override
    public double score() {
        return score;
    }

    @Override
    public void setEmissionScore(double score) {
        this.emissionScore = score;
    }

    @Override
    public void compareScoreAndSetPreviousMSeq(MorphSequence newPreviousMSeq,
                                               TransitionMapBaseDictionary dictionary) {
        Map<CharSequence, Double> transitionMap = dictionary.getFully(newPreviousMSeq.last().getPos());
        Double transitionScore;
        if(transitionMap == null || (transitionScore = transitionMap.get(first().getPos())) == null) transitionScore = -1.0;

        double newTotalScore = newPreviousMSeq.score() + transitionScore + emissionScore;
        if(newTotalScore < score && hasHPreviousMSeq()) return;

        System.out.println(first() + " : " + newPreviousMSeq.score() + " : " + transitionScore + " : " + emissionScore + " : " + newTotalScore);
        score = newTotalScore;
        hPreviousMSeq = newPreviousMSeq;
    }

    @Override
    public boolean hasVNextMSeq() {
        return vNextMSeq != null;
    }

    @Override
    public MorphSequence getHPreviousMSeq() {
        return hPreviousMSeq;
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
