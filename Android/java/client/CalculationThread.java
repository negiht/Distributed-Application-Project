package com.alexronnegi.client;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.alexronnegi.serialization.Gpx;
import com.alexronnegi.serialization.Result;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CalculationThread extends Thread{

    static int counter = 0;

    static String user;
    Handler myHandler;
    InputStream stream;


    public CalculationThread(Handler myHandler, InputStream stream){

        this.myHandler = myHandler;
        this.stream = stream;
        counter++;

    }

    @Override
    public void run() {
        try {
            // Establish a socket connection
            Socket socket = new Socket("192.168.2.9", 43434);

            // Create input/output streams for socket communication
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // Send the file
            Serializer serializer = new Persister();
            Gpx gpx = serializer.read(Gpx.class, stream);
            oos.writeObject(gpx);
            oos.flush();
            user = gpx.getUser();

            // Receive the result
            Result result = (Result) ois.readObject();

            // Process the result
            double distance = result.getDistance();
            double time = result.getTime() / 60000;
            double elevation = result.getElevation();
            double speed = 0.0;

            if (time > 0) {
                speed = distance / time * 60;
            }

            // Handle the result in the main thread using a Handler
            Message message = myHandler.obtainMessage();
            Bundle bundle1 = new Bundle();
            bundle1.putString("result", user.toUpperCase() + ":\n\n" + " File: " + "route" + counter + ".gpx\n" +
                    "     Distance: " + String.format("%.2f", distance) + " km" + "\n" +
                    "     Time: " + String.format("%.2f", time) + " minutes" + "\n" +
                    "     Elevation: " + String.format("%.2f", elevation) + " metres\n" +
                    "     Average Speed: " + String.format("%.2f",speed)+" km/h\n");
            message.setData(bundle1);
            myHandler.sendMessage(message);


            // Close the socket
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            // Handle the exception here
        }
    }
}