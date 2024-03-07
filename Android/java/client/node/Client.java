package com.alexronnegi.client.node;

import com.alexronnegi.client.loader.GpxLoader;
import com.alexronnegi.network.Network;
import com.alexronnegi.client.sanity.SanityChecker;
import com.alexronnegi.serialization.Gpx;
import com.alexronnegi.serialization.Result;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Node {
    public static final int VERSION = 1;
    private final String file;
    private final String ip;
    private final int port;

    public Client(String file, String ip, int port) {
        super("Client");
        
        if (file.endsWith(".gpx")) {
            this.file = file;
        } else {
            this.file = file + ".gpx";
        }
        
        this.ip = ip;
        this.port = port;
        
        println("created for file: " + this.file);
    }
    
    public void run() {
        
        println("started for file: " + file);
        
        try {
            Network network = new Network();
            GpxLoader loader = new GpxLoader();
            
            Gpx gpx = loader.load(file);
            
            println("loaded samples: " + gpx.getElements().size());
            
            network.connect(ip, port).send(gpx);
        
            Result result = (Result) network.receive();
            Result prevresult = (Result) network.receive();
        
            System.out.println("------------------------------------------ Result\n");
            
            System.out.println(result);
            
            double distance = result.getDistance();
            double time = result.getTime();
            
            double speed = 0.0;
            
            if (time > 0) {
                speed = distance/time;
            }
            
            System.out.println("Average speed: " + time);
            
            System.out.println("------------------------------------------ User running result\n");
            
            System.out.println(prevresult);
            
            double pdistance = prevresult.getDistance();
            double ptime = prevresult.getTime();
            
            double pspeed = 0.0;
            
            if (ptime > 0) {
                pspeed = pdistance/ptime;
            }
            
            System.out.println("Average speed: " + ptime);
        } catch (Exception ex) {
            ex.printStackTrace();
            
            println("file: " + file + " failed") ;
            
            System.exit(1);
        }
        
        println("finished for file: " + file);
    }

    
//     public static void main(String[] args) {
//        try {
//            String ip = "127.0.0.1"; // Server IP
//            int port = 43434; // Server Port
//
//            System.out.println("Booting Client v" + VERSION);
//
//            SanityChecker.gpxFilesExist();
//            SanityChecker.testParsing();
//
//            for (int i=1;i<=6;i++) {
//                Client client = new Client(SanityChecker.GPX_DIRECTORY + File.separator + "route" + i, ip, port);
//
//                System.out.println("Starting client ... " + i + " of " + 6);
//                client.start();
//
//                System.out.println("Waiting for client to exit ... ");
//                client.join();
//            }
//
//            System.out.println("Done.");
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

}
