package com.nathanaelg.messagingapp.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.beans.property.SimpleBooleanProperty;
import com.nathanaelg.messagingapp.shared.Message;
import com.nathanaelg.messagingapp.shared.MessagingApp;
import com.nathanaelg.messagingapp.shared.Registration;
import com.nathanaelg.messagingapp.shared.Socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class MessagingClient extends MessagingApp {
    private final ResourceBundle config = ResourceBundle.getBundle("config");
    private final InetAddress SERVER_ADDRESS;
    private final int SERVER_PORT;

    public SimpleBooleanProperty registeredProperty;
    private String registeredUser;

    public MessagingClient() {
        super(new Socket(64500, false));
        registeredProperty = new SimpleBooleanProperty(false);
        SERVER_PORT = Integer.parseInt(config.getString("port"));

        InetAddress dest = null;
        try {
            dest = InetAddress.getByName(config.getString("server"));
        } catch (UnknownHostException e) {
            ErrorMessage.show("Unable to resolve host.", "Unknown Host");
            System.exit(1);
        }
        SERVER_ADDRESS = dest;
    }

    public void sendMessage(String message) {
        try {
            super.sendMessage(registeredUser, message, false, SERVER_ADDRESS, SERVER_PORT);
        } catch (JsonProcessingException e) {
            ErrorMessage.show("Unable to process your message. Please try again.", "Message Processing Error");
        }
    }

    public Message receiveMessage() {
        Message message = null;
        byte[] bytes = super.getData();

        try {
            if (bytes != null) {
                message = super.getMapper().readValue(bytes, Message.class);
            }
        } catch (IOException e) {
            // if message cannot be parsed, do nothing
            e.printStackTrace();
        }

        return message;
    }

    public void register(Registration userRegistration) {
        try {
            super.sendMessage(userRegistration.getName(), null, true, SERVER_ADDRESS, SERVER_PORT);
            this.registeredUser = userRegistration.getName();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorMessage.show("Unable to register.\n\nError: " + e.getMessage(), "Registration Error");
        }
    }

    public String getRegisteredUser() {
        return registeredUser;
    }
}