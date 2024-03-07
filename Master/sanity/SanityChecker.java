package com.alexronnegi.client.sanity;

import com.alexronnegi.network.Network;
import com.alexronnegi.serialization.Gpx;
import com.alexronnegi.serialization.MagicToken;
import com.alexronnegi.serialization.Wpt;
import java.io.File;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class SanityChecker {

    public static void pingWorkers(String[] worker_ips, Integer[] worker_ports) {
        if (worker_ips.length != worker_ports.length) {
            throw new IllegalStateException("worker ips and ports do not match");
        }

        try {
            for (int i = 0; i < worker_ips.length; i++) {
                System.out.println("testing worker " + (i + 1) + " for availability: " + worker_ips[i] + ":" + worker_ports[i]);
                Network network = new Network().connect(worker_ips[i], worker_ports[i]).send(new MagicToken(0));
                network.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Network setup failed. Please check worker IP and ports");
            System.exit(1);
        }
    }
}
