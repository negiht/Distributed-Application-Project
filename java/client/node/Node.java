package com.alexronnegi.client.node;

public class Node extends Thread {
    public String prefix;

    public Node(String prefix) {
        this.prefix = prefix;
    }
    
    protected void println(String message) {
        System.out.println("    " + prefix + ": " + message);
    }
    
}
