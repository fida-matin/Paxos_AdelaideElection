// Fida Matin - a1798239 - The University of Adelaide
// 25th November 2024
// COMP SCI 3012 - Distributed Systems UG
// Assignment 3 - Paxos 

import java.util.ArrayList;
import java.util.List;

public class ConcurrentPaxosTest {
    public static void main(String[] args) throws InterruptedException {
        // Define council members (hosts and ports)
        List<String> members = List.of(
            "127.0.0.1:5001", // M1
            "127.0.0.1:5002", // M2
            "127.0.0.1:5003", // M3
            "127.0.0.1:5004", // M4
            "127.0.0.1:5005"  // M5
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

        // Start Concurrent Proposers
        List<Thread> proposerThreads = new ArrayList<>();
        String[] candidates = {"M1", "M2", "M3"}; // Three proposers competing

        for (int i = 0; i < candidates.length; i++) {
            String candidate = candidates[i];
            int delay = i * 100; // Small delays to create contention
            int initialProposalNumber = i + 1; // Overlapping proposal numbers
        
            Thread proposerThread = new Thread(() -> {
                try {
                    Thread.sleep(delay);
                    Proposer proposer = new Proposer(candidate, initialProposalNumber);
                    proposer.startProposal(members);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        
            proposerThreads.add(proposerThread);
            proposerThread.start();
        }
           

        // Wait for acceptor threads to complete
        for (Thread thread : acceptorThreads) {
            thread.join();
        }

        System.out.println("Paxos consensus process completed.");
    }
}
