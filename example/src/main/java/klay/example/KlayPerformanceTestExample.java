package klay.example;

import klay.core.Klay;
import klay.core.morphology.analysis.Morphs;
import org.apache.commons.lang3.time.StopWatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class KlayPerformanceTestExample {

    public static void main(String[] args) throws Exception {

        String src = "data/performance/test.txt";
        Klay klay = new Klay(Paths.get("data/configuration/klay.conf"));

        StopWatch watch = new StopWatch();
        watch.start();
        int count = 0;
        try (BufferedReader in = new BufferedReader(new FileReader(src))) {
            String line = null;
            while((line = in.readLine()) != null) {
                line = line.trim();
                if(line.isEmpty()) continue;

                klay.doKlay(line);
                System.out.print("\r" + ++count);
            }
        }
        watch.stop();
        System.out.println("Analysis Time : " + watch.getTime(TimeUnit.MILLISECONDS) / 1000.0 + " (s)");
    }
}
// 36143 (ms)