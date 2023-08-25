import java.io.IOException; 
import java.io.PrintWriter; 
import java.net.ServerSocket; 
import java.net.Socket; 
import java.util.HashMap; 
import java.util.Map; 
import java.util.Scanner; 
 
 
/** 
 * Clase que implementa un servidor de chat. 
 */ 
public class ChatServer { 
 
    private static final int STARTING_PORT = 12345; 
    private static int nextPort = STARTING_PORT; 
    private static Map<Integer, PrintWriter> clientWriters = new HashMap<>(); 
 
 
    /** 
     * Método principal que inicia el servidor de chat. 
     * @param args Arreglo de cadenas de texto. 
     * @return No retorna ningún valor, es de tipo void. 
     */ 
    public static void main(String[] args) { 
        try { 
            ServerSocket serverSocket = new ServerSocket(STARTING_PORT); 
            System.out.println("Servidor en ejecución. Esperando conexiones..."); 
 
            while (true) { 
                Socket clientSocket = serverSocket.accept(); 
                System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort()); 
 
                int clientPort = nextPort; 
                nextPort++; 
 
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true); 
                clientWriters.put(clientPort, writer); 
 
                Thread clientThread = new Thread(new ClientHandler(clientSocket, writer, clientPort)); 
                clientThread.start(); 
            } 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
 
 
    /** 
     * Clase interna que maneja la comunicación con un cliente. 
     */ 
    private static class ClientHandler implements Runnable { 
 
        private Socket clientSocket; 
        private PrintWriter writer; 
        private int clientPort; 
 
 
        /** 
         * Constructor de la clase ClientHandler. 
         * @param clientSocket Socket del cliente. 
         * @param writer       PrintWriter para enviar mensajes al cliente. 
         * @param clientPort   Puerto del cliente. 
         * @return No tiene un valor de retorno predeterminado, ya que es una clase que se utiliza para manejar la comunicación con un cliente en un servidor. 
         */ 
        public ClientHandler(Socket clientSocket, PrintWriter writer, int clientPort) { 
            this.clientSocket = clientSocket; 
            this.writer = writer; 
            this.clientPort = clientPort; 
        } 
 
 
        /** 
         * Establece el funcionamiento del servidor. 
         * @return No tiene un valor de retorno explícito, ya que es un método void. 
         */ 
        @Override 
        public void run() { 
            try { 
                Scanner scanner = new Scanner(clientSocket.getInputStream()); 
 
                while (true) { 
                    String message = scanner.nextLine(); 
                    System.out.println("Mensaje recibido del puerto " + clientPort + ": " + message); 
 
                    int separatorIndex = message.indexOf(":"); 
                    if (separatorIndex != -1) { 
                        int targetPort = Integer.parseInt(message.substring(0, separatorIndex)); 
                        String actualMessage = message.substring(separatorIndex + 1); 
 
                        for (Map.Entry<Integer, PrintWriter> entry : clientWriters.entrySet()) { 
                            int receiverPort = entry.getKey(); 
                            PrintWriter clientWriter = entry.getValue(); 
                            if (receiverPort == targetPort && clientWriter != writer) { 
                                clientWriter.println("Desde el puerto " + clientPort + ": " + actualMessage); 
                            } 
                        } 
                    } else { 
                        for (Map.Entry<Integer, PrintWriter> entry : clientWriters.entrySet()) { 
                            int receiverPort = entry.getKey(); 
                            PrintWriter clientWriter = entry.getValue(); 
                            if (receiverPort !=
clientPort) { 
                                clientWriter.println("Desde el puerto " + clientPort + ": " + message); 
                            } 
                        } 
                    } 
                } 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
    } 
}
