import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    private ChoiceBox<Integer> portChoiceBox;
    private Button sendToButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat App");

        // Crear componentes de la interfaz gráfica
        chatArea = new TextArea();
        messageField = new TextField();
        
        portLabel = new Label("Puerto: -");

        // Configurar acción del botón Enviar
        

        // Crear componente ChoiceBox
        portChoiceBox = new ChoiceBox<>();
        portChoiceBox.getItems().addAll(12345, 12346, 12347); // Agrega los puertos disponibles
        portChoiceBox.setValue(12345); // Establece un puerto predeterminado

        // Crear componente Button
        sendToButton = new Button("Enviar a");
        sendToButton.setOnAction(event -> sendToPort());

        // Crear contenedor principal y agregar componentes
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(chatArea, messageField, portLabel, portChoiceBox, sendToButton);

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


    private void sendToPort() {
        int targetPort = portChoiceBox.getValue();
        String message = messageField.getText();
        client.sendMessageToPort(message, targetPort);
        chatArea.appendText("Enviado a puerto " + targetPort + ": " + message + "\n");
        messageField.clear();
    }
}
