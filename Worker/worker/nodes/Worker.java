package com.alexronnegi.worker.nodes;

import com.alexronnegi.computations.Computer;
import com.alexronnegi.network.Network;
import com.alexronnegi.serialization.Gpx;
import com.alexronnegi.serialization.MagicToken;
import com.alexronnegi.serialization.Result;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Worker extends Node {

    public static final int VERSION = 1;
    private final String ip;
    private final int port;

    public Worker(String ip, int port) {
        super("Worker");

        this.ip = ip;
        this.port = port;
    }

    public void run() {
        println("started");

        try {
            Network network = new Network().listen(ip, port);

            while (true) {
                println("waiting for connection ...");

                Network clientNetwork = network.accept();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MagicToken token = (MagicToken) clientNetwork.receive();
                                    
                            if (token.getValue() == 1) {                            
                                Gpx gpx = (Gpx) clientNetwork.receive();

                                Computer computer = new Computer();

                                System.out.println("Gpx received: ");

                                System.out.println(gpx + ", samples: " + gpx.getElements().size());

                                Result result = computer.reduce(gpx);

                                clientNetwork.send(result);                                
                            }
                            
                            clientNetwork.disconnect();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                
                t.start();
            }

        } catch (Exception ex) {
            ex.printStackTrace();

            println(" failed");
        }

        println("finished");
    }

    public static void main(String[] args) {
        try {
            String ip = "192.168.2.9";
            int port = 50000;

            System.out.println("Booting Worker v" + VERSION);

            Worker worker = new Worker(ip, port);

            System.out.println("Starting Worker ... ");
            worker.start();

            System.out.println("Waiting for worker to exit ... ");
            worker.join();

            System.out.println("Done.");
        } catch (InterruptedException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
