package klay.example;

import klay.core.Klay;
import klay.core.morphology.analysis.Morph;
import klay.core.morphology.analysis.Morphs;
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
        String text = "너무기대안하고갔나....................재밌게봤다";

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
