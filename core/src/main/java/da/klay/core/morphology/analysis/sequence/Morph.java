package da.klay.core.morphology.analysis.sequence;

import da.klay.common.pos.Pos;

public class Morph {

    private Morph previous;
    private Morph next;

    private CharSequence text;
    private CharSequence pos;

    private long score;

    public Morph(CharSequence text,
                 CharSequence pos) {
        this.text = text;
        this.pos = pos;
    }

    public CharSequence getPos() {
        return pos;
    }

    public void setPrevious(Morph morph) {
        this.previous = morph;
    }

    public void setNext(Morph morph) {
        this.next = next;
    }

    public static Morph emptyMorph(Pos pos) {
        return new Morph(null, pos.label());
    }

    public static Morph newStartMorph() {
        return Morph.emptyMorph(Pos.START);
    }

    public static Morph newEndMorph() {
        return Morph.emptyMorph(Pos.END);
    }
}
