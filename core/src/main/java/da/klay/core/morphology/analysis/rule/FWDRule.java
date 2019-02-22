package da.klay.core.morphology.analysis.rule;

import da.klay.core.morphology.analysis.rule.param.AnalysisParam;
import da.klay.core.morphology.analysis.sequence.MorphSequence;
import da.klay.dictionary.triebase.user.FWDUserTrieBaseDictionary;

public class FWDRule extends AbstractAnalysisRule {

    private final FWDUserTrieBaseDictionary dictionary;
    public FWDRule(FWDUserTrieBaseDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public FWDRule(AnalysisRule nextRule,
                   FWDUserTrieBaseDictionary dictionary) {
        super(nextRule);
        this.dictionary = dictionary;
    }

    @Override
    public void apply(AnalysisParam param) {

        MorphSequence previousMSeq = param.lastMSeq();

        dictionary.getFully(param.getText(), param.getFrom(), param.getTo() - param.getFrom());
        super.apply(param);
    }
}
