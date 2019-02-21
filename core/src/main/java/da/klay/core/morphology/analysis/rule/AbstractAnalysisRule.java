package da.klay.core.morphology.analysis.rule;

import da.klay.core.morphology.analysis.rule.param.AnalysisParam;

public abstract class AbstractAnalysisRule implements AnalysisRule {

    protected AnalysisRule nextRule;

    protected AbstractAnalysisRule() {}

    protected AbstractAnalysisRule(AnalysisRule nextRule) {
        this.nextRule = nextRule;
    }

    @Override
    public void apply(AnalysisParam param) {
        if(this.nextRule != null) this.nextRule.apply(param);
    }
}
