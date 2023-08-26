/**
 * @author Fabricio Mena y Pablo Araya
 */
import java.io.IOException; 
import java.io.PrintWriter; 
import java.net.Socket; 
import java.util.Scanner; 
import javafx.scene.control.TextArea; 
 
 
/** 
 * Clase que representa un cliente de chat. 
 */ 
public class ChatClient { 
 
    private Socket socket; 
    private PrintWriter writer; 
    private int port; 
    private TextArea chatArea; 
 
 
    /** 
     * Método para conectar el cliente al servidor de chat. 
     * @param serverIp  Dirección IP del servidor. 
     * @param port      Puerto del servidor. 
     * @param chatArea  Área de texto para mostrar los mensajes del chat. 
     * @return No retorna ningún valor, es de tipo void. 
     */ 
    public void connect(String serverIp, int port, TextArea chatArea) { 
        try { 
            socket = new Socket(serverIp, port); 
            System.out.println("Conectado al servidor en " + serverIp + ":" + port); 
            this.port = port; 
            this.chatArea = chatArea; 
 
            writer = new PrintWriter(socket.getOutputStream(), true); 
            Thread readerThread = new Thread(new MessageReader(socket)); 
            readerThread.start(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
 
 
    /** 
     * Método para enviar un mensaje a un puerto específico. 
     * @param message     Mensaje a enviar. 
     * @param targetPort  Puerto de destino. 
     * @return No retorna ningún valor, es de tipo void. 
     */ 
    public void sendMessageToPort(String message, int targetPort) { 
        writer.println(targetPort + ":" + message); 
    } 
 
 
    /** 
     * Método para obtener el puerto del cliente. 
     * @return El puerto del cliente. 
     */ 
    public int getPort() { 
        return port; 
    } 
 
 
    /** 
     * Toma el mensaje escrito por el cliente y lo muestra en un área de chat. 
     * @param message Mensaje del cliente. 
     * @return No retorna ningún valor, es de tipo void. 
     */ 
    private void receiveMessage(String message) { 
        chatArea.appendText("Recibido: " + message + "\n"); 
    } 
 
 
     /** 
     * Clase interna que maneja la lectura de mensajes del servidor. 
     */ 
    private class MessageReader implements Runnable { 
        private Socket socket; 
 
 
        /** 
         * Constructor de la clase MessageReader. 
         * @param socket  Socket del cliente. 
         */ 
        public MessageReader(Socket socket) { 
            this.socket = socket; 
        } 
 
 
        /** 
         * Inplementa el manejo de los clientes. 
         * @return No retorna ningún valor, es de tipo void. 
         */ 
        @Override 
        public void run() { 
            try { 
                Scanner scanner = new Scanner(socket.getInputStream()); 
 
                while (scanner.hasNextLine()) { 
                    String message = scanner.nextLine(); 
                    if (!message.isEmpty()) { 
                        receiveMessage(message); 
                    } 
                } 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
    } 
}
