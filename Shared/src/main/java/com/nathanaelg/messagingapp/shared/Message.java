package com.nathanaelg.messagingapp.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    private final String sender;
    private final String message;
    private final String timestamp;
    private final boolean registration;

    @JsonCreator
    public Message(@JsonProperty("sender") String sender, @JsonProperty("message") String message, @JsonProperty("timestamp") String timestamp, @JsonProperty("registration") boolean registration) {
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
        this.registration = registration;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isRegistration() {
        return registration;
    }

    @Override
    public String toString() {
        return "shared.Message{" +
                "sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
