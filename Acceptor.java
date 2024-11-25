// Fida Matin - a1798239 - The University of Adelaide
// 25th November 2024
// COMP SCI 3012 - Distributed Systems UG
// Assignment 3 - Paxos 

/*
 * Acceptor class implementation
 * Creates an implementation for handling and receiving proposer messagas
 * A message handler verifies the type and then runs suitable function depending on the type:
 *  - prepare messages - handle prepare to send promise message
 *  - accept - handle accept messages to send accept-ok or accept-reject messages
 */
public class Acceptor implements NodeServer.MessageHandler {
    private int promisedProposalNumber = -1;
    private int acceptedProposalNumber = -1;
    private String acceptedCandidate = null;
    private Learner learner;
    private int delay = 0;

    // basic constructor
    public Acceptor(Learner learner) {
        this.learner = learner;
    }

    // constructor with delay for testing delay behaviour
    public Acceptor(Learner learner, int delay) {
        this.learner = learner;
        this.delay = delay;
    }

    // message handler override from node server
    @Override
    public String handleMessage(String message) {
        // -1 delay is set for dropped requests
        if (delay == -1) {
            System.out.println("Dropping request (no response).");
            return "Dropping request (no response).";// No response
        }
        try {
            Thread.sleep(delay); // Simulate delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // parse message to get all required data
        String[] parts = message.split(":");
        PaxosMessageType type = PaxosMessageType.valueOf(parts[0]);
        int proposalNumber = Integer.parseInt(parts[1]);
        String candidate = parts.length > 2 ? parts[2] : null;

        // run correct function based on message types
        switch (type) {
            case PREPARE:
                return handlePrepare(proposalNumber);
            case ACCEPT:
                return handleAccept(proposalNumber, candidate);
            default:
                return null;
        }
    }

    // prepare message handler
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

    // accept message handler
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


