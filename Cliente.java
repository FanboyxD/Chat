import java.io.*;
import java.net.*;

public class Cliente {
    private String serverIP;
    private int serverPort;
    private PrintWriter out;

    public Cliente(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }
    
    public void startClient() {
        try {
            Socket socket = new Socket(serverIP, serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(new MessageReceiver(in)).start();

            // Add your code here to handle sending messages from the UI
            // For example, when the user clicks the "Send" button
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public PrintWriter getPrintWriter() {
        return out;
    }
    public static void main(String[] args) {
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Ingrese la direccion IP: ");
            String serverIP = userInput.readLine();
            System.out.print("Ingrese el puerto: ");
            int serverPort = Integer.parseInt(userInput.readLine());

            Socket socket = new Socket(serverIP, serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(new MessageReceiver(in)).start();

            String message;
            while ((message = userInput.readLine()) != null) {
                out.println(message);

                // Respond to the received message
                System.out.print("Enviado: ");
                String response = userInput.readLine();
                out.println(response);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class MessageReceiver implements Runnable {
    private BufferedReader in;

    public MessageReceiver(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Recibido: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}