// Fida Matin - a1798239 - The University of Adelaide
// 25th November 2024
// COMP SCI 3012 - Distributed Systems UG
// Assignment 3 - Paxos 

/*
 * Base Client Implementation
 * Send messages to server with either returning nothing or the response
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NodeClient {
    // void instance of send message function
    public static void sendMessage(String host, int port, String message) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 5000); // 5 seconds connection timeout
            System.out.println("Connected to " + host + ":" + port);
    
            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                System.out.println("Sending message to " + host + ":" + port + " - " + message);
                out.println(message);
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Error sending message to " + host + ":" + port + " - " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // send message function with a response string returned
    public static String sendMessageWithResponse(String host, int port, String message) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 5000); // 5 seconds connection timeout
            System.out.println("Connected to " + host + ":" + port);

            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                System.out.println("Sending message to " + host + ":" + port + " - " + message);
                out.println(message);
                out.flush();

                // Read the server's response
                String response = in.readLine();
                System.out.println("Received response from server: " + response);
                return response;
            }
        } catch (IOException e) {
            System.err.println("Error sending message to " + host + ":" + port + " - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
