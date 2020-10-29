package com.nathanaelg.messagingapp.client;

import com.nathanaelg.messagingapp.shared.Message;
import com.nathanaelg.messagingapp.shared.Registration;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Timestamp;

public class MessagesView extends Application {
    private final MessagingClient client = new MessagingClient();
    private TableView<Message> messageTableView;

    private final Tooltip SEND_DISABLED_TOOLTIP = new Tooltip("Please register first.");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();

        Text text = new Text("CMP 405: Messaging Client");

        Label nameLabel = new Label("Enter name");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        Button registerButton = new Button("Register");
        registerButton.setOnAction(ev -> client.register(new Registration(nameField.getText())));
        registerButton.disableProperty().bind(client.registeredProperty);

        messageTableView = new TableView<>();
        messageTableView.prefWidthProperty().bind(primaryStage.widthProperty());
        messageTableView.setRowFactory(r -> {
            TableRow<Message> row = new TableRow<>();
            row.maxWidthProperty().bind(messageTableView.widthProperty());
            row.setPrefHeight(40.0);
            return row;
        });

        TableColumn<Message, Region> messageColumn = new TableColumn<>();
        messageColumn.prefWidthProperty().bind(messageTableView.widthProperty());
        messageColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(new MessageNode(cellData.getValue(), client.getRegisteredUser())));
        messageColumn.setCellFactory(col -> {
            TableCell<Message, Region> cell = new TableCell<>();
            cell.maxWidthProperty().bind(messageTableView.widthProperty());
            cell.prefHeight(40.0);
            cell.graphicProperty().bind(cell.itemProperty());
            return cell;
        });
        messageTableView.getColumns().add(messageColumn);

        TextField sendMessageField = new TextField();
        sendMessageField.setPromptText("Type message...");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(ev -> {client.sendMessage(sendMessageField.getText()); sendMessageField.clear();});
        sendButton.disableProperty().bind(client.registeredProperty.not());
        sendButton.disableProperty().addListener((ob, ov, nv) -> sendButton.setTooltip(nv ? SEND_DISABLED_TOOLTIP : null));

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.add(text, 0, 0);
        grid.add(nameLabel, 0, 1, 1, 1);
        grid.add(nameField, 1, 1, 1, 1);
        grid.add(registerButton, 2, 1, 1, 1);
        grid.add(messageTableView, 0, 2, 3, 5);
        grid.add(sendMessageField, 0, 7, 2, 1);
        grid.add(sendButton, 2, 7, 1, 1);

        messageTableView.getItems().add(new Message("CLIENT ", "Waiting for registration...",
                new Timestamp(System.currentTimeMillis()).toString(), false));

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);

        primaryStage.show();

        Runnable refresh = () -> {
            while (true) {
                Message message = client.receiveMessage();
                if (message != null) {
                    messageTableView.getItems().add(message);
                    if (message.isRegistration()) client.registeredProperty.set(true);
                }
            }
        };

        Thread refreshThread = new Thread(refresh);
        refreshThread.setDaemon(true);
        refreshThread.start();
    }
}