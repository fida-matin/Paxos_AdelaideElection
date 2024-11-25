# Paxos - Adelaide Council Election
Fida Matin - a1798239 - The University of Adelaide
25th November 2024
COMP SCI 3012 - Distributed Systems UG
Assignment 3 - Paxos 

A Paxos algorithm designed to handle 9 members in a council election

Each member (M1–M9) will act as a Proposer, Acceptor, and Learner. These roles are distributed, but the logic can be abstracted as follows:

- Proposer: Proposes a candidate (e.g., "M1 for President").
- Acceptor: Votes on proposals and ensures majority agreement.
- Learner: Learns the outcome once a majority of Acceptors agree.

Paxos Messages can have the following types:
* Prepare - Sent from Proposer to Acceptor to verify connection
* Promise - Acknowledgement to check if any previous promises have been made to other Proposers
* Accept - Sent to acceptor to accept a value
* Accepted - Sent to proposer to verify value has been accepted
* Learn - Sent to learner to check if consensus has been reached
* Accept ok - Acceptor accepts value
* Accept reject - Acceptor rejects value as there has been a previous promise made

*Use of Makefile:*

- make : compiles all java files
- make clean : removes all class files

- make run : runs integrated test of paxos
- make concurrent : run multiple proposers simultaneously
- make profiles : run proposers with several profiles (immediate response, small delay, large delay and no response)

*Files included in project:*

Creating basic client and server functionality as well as format for sending Paxos messages:
- NodeClient.java
- NodeServer.java
- PaxosMessageType.java

Implementation of Paxos roles to each node
- Proposer.java
- Acceptor.java
- Learner.java

Integrated Run of Paxos to test whether consensus can be reached
- Paxos.java
- ConcurrentPaxosTest.java
- ProfiledPaxosTest.java

Unit testing of Client and Server
- ClientTest.java
- ServerTest.java

