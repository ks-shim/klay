package da.klay.core.morphology.analysis.rule;

import da.klay.core.morphology.analysis.rule.param.AnalysisParam;

public interface ChainedAnalysisRule {

    void apply(AnalysisParam param);
}
