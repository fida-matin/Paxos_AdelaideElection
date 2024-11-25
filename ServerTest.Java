// Fida Matin - a1798239 - The University of Adelaide
// 25th November 2024
// COMP SCI 3012 - Distributed Systems UG
// Assignment 3 - Paxos 

public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(5001);
    System.out.println("Server listening on port 5001");
    while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());
    }
}
