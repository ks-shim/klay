package klay.core.morphology.analysis;

import klay.core.morphology.analysis.sequence.MorphSequence;
import lombok.Data;
import lombok.ToString;

import java.util.Iterator;
import java.util.LinkedList;

@Data
@ToString
public class Morphs {

    private CharSequence text;
    private LinkedList<Morph> morphs;

    public Morphs(CharSequence text) {
        this.text = text;
        morphs = new LinkedList<>();
    }

    public void addFirst(MorphSequence mSeq) {

        Morph morph = mSeq.last();
        while(true) {
            if(morph == null) break;

            addFirst(morph);

            morph = morph.getPrevious();
        }
    }

    public void doNumbering() {
        int number = 0;
        Iterator<Morph> iter = morphs.iterator();
        while(iter.hasNext())
            iter.next().setMorphNumber(number++);
    }

    public void addFirst(Morph morph) {
        morphs.addFirst(morph);
    }

    public Iterator<Morph> iterator() {
        return morphs.iterator();
    }
}
