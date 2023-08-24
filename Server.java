import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ChatServer {

    private static final int STARTING_PORT = 12345;
    private static int nextPort = STARTING_PORT;
    private static Map<Integer, PrintWriter> clientWriters = new HashMap<>();

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

    private static class ClientHandler implements Runnable {

        private Socket clientSocket;
        private PrintWriter writer;
        private int clientPort;

        public ClientHandler(Socket clientSocket, PrintWriter writer, int clientPort) {
            this.clientSocket = clientSocket;
            this.writer = writer;
            this.clientPort = clientPort;
        }

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
                            if (receiverPort != clientPort) {
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
