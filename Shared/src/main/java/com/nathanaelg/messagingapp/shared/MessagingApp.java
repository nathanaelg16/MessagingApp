package com.nathanaelg.messagingapp.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.sql.Timestamp;

public abstract class MessagingApp {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Socket socket;

    protected MessagingApp(Socket socket) {
        this.socket = socket;
    }

    protected Socket getSocket() {
        return socket;
    }

    protected ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * Send a message
     * @param message Message to be sent
     */
    protected void sendMessage(String senderName, String message, boolean isRegistration, InetAddress destAddress, int destPort) throws JsonProcessingException {
        Message msg = new Message(senderName, message, (new Timestamp(System.currentTimeMillis())).toString(), isRegistration);
        socket.send(mapper.writeValueAsBytes(msg), destAddress, destPort);
    }

    protected byte[] getData() {
        return this.socket.receiveAsBytes();
    }

    protected DatagramPacket getDatagram() {
        return this.socket.receive();
    }

    /**
     * Register user
     */
    public abstract void register(Registration userRegistration);
}
