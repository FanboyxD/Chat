// ChatApp.java
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatApp extends Application {

    private ChatClient client;
    private TextArea chatArea;
    private TextField messageField;
    private Label portLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat App");

        // Crear componentes de la interfaz gráfica
        chatArea = new TextArea();
        messageField = new TextField();
        Button sendButton = new Button("Enviar");
        portLabel = new Label("Puerto: -");

        // Configurar acción del botón Enviar
        sendButton.setOnAction(event -> sendMessage());

        // Crear contenedor principal y agregar componentes
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(chatArea, messageField, sendButton, portLabel);

        // Crear escena y mostrar ventana
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Conectar al servidor
        client = new ChatClient();
        String serverIp = "127.0.0.1"; // Cambiar por la dirección IP del servidor
        int port = 12345; // Cambiar por el puerto del servidor
        client.connect(serverIp, port, chatArea);

        // Mostrar el puerto en la interfaz gráfica
        portLabel.setText("Puerto: " + client.getPort());
    }

    private void sendMessage() {
        String message = messageField.getText();
        client.sendMessage(message);
        chatArea.appendText("Enviado: " + message + "\n");
        messageField.clear();
    }
}
