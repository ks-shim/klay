package da.klay.core.morphology.analysis.rule;

import da.klay.core.morphology.analysis.rule.param.AnalysisParam;

public abstract class AbstractChainedAnalysisRule implements ChainedAnalysisRule {

    protected ChainedAnalysisRule nextRule;

    protected AbstractChainedAnalysisRule() {}

    protected AbstractChainedAnalysisRule(ChainedAnalysisRule nextRule) {
        this.nextRule = nextRule;
    }

    @Override
    public void apply(AnalysisParam param) {
        if(this.nextRule != null) this.nextRule.apply(param);
    }
}
