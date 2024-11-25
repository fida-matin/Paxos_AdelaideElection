// Fida Matin - a1798239 - The University of Adelaide
// 25th November 2024
// COMP SCI 3012 - Distributed Systems UG
// Assignment 3 - Paxos 

public class Acceptor implements NodeServer.MessageHandler {
    private int promisedProposalNumber = -1;
    private int acceptedProposalNumber = -1;
    private String acceptedCandidate = null;
    private Learner learner;
    private int delay = 0;
    
    public Acceptor(Learner learner) {
        this.learner = learner;
    }

    public Acceptor(Learner learner, int delay) {
        this.learner = learner;
        this.delay = delay;
    }

    @Override
    public String handleMessage(String message) {
        if (delay == -1) {
            System.out.println("Dropping request (no response).");
            return "Dropping request (no response).";// No response
        }
        try {
            Thread.sleep(delay); // Simulate delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String[] parts = message.split(":");
        PaxosMessageType type = PaxosMessageType.valueOf(parts[0]);
        int proposalNumber = Integer.parseInt(parts[1]);
        String candidate = parts.length > 2 ? parts[2] : null;

        switch (type) {
            case PREPARE:
                return handlePrepare(proposalNumber);
            case ACCEPT:
                return handleAccept(proposalNumber, candidate);
            default:
                return null;
        }
    }

    private String handlePrepare(int proposalNumber) {
        if (proposalNumber < promisedProposalNumber) {
            System.out.println("Ignoring PREPARE with proposal number " + proposalNumber + 
                               " as it is less than promised proposal number " + promisedProposalNumber);
            return null; // Ignored message
        }
    
        promisedProposalNumber = proposalNumber;
        System.out.println("PROMISE made for proposal number " + proposalNumber);
        return PaxosMessageType.PROMISE + ":" + proposalNumber + ":" + acceptedProposalNumber + ":" + acceptedCandidate;
    }

    private String handleAccept(int proposalNumber, String candidate) {
        synchronized (this) {
            if (proposalNumber < promisedProposalNumber) {
                System.out.println("ACCEPT_REJECT for proposal " + proposalNumber 
                           + " from candidate " + candidate 
                           + " as promised proposal is " + promisedProposalNumber);
                return PaxosMessageType.ACCEPT_REJECT + ":" + proposalNumber + ":" + promisedProposalNumber;
            }
        
            promisedProposalNumber = proposalNumber;
            acceptedProposalNumber = proposalNumber;
            acceptedCandidate = candidate;
        
            System.out.println("ACCEPTED proposal: " + proposalNumber + " with candidate: " + candidate);
        
            // Notify the learner
            learner.handleAccepted(candidate);
        
            return PaxosMessageType.ACCEPT_OK + ":" + proposalNumber + ":" + candidate;
        }
    }
    
}


