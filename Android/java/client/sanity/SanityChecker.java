package com.alexronnegi.client.sanity;

import com.alexronnegi.serialization.Gpx;
import com.alexronnegi.serialization.Wpt;
import java.io.File;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class SanityChecker {

    public static final String GPX_DIRECTORY = "gpx";
    public static final Boolean verbose = false;

    public static void gpxFilesExist() {
        File source = new File(GPX_DIRECTORY);

        if (source.exists() == false) {
            System.out.println("[ CRITICAL ] Directory with gpx files not found. Missing: " + GPX_DIRECTORY);
            System.exit(1);
        }

        System.out.println("Gpx test passed");
    }

    public static void testParsing() {
        try {
            String testfile = GPX_DIRECTORY + File.separator + "route1.gpx";
            File source = new File(testfile);

            if (source.exists() == false) {
                System.out.println("[ CRITICAL ] Test file not found. Missing: " + testfile);
                System.exit(1);
            }

            Serializer serializer = new Persister();
            Gpx gpx = serializer.read(Gpx.class, source);

            System.out.println("Gpx file parse test passed with samples: " + gpx.getElements().size());
            
            if (verbose) {
                for (Wpt wpt: gpx.getElements()) {
                    System.out.println(wpt);
                }
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();                   
            System.out.println("[ CRITICAL ] Critical exception occured. Main failed.");
            System.exit(1);
        }

    }
}
