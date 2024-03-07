package com.alexronnegi.master.nodes;

import com.alexronnegi.client.sanity.SanityChecker;
import com.alexronnegi.computations.Computer;
import com.alexronnegi.network.Network;
import com.alexronnegi.serialization.Gpx;
import com.alexronnegi.serialization.MagicToken;
import com.alexronnegi.serialization.Result;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Master extends Node {

    public static final int VERSION = 1;
    private final String ip;
    private final int port;
    private final Computer computer = new Computer();
    private final String[] worker_ips;
    private final Integer[] worker_ports;
    private final HashMap<String, Result> users = new HashMap<>();

    public Master(String ip, int port, String[] worker_ips, Integer[] worker_ports) {
        super("Master");

        this.ip = ip;
        this.port = port;
        this.worker_ips = worker_ips;
        this.worker_ports = worker_ports;

        println("Total workers reported:" + worker_ips.length);
    }

    public void service(Network clientNetwork) {

        try {
            Gpx gpx = (Gpx) clientNetwork.receive();

            System.out.println("Gpx received: ");

            System.out.println(gpx + ", samples: " + gpx.getElements().size());

            int w = worker_ips.length;

            final Gpx[] workerGpx = new Gpx[w];
            final Thread[] workerThreads = new Thread[w];
            final List<Result> results = new ArrayList<>();

            for (int i = 0; i < w; i++) {
                Gpx wgpx = new Gpx();
                wgpx.setElements(new ArrayList<>());
                workerGpx[i] = wgpx;
                workerGpx[i].setUser(gpx.getUser());
                workerGpx[i].setVersion(gpx.getVersion());

                int L = gpx.getElements().size() / w;
                int lmin = L * i;
                int lmax = L * i + L + 1;

                for (int j = lmin; j < lmax; j++) {
                    if (j < gpx.getElements().size()) {
                        wgpx.getElements().add(gpx.getElements().get(j));
                    }
                }
            }

            for (int i = 0; i < w; i++) {
                final String x = worker_ips[i];
                final Integer y = worker_ports[i];
                final Gpx wgps = workerGpx[i];

                workerThreads[i] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MagicToken token = new MagicToken(1);

                            Result result = (Result) new Network().connect(x, y).send(token).send(wgps).receive();

                            synchronized (results) {
                                results.add(result);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }

            for (int i = 0; i < w; i++) {
                workerThreads[i].start();
            }

            for (int i = 0; i < w; i++) {
                workerThreads[i].join();
            }

            Result result = computer.reduce(results);

            String user = gpx.getUser();

            if (user != null) {
                Result prevresult = null;
                
                synchronized (users) {
                    prevresult = users.get(user);

                    if (prevresult == null) {
                        users.put(user, result);
                        prevresult = new Result(0,0,0);
                    } else {
                        prevresult.setDistance(prevresult.getDistance() + result.getDistance());
                        prevresult.setElevation(prevresult.getElevation() + result.getElevation());
                        prevresult.setTime(prevresult.getTime() + result.getTime());
                    }
                }
                
                clientNetwork.send(result);
                clientNetwork.send(prevresult);
            } else {
                clientNetwork.send(result);
                clientNetwork.send(new Result(0,0,0));
            }
        } catch (Exception ex) {
            ex.printStackTrace();;
        }
    }

    public void run() {
        println("started");

        try {
            Network network = new Network().listen(ip, port);

            while (true) {
                println("waiting for connection ...");

                final Network clientNetwork = network.accept();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        service(clientNetwork);
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
            int port = 43434;

            String[] worker_ips = new String[]{"192.168.2.9", "192.168.2.9"};
            Integer[] worker_ports = new Integer[]{50000, 50001};

            System.out.println("Booting Server v" + VERSION + " at interface: " + ip + ":" + port);

            SanityChecker.pingWorkers(worker_ips, worker_ports);

            Master master = new Master(ip, port, worker_ips, worker_ports);
            master.start();
            master.join();

            System.out.println("Done.");
        } catch (InterruptedException ex) {
            Logger.getLogger(Master.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
