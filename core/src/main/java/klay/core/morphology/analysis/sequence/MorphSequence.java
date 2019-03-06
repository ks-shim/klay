package klay.core.morphology.analysis.sequence;

import klay.core.morphology.analysis.Morph;
import klay.dictionary.mapbase.TransitionMapBaseDictionary;

public interface MorphSequence {

    void addMorph(Morph morph);

    Morph first();

    Morph last();

    double score();

    void setEmissionScore(double score);

    void compareScoreAndSetPreviousMSeq(MorphSequence newPreMorphSequence,
                                        TransitionMapBaseDictionary dictionary);

    boolean hasHPreviousMSeq();

    boolean hasVNextMSeq();

    boolean hasVPreviousMSeq();

    MorphSequence getHPreviousMSeq();

    MorphSequence setVNextMSeq(MorphSequence nextMSeq);

    MorphSequence setVPreviousMSeq(MorphSequence previousMSeq);

    MorphSequence getVNextMSeq();

    MorphSequence getVPreviousMSeq();
}
