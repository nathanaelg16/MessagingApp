package com.nathanaelg.messagingapp.shared;

import java.io.IOException;
import java.net.*;

public class Socket {
    private final int port;
    private InetAddress address;
    private DatagramSocket socket;

    public Socket(int port, boolean broadcast) {
        this.port = port;

        try {
            this.address = InetAddress.getLocalHost();
            System.out.println("IP Address: " + address);
        } catch (UnknownHostException e) {
            doErrorHandling(e);
        }

        try {
            if (broadcast) {
                this.socket = new DatagramSocket(this.port);
            } else {
                this.socket = new DatagramSocket(this.port, this.address);
            }
        } catch (SocketException e) {
            doErrorHandling(e);
        }
    }

    public void send(String message, InetAddress destination, int port) {
        send(message.getBytes(), destination, port);
    }

    public void send(byte[] message, InetAddress destination, int port) {
        DatagramPacket packet = new DatagramPacket(message, message.length, destination, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            doErrorHandling(e);
        }
    }

    public DatagramPacket receive() {
        byte[] incomingBuffer = new byte[1024];
        DatagramPacket incomingPacket = new DatagramPacket(incomingBuffer, incomingBuffer.length);
        try {
            socket.receive(incomingPacket);
        } catch (IOException e){
            doErrorHandling(e);
        }
        return incomingPacket;
    }

    public byte[] receiveAsBytes() {
        DatagramPacket packet = receive();
        return packet != null ? packet.getData() : null;
    }

    private static void doErrorHandling(Exception e) {
        e.printStackTrace();
        System.exit(-1);
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPortNumber() {
        return port;
    }

    public void close() {
        socket.close();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
}