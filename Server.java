import java.io.*;
import java.net.*;

public class Server {
    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(12345); // Port for clients to connect
            System.out.println("El servidor del chat esta en el puerto 12345");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int clientPort = findAvailablePort(); // Find an available port for the client

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(clientPort); // Send the assigned port to the client
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(0); // Let the system choose an available port
            int port = serverSocket.getLocalPort();
            System.out.println("El servidor del chat esta en el puerto " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static int findAvailablePort() {
        for (int port = 1024; port <= 65535; port++) {
            try {
                ServerSocket socket = new ServerSocket(port);
                socket.close();
                return port;
            } catch (IOException e) {
                // Port is already in use, try the next one
            }
        }
        throw new RuntimeException("No hay puertos disponibles");
    }
    @Override
    public String toString() {
        return "Servidor []";
    }
}
class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Mensaje recibido: " + message);
                // You can implement message handling and forwarding here

                // For this basic example, let's just echo the message back
                out.println("Servidor: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}