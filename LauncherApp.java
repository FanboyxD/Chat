import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LauncherApp extends Application {

    private int clientCounter = 12344; // Counter to keep track of client order

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Launcher");

        Button startServerButton = new Button("Iniciar Servidor");
        Button startChatAppButton = new Button("Iniciar ChatApp");
        startChatAppButton.setDisable(true); // Disable initially

        startServerButton.setOnAction(event -> {
            startServer();
            startChatAppButton.setDisable(false); // Enable the ChatApp button
        });

        startChatAppButton.setOnAction(event -> startChatApp()); // Link the method to the button action

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(startServerButton, startChatAppButton);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void startServer() {
        Thread serverThread = new Thread(() -> ChatServer.main(null));
        serverThread.start();
    }

    private void startChatApp() {
        Stage chatAppStage = new Stage();
        ChatApp chatApp = new ChatApp();
        chatApp.start(chatAppStage);

        // Increment the client counter and create a label to indicate the order
        clientCounter++;
        Label orderLabel = new Label("Puerto" + clientCounter);
        chatAppStage.setTitle("ChatApp - Puerto #" + clientCounter);
        
        VBox chatAppRoot = (VBox) chatAppStage.getScene().getRoot();
        chatAppRoot.getChildren().add(orderLabel);

        chatAppStage.setOnCloseRequest(event -> {
            chatAppRoot.getChildren().remove(orderLabel);
            clientCounter--;
        });
    }
}
