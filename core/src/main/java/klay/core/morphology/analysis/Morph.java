package klay.core.morphology.analysis;

import klay.common.pos.Pos;

public class Morph {

    private int tokenNumber;
    private int morphNumber;

    private Morph previous;
    private Morph next;

    private CharSequence text;
    private CharSequence pos;

    private long score;

    public Morph(int tokenNumber,
                 CharSequence text,
                 CharSequence pos) {
        this.tokenNumber = tokenNumber;
        this.text = text;
        this.pos = pos;
    }

    public int getTokenNumber() {
        return tokenNumber;
    }

    public void setMorphNumber(int number) {
        morphNumber = number;
    }

    public int getMorphNumber() {
        return morphNumber;
    }

    public CharSequence getText() {
        return text;
    }

    public Morph getPrevious() {
        return previous;
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

    public static Morph emptyMorph(int tokenNumber, Pos pos) {
        return new Morph(tokenNumber, null, pos.label());
    }

    public static Morph newStartMorph() {
        return Morph.emptyMorph(-1, Pos.START);
    }

    public static Morph newEndMorph() {
        return Morph.emptyMorph(-1, Pos.END);
    }

    @Override
    public String toString() {
        return "[TOKEN : " + tokenNumber + "] - [MORPH : " + morphNumber + "] --> " + text + "/" + pos;
    }
}
