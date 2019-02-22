package da.klay.core.morphology.analysis.rule;

import da.klay.core.morphology.analysis.rule.param.AnalysisParam;
import da.klay.core.morphology.analysis.sequence.Morph;
import da.klay.core.morphology.analysis.sequence.MorphSequence;
import da.klay.core.morphology.analysis.sequence.MultiMorphSequence;
import da.klay.dictionary.mapbase.TransitionMapBaseDictionary;
import da.klay.dictionary.triebase.user.FWDUserTrieBaseDictionary;

public class FWDRule extends AbstractAnalysisRule {

    private final FWDUserTrieBaseDictionary fwdDictionary;
    private final TransitionMapBaseDictionary transitionDictionary;
    public FWDRule(FWDUserTrieBaseDictionary fwdDictionary,
                   TransitionMapBaseDictionary transitionDictionary) {
        this.fwdDictionary = fwdDictionary;
        this.transitionDictionary = transitionDictionary;
    }

    public FWDRule(FWDUserTrieBaseDictionary dictionary,
                   TransitionMapBaseDictionary transitionDictionary,
                   AnalysisRule nextRule) {
        super(nextRule);
        this.fwdDictionary = dictionary;
        this.transitionDictionary = transitionDictionary;
    }

    @Override
    public void apply(AnalysisParam param) {

        MorphSequence previousMSeq = param.lastMSeq();

        CharSequence result = fwdDictionary.getFully(param.getText(), param.getFrom(), param.getKeyLength());
        if(result == null) {
            super.apply(param);
            return;
        }

        MorphSequence lastMSeq = parseTrieResultAndCreateMSeqs(result, previousMSeq);
        param.setLastMSeq(lastMSeq);
    }

    private MorphSequence parseTrieResultAndCreateMSeqs(CharSequence res,
                                                        MorphSequence previousMSeq) {

        MorphSequence currentMSeq = new MultiMorphSequence();

        // ex) 흘리/VV 었/EP 어요/EC
        int textStartIndex = 0;
        int slashIndex = 0;
        int resLength = res.length();
        for(int i=0; i<resLength; i++) {

            char ch = res.charAt(i);
            if(ch == '/') {
                slashIndex = i;
            } else if(ch == ' ') {
                CharSequence text = res.subSequence(textStartIndex, slashIndex);
                CharSequence pos = res.subSequence(slashIndex+1, i);
                textStartIndex = i+1;

                Morph morph = new Morph(text, pos);
                currentMSeq.addMorph(morph);
            } else if(i == resLength - 1) {
                CharSequence text = res.subSequence(textStartIndex, slashIndex);
                CharSequence pos = res.subSequence(slashIndex+1, i+1);

                Morph morph = new Morph(text, pos);
                currentMSeq.addMorph(morph);
            }
        }

        calculateScore(previousMSeq, currentMSeq);

        return currentMSeq;
    }

    private void calculateScore(MorphSequence previousMSeq,
                                MorphSequence currentMSeq) {

        while(true) {
            currentMSeq.compareScoreAndSetPreviousMSeq(previousMSeq, transitionDictionary);

            if(!previousMSeq.hasVPreviousMSeq()) break;

            previousMSeq = previousMSeq.getVPreviousMSeq();
        }
    }
}
