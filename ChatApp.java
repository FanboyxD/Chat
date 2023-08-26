/**
 * @author Fabricio Mena y Pablo Araya
 */
import javafx.application.Application; 
import javafx.geometry.Insets; 
import javafx.scene.Scene; 
import javafx.scene.control.Button; 
import javafx.scene.control.ChoiceBox; 
import javafx.scene.control.TextArea; 
import javafx.scene.control.TextField; 
import javafx.scene.layout.VBox; 
import javafx.stage.Stage; 
 
 
/** 
 * Clase que representa una aplicación de chat. 
 */ 
public class ChatApp extends Application { 
 
    private ChatClient client; 
    private TextArea chatArea; 
    private TextField messageField; 
    private ChoiceBox<Integer> portChoiceBox; 
    private Button sendToButton; 
 
 
    /** 
     * Método principal que inicia el ChatApp. 
     * @param args Arreglo de cadenas de texto. 
     * @return No retorna ningún valor, es de tipo void. 
     */ 
    public static void main(String[] args) { 
        launch(args); 
    } 
 
 
    /** 
     * Se encarga de crear la interfaz gráfica de ChatApp. 
     * @param primaryStage Es el contenedor donde se muestra la interfaz. 
     * @return No retorna ningún valor, es de tipo void. 
     */ 
    @Override 
    public void start(Stage primaryStage) { 
        primaryStage.setTitle("Chat App"); 
 
        // Crea componentes de la interfaz gráfica 
        chatArea = new TextArea(); 
        messageField = new TextField(); 
 
        // Crea componente Button 
        sendToButton = new Button("Enviar a"); 
        sendToButton.setOnAction(event -> sendToPort()); 
 
        // Crea componente ChoiceBox 
        portChoiceBox = new ChoiceBox<>(); 
        portChoiceBox.getItems().addAll(12345, 12346, 12347, 12348, 12349); // Agrega los puertos disponibles 
        portChoiceBox.setValue(12345); // Establece un puerto predeterminado 
 
         
 
        // Crea contenedor principal y agregar componentes 
        VBox root = new VBox(10); 
        root.setPadding(new Insets(10)); 
        root.getChildren().addAll(chatArea, messageField, portChoiceBox, sendToButton); 
 
        // Crea escena y muestra la ventana 
        Scene scene = new Scene(root, 400, 300); 
        primaryStage.setScene(scene); 
        primaryStage.show(); 
 
        // Conecta al servidor 
        client = new ChatClient(); 
        String serverIp = "127.0.0.1"; // Cambia por la dirección IP del servidor 
        int port = 12345; // Cambia por el puerto del servidor 
        client.connect(serverIp, port, chatArea); 
    } 
 
 
    /** 
     * Método para enviar un mensaje al puerto seleccionado. 
     * @return No retorna ningún valor, es de tipo void. 
     */ 
    private void sendToPort() { 
        int targetPort = portChoiceBox.getValue(); 
        String message = messageField.getText(); 
        client.sendMessageToPort(message, targetPort); 
        chatArea.appendText("Enviado a puerto " + targetPort + ": " + message + "\n"); 
        messageField.clear(); 
    } 
}
