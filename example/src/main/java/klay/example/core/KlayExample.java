package klay.example.core;

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
        long startMem = Runtime.getRuntime().freeMemory();
        Klay klay = new Klay(Paths.get("data/configuration/klay.conf"));
        long endMem = Runtime.getRuntime().freeMemory();
        System.out.println((startMem - endMem)/(1024.0 * 1024.0) + " (MB)");

        //***********************************************************************
        // 2. start morphological analysis.
        //***********************************************************************
        String text = "ㄱㅐOOO같은영화 뭐가무섭다는건지ㅡㅡ";

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
