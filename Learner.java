// Fida Matin - a1798239 - The University of Adelaide
// 25th November 2024
// COMP SCI 3012 - Distributed Systems UG
// Assignment 3 - Paxos 

/*
 * Learner Class Implementation
 * Checks from acceptors to verify whether consensus is reached
 */

import java.util.HashMap;
import java.util.Map;

public class Learner {
    private Map<String, Integer> voteCounts = new HashMap<>();
    private int quorum;

    public Learner(int quorum) {
        this.quorum = quorum;
    }

    public void handleAccepted(String candidate) {
        voteCounts.put(candidate, voteCounts.getOrDefault(candidate, 0) + 1);
        if (voteCounts.get(candidate) >= quorum) {
            System.out.println("Consensus reached! President elected: " + candidate);
        }
    }
}
