// Fida Matin - a1798239 - The University of Adelaide
// 25th November 2024
// COMP SCI 3012 - Distributed Systems UG
// Assignment 3 - Paxos 

/* Paxos Message Format:
 * All implementation files follows this structure
 * enum used to carry all message types
 * class used to implement all needed elements in a Paxos message
 */


// Paxos Messages can have the following types:
/* Prepare - Sent from Proposer to Acceptor to verify connection
 * Promise - Acknowledgement to check if any previous promises have been made to other Proposers
 * Accept - Sent to acceptor to accept a value
 * Accepted - Sent to proposer to verify value has been accepted
 * Learn - Sent to learner to check if consensus has been reached
 * Accept ok - Acceptor accepts value
 * Accept reject - Acceptor rejects value as there has been a previous promise made
 */
public enum PaxosMessageType {
    PREPARE, PROMISE, ACCEPT, ACCEPTED, LEARN, ACCEPT_OK, ACCEPT_REJECT
}
/*
 * PaxosMessage Class Implementation
 * Allows class instances to be converted to strings 
 * Also parse string messages back into class instances
 */
class PaxosMessage {
    // Initialise private properties
    private PaxosMessageType type;
    private int proposalNumber;
    private String value;

    // Constructor with message type, a counter, and proposed value
    public PaxosMessage(PaxosMessageType type, int proposalNumber, String value) {
        this.type = type;
        this.proposalNumber = proposalNumber;
        this.value = value;
    }

    // Convert PaxosMessage type to a string message
    @Override
    public String toString() {
        return type + ":" + proposalNumber + ":" + value;
    }

    // Convert string message back to PaxosMessage type
    public static PaxosMessage parse(String message) {
        String[] parts = message.split(":");
        return new PaxosMessage(
            PaxosMessageType.valueOf(parts[0]),
            Integer.parseInt(parts[1]),
            parts.length > 2 ? parts[2] : null
        );
    }
}
