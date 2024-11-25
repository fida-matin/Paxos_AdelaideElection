## Fida Matin - a1798239 - The University of Adelaide
## 25th November 2024
## COMP SCI 3012 - Distributed Systems UG
## Assignment 3 - Paxos 

# Compiler and flags
JAVAC = javac
JAVA = java
JFLAGS = -g

# Source files
SOURCES = Acceptor.java \
          Learner.java \
          NodeClient.java \
          NodeServer.java \
          Paxos.java \
          PaxosMessageType.java \
          Proposer.java \
		  ConcurrentPaxosTest.java \
		  ProfiledPaxosTest.java \

# Class files (created from Source files)
CLASSES = $(SOURCES:.java=.class)

# Default target: build all classes
all: $(CLASSES)

# Rule to compile .java files to .class files
%.class: %.java
	$(JAVAC) $(JFLAGS) $<

# Run integrated test of whole paxos process
run: all
	$(JAVA) Paxos

# run test for multiple proposers
concurrent: all
	$(JAVA) ConcurrentPaxosTest

# run test for different roles (immediate response, small delay, large delay and no response)
profiles: all
	$(JAVA) ProfiledPaxosTest

# Clean up compiled class files
clean:
	rm -f *.class
