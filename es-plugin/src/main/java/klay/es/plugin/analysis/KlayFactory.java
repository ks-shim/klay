package klay.es.plugin.analysis;

import klay.core.Klay;

import java.util.List;
import java.util.Properties;

public final class KlayFactory {

    private static Klay klay;
    public synchronized final static Klay getInstance() {

        if(klay != null) return klay;

        Properties config = new Properties();
        config.put("dictionary.emission.path", "plugins/analysis-klay/dictionary/binary/system/emission.bin");
        config.put("dictionary.transition.path", "plugins/analysis-klay/dictionary/binary/system/transition.bin");
        config.put("dictionary.user.path", "plugins/analysis-klay/dictionary/text/user/dic.user");
        config.put("dictionary.fwd.path", "plugins/analysis-klay/dictionary/text/user/fwd.user");
        config.put("tokenization.token.length_limit", "-1");

        try {
            klay = new Klay(config);
            return klay;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
