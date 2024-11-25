// Fida Matin - a1798239 - The University of Adelaide
// 25th November 2024
// COMP SCI 3012 - Distributed Systems UG
// Assignment 3 - Paxos 

public class ClientTest {
    public static void main(String[] args) {
        NodeClient.sendMessage("127.0.0.1", 5001, "Test message");
    }
}