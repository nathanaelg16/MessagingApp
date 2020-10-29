package com.nathanaelg.messagingapp.client;

import com.nathanaelg.messagingapp.shared.Message;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MessageNode extends Region {
    public MessageNode(Message message, String registeredUser) {
        VBox vbox = new VBox();
        vbox.setSpacing(1.0);
        vbox.maxWidthProperty().bind(this.maxWidthProperty());

        Label senderLabel = new Label(String.format("%s\t%s", message.getSender(), message.getTimestamp()));
        Label messageLabel = new Label(message.getMessage());
        messageLabel.maxWidthProperty().bindBidirectional(vbox.maxWidthProperty());
        messageLabel.setWrapText(true);
        messageLabel.setAlignment(message.getSender().equals(registeredUser) ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        //messageLabel.setStyle(""); todo set custom css for text field (rounded edges, etc)

        vbox.maxHeightProperty().bind(messageLabel.heightProperty());
        this.maxHeightProperty().bind(vbox.maxHeightProperty());

        vbox.getChildren().addAll(senderLabel, messageLabel);
        this.getChildren().add(vbox);
    }
}
