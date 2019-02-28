package da.klay.example;

import da.klay.core.Klay;
import da.klay.core.morphology.analysis.Morph;
import da.klay.core.morphology.analysis.Morphs;
import org.apache.commons.lang3.time.StopWatch;

import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class KlayExample {

    public static void main(String[] args) throws Exception {

        //***********************************************************************
        // 1. configuration and creating Klay object ...
        //***********************************************************************
        Klay klay = new Klay(Paths.get("data/configuration/klay.conf"));

        //***********************************************************************
        // 2. start morphological analysis.
        //***********************************************************************
        String text = "><?/!@#$%^&*()[]{}\\|+=";

        StopWatch watch = new StopWatch();
        watch.start();
        Morphs morphs = klay.doKlay(text);
        watch.stop();
        System.out.println("Analysis Time : " + watch.getTime(TimeUnit.MILLISECONDS) + " (ms)");

        //***********************************************************************
        // 3. print result.
        //***********************************************************************
        System.out.println("\nTEXT : " + text);
        System.out.println("-----------------------------------------------------------\n");
        Iterator<Morph> iter = morphs.iterator();
        while(iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
