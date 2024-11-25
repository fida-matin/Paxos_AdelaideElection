// Fida Matin - a1798239 - The University of Adelaide
// 25th November 2024
// COMP SCI 3012 - Distributed Systems UG
// Assignment 3 - Paxos 

import java.util.ArrayList;
import java.util.List;

public class Paxos {
    public static void main(String[] args) throws InterruptedException {
        // Define council members (hosts and ports)
        List<String> members = List.of(
            "127.0.0.1:5001", // M1
            "127.0.0.1:5002", // M2
            "127.0.0.1:5003", // M3
            "127.0.0.1:5004", // M4
            "127.0.0.1:5005", // M5
            "127.0.0.1:5006", // M6
            "127.0.0.1:5007", // M7
            "127.0.0.1:5008", // M8
            "127.0.0.1:5009"  // M9
        );

        // Initialize the learner
        Learner learner = new Learner((int)Math.ceil(members.size() / 2)); // Quorum of 3

        // Start acceptors
        List<Thread> acceptorThreads = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            int index = i;
            Thread acceptorThread = new Thread(() -> {
                String[] hostPort = members.get(index).split(":");
                int port = Integer.parseInt(hostPort[1]);

                // Pass the learner to the acceptor
                NodeServer server = new NodeServer(port, new Acceptor(learner));
                server.start();
            });
            acceptorThreads.add(acceptorThread);
            acceptorThread.start();
        }

        // Add a delay to ensure all servers are ready
        System.out.println("Waiting for servers to initialize...");
        Thread.sleep(2000); // 2 seconds delay to give time for servers to initialize

        // Start the Proposer
        System.out.println("Starting proposer thread...");
        Thread proposerThread = new Thread(() -> {
            Proposer proposer = new Proposer("M1", 1); // Proposing M1 for president
            try {
                proposer.startProposal(members);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        proposerThread.start();

        // Wait for threads to complete (for testing purposes)
        proposerThread.join();
        for (Thread thread : acceptorThreads) {
            thread.join();
        }

        System.out.println("Paxos consensus process completed.");
    }
}

