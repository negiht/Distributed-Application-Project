package com.alexronnegi.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Network {
    private ServerSocket listeningSocket = null;
    private Socket chatSocket = null;
    
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    
    public Network connect(String ip, int port) throws IOException {
        if (listeningSocket != null) {
            throw new IllegalStateException("use either to serve or to connect");
        }
        
        if (chatSocket != null) {
            throw new IllegalStateException("please close previous socket first");
        }
        
        chatSocket = new Socket(ip, port);
        
        input = new ObjectInputStream(chatSocket.getInputStream());
        output = new ObjectOutputStream(chatSocket.getOutputStream());
        
        return this;
    }
    
    public Network listen(String ip, int port) throws IOException {
        if (chatSocket != null) {
            throw new IllegalStateException("use either to serve or to connect");
        }
        
        if (listeningSocket != null) {
            throw new IllegalStateException("please close previous socket first");
        }
        
        for (int i=0;i<20;i++) {
            try {
                listeningSocket = new ServerSocket(port, 255, InetAddress.getByName(ip));
                System.out.println("Port opened for incoming connections: " + port);
                return this;
            } catch (Exception ex) {
                System.out.println("Warning: port unavailable. Changing port to " + (port + 1));
                port++;
            }
        }
        
        throw new IllegalStateException("No available port found");
    }
    
    public Network accept() throws IOException {
        if (listeningSocket == null) {
            throw new IllegalStateException("listening socket setup is required to enable listening");
        }
        
        Network network = new Network();
        network.chatSocket = listeningSocket.accept();
        
        network.output = new ObjectOutputStream(network.chatSocket.getOutputStream());
        network.input = new ObjectInputStream(network.chatSocket.getInputStream());
        return network;
    }
    
    public Network disconnect() throws IOException {
        if (chatSocket != null) {
            chatSocket.close();
            chatSocket = null;
            output = null;
            input = null;
        }
        if (listeningSocket != null) {
            listeningSocket.close();
            listeningSocket = null;
            output = null;
            input = null;
        }
        return this;
    }
    
    public boolean isConnected() {
        return chatSocket != null || listeningSocket != null;
    }
    
    public Network send(Object object) throws IOException {
        if (output == null) {
            throw new IllegalStateException("please initialize a socket first");
        }
        output.writeObject(object);
        output.flush();
        
        return this;
    }
    
    public Object receive() throws IOException, ClassNotFoundException {
        if (input == null) {
            throw new IllegalStateException("please initialize a socket first");
        }
        
        Object object = input.readObject();        
        
        return object;
    }
}
