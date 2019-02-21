package da.klay.core.morphology.analysis.sequence;

import lombok.Data;

@Data
public class SingleMorphSequence extends AbstractMorphSequence {

    private MorphSequence preMorphSequence;

    private final Morph morph;

    private int emissionScore;
    private long score;

    public SingleMorphSequence(Morph morph) {
        this.morph = morph;
    }

    @Override
    public void addMorph(Morph morph) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Morph first() {
        return morph;
    }

    @Override
    public Morph last() {
        return morph;
    }
}
