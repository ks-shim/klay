package da.klay.core.morphology.analysis.sequence;

import da.klay.core.morphology.analysis.Morph;

public class SingleMorphSequence extends AbstractMorphSequence {

    private final Morph morph;

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
