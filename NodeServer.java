// Fida Matin - a1798239 - The University of Adelaide
// 25th November 2024
// COMP SCI 3012 - Distributed Systems UG
// Assignment 3 - Paxos 

/*
 * Base Server Implementation
 * creates a client handler and a method for starting server
 * Takes in an acceptor as a message handler
 */
import java.io.*;
import java.net.*;

public class NodeServer {
    private int port;
    private MessageHandler messageHandler;

    public NodeServer(int port, MessageHandler messageHandler) {
        this.port = port;
        this.messageHandler = messageHandler;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("NodeServer listening on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());
                new Thread(new ClientHandler(clientSocket, messageHandler)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error on port " + port + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private MessageHandler messageHandler;

        public ClientHandler(Socket socket, MessageHandler messageHandler) {
            this.socket = socket;
            this.messageHandler = messageHandler;
        }

        @Override
        public void run() {
            System.out.println("Client connected: " + socket.getRemoteSocketAddress());
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received message: " + message);
                    String response = messageHandler.handleMessage(message);
                    if (response != null) {
                        System.out.println("Sending response: " + response);
                        out.println(response);
                        out.flush();
                    }
                }
            } catch (IOException e) {
                System.err.println("Client disconnected: " + e.getMessage());
            }
        }

    }

    public interface MessageHandler {
        String handleMessage(String message);
    }
}
