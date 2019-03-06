package klay.core.morphology.analysis.rule.param;

import klay.common.parser.JasoParser;
import klay.core.morphology.analysis.sequence.MorphSequence;
import klay.core.morphology.analysis.sequence.MultiMorphSequence;

import java.util.HashMap;
import java.util.Map;

public class AnalysisParam {

    private int tokenNumber;
    private CharSequence text;
    private CharSequence pos;
    private int from;
    private int to;
    private boolean canSkip;

    private CharSequence jaso;

    private Map<Integer, MorphSequence> candidateMSeqSlot;

    private MorphSequence lastMSeq;

    public AnalysisParam() {
        candidateMSeqSlot = new HashMap<>();
    }

    public CharSequence getText() {
        return text;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getKeyLength() {
        return to - from;
    }

    public void set(int tokenNumber,
                    CharSequence text,
                    CharSequence pos,
                    int from, int to, boolean canSkip) {
        this.tokenNumber = tokenNumber;
        this.text = text;
        this.pos = pos;
        this.from = from;
        this.to = to;
        this.canSkip = canSkip;

        clearJasoAndSlot();
    }

    public int getTokenNumber() {
        return tokenNumber;
    }

    public CharSequence getSubCharSequence() {
        return text.subSequence(from, to);
    }

    public CharSequence getPos() {
        return pos;
    }

    public boolean canSkip() {
        return canSkip;
    }

    public MorphSequence lastMSeq() {
        return lastMSeq;
    }

    public void setLastMSeq(MorphSequence lastMSeq) {
        this.lastMSeq = lastMSeq;
    }

    public void clearLastMSeq() {
        lastMSeq = null;
    }

    //**************************************************************************
    // Jaso and slot related methods ...
    //**************************************************************************
    public CharSequence jaso() {
        if(jaso == null) jaso = JasoParser.parseAsString(text, from, to);
        return jaso;
    }

    public void clearJasoAndSlot() {
        jaso = null;
        candidateMSeqSlot.clear();
    }

    public void clearSlot() {
        candidateMSeqSlot.clear();
    }

    public MorphSequence slotAt(int index) {
        return candidateMSeqSlot.get(index);
    }

    public void printSlot() {
        System.out.println(candidateMSeqSlot);
    }

    public void removeSlot(int index) {
        candidateMSeqSlot.remove(index);
    }

    public MorphSequence newSlotAt(int index) {
        MorphSequence mSeq = new MultiMorphSequence();
        candidateMSeqSlot.put(index, mSeq);
        return mSeq;
    }

    public void setSlotAt(int index, MorphSequence mSeq) {
        candidateMSeqSlot.put(index, mSeq);
    }

}
