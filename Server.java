import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatServer {

    private static final int STARTING_PORT = 12345;
    private static int nextPort = STARTING_PORT;
    private static List<PrintWriter> clientWriters = new ArrayList<>();

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
                clientWriters.add(writer);

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

                    for (PrintWriter clientWriter : clientWriters) {
                        if (clientWriter != writer) {
                            clientWriter.println("Desde el puerto " + clientPort + ": " + message);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
