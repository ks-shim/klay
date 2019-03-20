package klay.python.wrapper;

import klay.core.Klay;
import klay.core.morphology.analysis.Morphs;

import java.nio.file.Paths;

public class KlayEntryPoint {

    private final Klay klay;

    public KlayEntryPoint(String configFilePath) {
        try {
            klay = new Klay(Paths.get(configFilePath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Morphs analyze(CharSequence text) {
        return klay.doKlay(text);
    }
}
