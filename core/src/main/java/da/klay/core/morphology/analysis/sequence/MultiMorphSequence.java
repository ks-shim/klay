package da.klay.core.morphology.analysis.sequence;

import lombok.Data;

@Data
public class MultiMorphSequence extends AbstractMorphSequence {

    private Morph first;
    private Morph last;

    public MultiMorphSequence() {}

    public MultiMorphSequence(Morph morph) {
        this.first = morph;
        this.last = morph;
    }

    @Override
    public void addMorph(Morph morph) {
        if(first == null) this.first = morph;

        if(last != null) {
            morph.setPrevious(last);
            last.setNext(morph);
        }

        this.last = morph;
    }

    @Override
    public Morph first() {
        return first;
    }

    @Override
    public Morph last() {
        return last;
    }
}
