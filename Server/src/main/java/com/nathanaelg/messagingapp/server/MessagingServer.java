package com.nathanaelg.messagingapp.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nathanaelg.messagingapp.shared.Message;
import com.nathanaelg.messagingapp.shared.MessagingApp;
import com.nathanaelg.messagingapp.shared.Registration;
import com.nathanaelg.messagingapp.shared.Socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;

public class MessagingServer extends MessagingApp {
    private final ArrayList<Registration> registeredUsers = new ArrayList<>();

    public MessagingServer() {
        super(new Socket(64400, false));
    }

    public static void main(String[] args) {
        MessagingServer server = new MessagingServer();
        while (true) {
            Object received = server.receiveMessage();
            if (received != null) {
                if (received instanceof Message) server.sendMessage((Message) received);
                else if (received instanceof Registration) server.register((Registration) received);
            }
        }
    }

    public void sendMessage(Message message) {
        for (Registration user : registeredUsers) {
            try {
                super.getSocket().send(super.getMapper().writeValueAsBytes(message), user.getAddress(), user.getPort());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public Object receiveMessage() {
        DatagramPacket packet = super.getDatagram();
        byte[] bytes = packet.getData();

        try {
            Message message = super.getMapper().readValue(bytes, Message.class);
            if (message.isRegistration()) return new Registration(message.getSender(), packet.getAddress(), packet.getPort());
            else return message;
        } catch (IOException e) {
            // do nothing
            e.printStackTrace();
        }
        return null;
    }


    public void sendMessage(Registration user, String message, boolean isRegistration) {
        try {
            super.sendMessage("SERVER", message, isRegistration, user.getAddress(), user.getPort());
        } catch (JsonProcessingException e) {
            // DO NOTHING
            e.printStackTrace();
        }
    }

    public synchronized void register(Registration userRegistration) {
        if (!registeredUsers.contains(userRegistration)) {
            registeredUsers.add(userRegistration);
            sendMessage(userRegistration, "Successfully registered.", true);
        } else sendMessage(userRegistration, "Registration unsuccessful. Name is taken.", false);
    }
}
