// Fida Matin - a1798239 - The University of Adelaide
// 25th November 2024
// COMP SCI 3012 - Distributed Systems UG
// Assignment 3 - Paxos 

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Proposer {
    private int proposalNumber;
    private String candidate;
    private Map<String, String> promises = new ConcurrentHashMap<>();
    private int quorum;
    private final Map<String, String> acceptResponses = new HashMap<>();
    private List<String> servers;

    public Proposer(String candidate, int initialProposalNumber) {
        this.candidate = candidate;
        this.proposalNumber = initialProposalNumber; // Initialize with the given number
    }

    public Proposer(String candidate, int initialProposalNumber, List<String> servers) {
        this.candidate = candidate;
        this.proposalNumber = initialProposalNumber;
        this.servers = servers;
    }

    public void startProposal(List<String> members) throws InterruptedException {
        while (true) {
            promises.clear(); // Clear promises for the new round
            quorum = (members.size() / 2) + 1; // Majority

            System.out.println("Proposer starting proposal #" + proposalNumber + " with candidate: " + candidate);

            for (String member : members) {
                String[] hostPort = member.split(":");
                String host = hostPort[0];
                int port = Integer.parseInt(hostPort[1]);

                System.out.println("Sending PREPARE message to " + host + ":" + port);
                String response = NodeClient.sendMessageWithResponse(host, port, PaxosMessageType.PREPARE + ":" + proposalNumber + ":" + candidate);
                if (response != null) {
                    System.out.println("Received response from " + host + ":" + port + " - " + response);
                    handlePromise(response, member); // Pass the unique member address
                }
            }

            System.out.println("Quorum required: " + quorum);
            System.out.println("Responses received: " + promises.size());

            if (promises.size() >= quorum) {
                System.out.println("Quorum reached. Moving to ACCEPT phase.");
                sendAccept(members);
                break; // Exit once consensus is reached
            } else {
                System.out.println("Quorum not reached. Retrying with higher proposal number.");
                proposalNumber++; // Increment proposal number on retry
            }
        }
    }

    public void autoProposal() throws InterruptedException {
        while (true) {
            promises.clear(); // Clear promises for the new round
            quorum = (servers.size() / 2) + 1; // Majority
    
            System.out.println("Proposer starting proposal #" + proposalNumber + " with candidate: " + candidate);
    
            for (String member : servers) {
                String[] hostPort = member.split(":");
                String host = hostPort[0];
                int port = Integer.parseInt(hostPort[1]);
    
                System.out.println("Sending PREPARE message to " + host + ":" + port);
                String response = NodeClient.sendMessageWithResponse(host, port, PaxosMessageType.PREPARE + ":" + proposalNumber + ":" + candidate);
                if (response != null) {
                    System.out.println("Received response from " + host + ":" + port + " - " + response);
                    handlePromise(response, member); // Pass the unique member address
                }
            }
    
            System.out.println("Quorum required: " + quorum);
            System.out.println("Responses received: " + promises.size());
    
            if (promises.size() >= quorum) {
                System.out.println("Quorum reached. Moving to ACCEPT phase.");
                sendAccept(servers);
                break; // Exit once consensus is reached
            } else {
                System.out.println("Quorum not reached. Retrying with higher proposal number.");
                proposalNumber++; // Increment proposal number on retry
            }
        }
    }

    private void handlePromise(String response, String memberAddress) {
        String[] parts = response.split(":");
        if (parts[0].equals(PaxosMessageType.PROMISE.name())) {
            promises.put(memberAddress, response);

            System.out.println("Processing response: " + response);
            System.out.println("Current PROMISE count: " + promises.size());
            System.out.println("Current PROMISE map: " + promises);
        }
    }

    private void sendAccept(List<String> members) throws InterruptedException {
        for (String member : members) {
            String[] hostPort = member.split(":");
            String host = hostPort[0];
            int port = Integer.parseInt(hostPort[1]);

            System.out.println("Sending ACCEPT message to " + host + ":" + port);
            String response = NodeClient.sendMessageWithResponse(host, port, PaxosMessageType.ACCEPT + ":" + proposalNumber + ":" + candidate);
            if (response != null) {
                handleAcceptResponse(response, member);
            }
        }
    }

    private void handleAcceptResponse(String response, String memberAddress) throws InterruptedException {
        String[] parts = response.split(":");
        if (parts[0].equals(PaxosMessageType.ACCEPT_OK.name())) {
            System.out.println("ACCEPT-OK received from " + memberAddress);
            acceptResponses.put(memberAddress, response);

            // Check for quorum
            if (acceptResponses.size() >= quorum) {
                System.out.println("Consensus reached! President elected: " + candidate);
                return;
            }

            System.out.println("Current ACCEPT_OK responses: " + acceptResponses.size());
            System.out.println("Responses: " + acceptResponses);

        } else if (parts[0].equals(PaxosMessageType.ACCEPT_REJECT.name())) {
            System.out.println("ACCEPT-REJECT received from " + memberAddress + 
            " for proposal: " + parts[1]);
                    
        }
    }
}


