package com.alexronnegi.client.loader;

import static com.alexronnegi.client.sanity.SanityChecker.verbose;
import com.alexronnegi.serialization.Gpx;
import com.alexronnegi.serialization.Wpt;
import java.io.File;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class GpxLoader {
    public Gpx load(String file) throws Exception {
        File source = new File(file);

        Serializer serializer = new Persister();
        Gpx gpx = serializer.read(Gpx.class, source);
        
        return gpx;
    }
}
