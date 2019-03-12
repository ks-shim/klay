package klay.core.morphology.analysis.rule.param;

import klay.common.parser.JasoParser;
import klay.core.morphology.analysis.sequence.MorphSequence;
import klay.core.morphology.analysis.sequence.MultiMorphSequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisParam {

    private int tokenNumber;
    private CharSequence text;
    private CharSequence pos;
    private int from;
    private int to;
    private boolean canSkip;

    private CharSequence jaso;
    private List<Integer> jasoToSyllablePosition;

    private Map<Integer, MorphSequence> candidateMSeqSlot;

    private MorphSequence lastMSeq;

    public AnalysisParam() {
        candidateMSeqSlot = new HashMap<>();
        jasoToSyllablePosition = new ArrayList<>();
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
        if(jaso != null) return jaso;

        int preSbLength = 0;
        StringBuilder sb = new StringBuilder();
        for(int i=from; i<to; ++i) {
            char ch = text.charAt(i);
            JasoParser.parseCharAsString(ch, sb);

            int curSbLength = sb.length() - preSbLength;
            if(curSbLength == 0) continue;

            for(int s=0; s<curSbLength; ++s) {
                jasoToSyllablePosition.add(i);
            }

            preSbLength = sb.length();
        }

        jaso = sb.toString();
        return jaso;
    }

    public void clearJasoAndSlot() {
        jaso = null;
        candidateMSeqSlot.clear();
        jasoToSyllablePosition.clear();
    }

    public MorphSequence slotAt(int index) {
        return candidateMSeqSlot.get(index);
    }

    public void removeSlot(int index) {
        candidateMSeqSlot.remove(index);
    }

    public void setSlotAt(int index, MorphSequence mSeq) {
        candidateMSeqSlot.put(index, mSeq);
    }

}
