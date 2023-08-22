// ChatClient.java
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javafx.scene.control.TextArea;

public class ChatClient {

    private Socket socket;
    private PrintWriter writer;
    private int port;
    private TextArea chatArea;

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

    public void sendMessage(String message) {
        writer.println(message);
    }

    public int getPort() {
        return port;
    }

    private void receiveMessage(String message) {
        chatArea.appendText("Recibido: " + message + "\n");
    }

    private class MessageReader implements Runnable {
        private Socket socket;

        public MessageReader(Socket socket) {
            this.socket = socket;
        }

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
