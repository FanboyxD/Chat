import java.io.PrintWriter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ChatApp extends Application {
    private Cliente client;
    private PrintWriter serverOut;
    @Override
    public void start(Stage primaryStage) {
        Thread serverThread = new Thread(() -> {
            Server server = new Server();
            server.startServer(); // Implement this method in your ChatServer class
        });
        serverThread.start();
        // Layout for the main chat window
        BorderPane mainLayout = new BorderPane();

        // Text area to display chat messages
        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        mainLayout.setCenter(chatArea);

        // HBox for entering messages and sending
        TextField messageField = new TextField();
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> {
            String message = messageField.getText(); // Get the message from the input field
    
    // Send the message to the server through the client
    // Implement this logic in your ChatClient class
            messageField.clear(); // Clear the input field after sending
            chatArea.appendText("Yo: " + message + "\n");
        });
        HBox inputBox = new HBox(messageField, sendButton);
        inputBox.setSpacing(10);
        inputBox.setPadding(new Insets(10));
        mainLayout.setBottom(inputBox);

        // VBox for entering server IP and port
        TextField ipField = new TextField();
        TextField portField = new TextField();
        Label ipLabel = new Label("IP del servidor:");
        Label portLabel = new Label("Puerto disponible:");
        VBox serverInfoBox = new VBox(ipLabel, ipField, portLabel, portField);
        serverInfoBox.setSpacing(10);
        serverInfoBox.setPadding(new Insets(10));
        mainLayout.setLeft(serverInfoBox);

        // Create the Connect button
        Button connectButton = new Button("Conectar");
        connectButton.setOnAction(event -> {
            String serverIP = ipField.getText();
            int serverPort = Integer.parseInt(portField.getText());

            // Start the client in a separate thread
            Thread clientThread = new Thread(() -> {
                Cliente client = new Cliente(serverIP, serverPort);
                client.startClient(); // Implement this method in your ChatClient class
                
            });
            clientThread.start();
        });
        serverInfoBox.getChildren().add(connectButton);

        // Create the scene and set it to the stage
        Scene scene = new Scene(mainLayout, 400, 300);
        primaryStage.setTitle("Aplicacion de Chat");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
