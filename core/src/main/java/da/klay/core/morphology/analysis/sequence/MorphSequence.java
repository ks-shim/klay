package da.klay.core.morphology.analysis.sequence;

import da.klay.dictionary.mapbase.TransitionMapBaseDictionary;

public interface MorphSequence {

    void addMorph(Morph morph);

    Morph first();

    Morph last();

    long score();

    void setEmissionScore(int score);

    void compareScoreAndSetPreviousMSeq(MorphSequence newPreMorphSequence,
                                        TransitionMapBaseDictionary dictionary);
    boolean hasHPreviousMSeq();

    boolean hasVNextMSeq();

    boolean hasVPreviousMSeq();

    MorphSequence setVNextMSeq(MorphSequence nextMSeq);

    MorphSequence setVPreviousMSeq(MorphSequence previousMSeq);

    MorphSequence getVNextMSeq();

    MorphSequence getVPreviousMSeq();
}
