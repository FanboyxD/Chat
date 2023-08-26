/**
 * @author Fabricio Mena y Pablo Araya
 */
import javafx.application.Application; 
import javafx.geometry.Pos; 
import javafx.scene.Scene; 
import javafx.scene.control.Button; 
import javafx.scene.control.Label; 
import javafx.scene.layout.VBox; 
import javafx.stage.Stage; 
 
 
/** 
 * Clase principal que actúa como punto de entrada para la aplicación. 
 */ 
public class LauncherApp extends Application { 
 
    private int clientCounter = 12344; // Contador para realizar un seguimiento del orden de los clientes. 
 
 
    /** 
     * Método principal que inicia la aplicación. 
     * @param args Arreglo de cadenas de texto. 
     * @return No retorna ningún valor, es de tipo void. 
     */ 
    public static void main(String[] args) { 
        launch(args); 
    } 
 
 
    /** 
     * Se encarga de crear la interfaz gráfica del Launcher, iniciar el servidor y ejecutar el ChatApp (las dos últimas se realizan por medio de botones). 
     * @param primaryStage Objeto de tipo Stage (ventana o contenedor principal en el que se muestran los elementos de la interfaz gráfica). 
     * @return No retorna ningún valor, es de tipo void. 
     */ 
    @Override 
    public void start(Stage primaryStage) { 
        primaryStage.setTitle("Launcher"); 
 
        Button startServerButton = new Button("Iniciar Servidor"); 
        Button startChatAppButton = new Button("Iniciar ChatApp"); 
        startChatAppButton.setDisable(true); // Deshabilita inicialmente el botón de ChatApp. 
 
        startServerButton.setOnAction(event -> { 
            startServer(); 
            startChatAppButton.setDisable(false); // Habilita el botón de ChatApp. 
        }); 
 
        startChatAppButton.setOnAction(event -> startChatApp()); // Vincula el método a la acción del botón. 
 
        VBox root = new VBox(10); 
        root.setAlignment(Pos.CENTER); 
        root.getChildren().addAll(startServerButton, startChatAppButton); 
 
        Scene scene = new Scene(root, 300, 200); 
        primaryStage.setScene(scene); 
        primaryStage.show(); 
    } 
 
 
    /** 
     * Método que inicia el servidor en un hilo separado. 
     * @return No retorna ningún valor, es de tipo void. 
     */ 
    private void startServer() { 
        Thread serverThread = new Thread(() -> ChatServer.main(null)); 
        serverThread.start(); 
    } 
 
 
    /** 
     * Método que inicia la aplicación de chat. 
     * @return No retorna ningún valor, es de tipo void. 
     */ 
    private void startChatApp() { 
        Stage chatAppStage = new Stage(); 
        ChatApp chatApp = new ChatApp(); 
        chatApp.start(chatAppStage); 
 
        // Incrementa el contador de clientes y crea una etiqueta para indicar el orden. 
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
