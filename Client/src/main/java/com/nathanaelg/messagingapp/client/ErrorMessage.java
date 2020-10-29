package com.nathanaelg.messagingapp.client;

import javafx.scene.control.Alert;

public class ErrorMessage {
    public static void show(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(500);
        alert.showAndWait();
    }
}