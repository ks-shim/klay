package da.klay.core.morphology.analysis;

import lombok.Data;

@Data
public class AnalysisUnit {

    private AnalysisUnit preUnit;

    public AnalysisUnit() {
        preUnit = null;
    }

    public AnalysisUnit(AnalysisUnit preUnit) {
        this.preUnit = preUnit;
    }
}
