package klay.core.morphology.analysis.rule;

import klay.core.morphology.analysis.rule.param.AnalysisParam;

public interface ChainedAnalysisRule {

    void apply(AnalysisParam param);
}
