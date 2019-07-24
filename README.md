# NeoTransactionInvestigation

### Summary
We started seeing an issue in our applications where data was sometimes not returned from API calls. While investigating, it appeared to be an issue where write transactions happening in one application that destroys and recreates a node in the database interfere with the read transactions from the APIs. This seems to violate the expectation that the write transaction should be self-contained and not affect the read of the same node.

We've setup a simplified version of this problem occurring. We first create a node and spin up a separate instance of the application to simply continually query for that node. We then continually run a write transaction that first deletes the node and then recreates the node with a configurable delay in between those two steps (however, the same transaction is still used in both steps).

### Setup

- Neo4j: v3.5.3 (also tried v3.5.6)
- OS: MacOS Mojave (Neo in Docker), Ubuntu 14 (Servers)
- Driver: Java v1.7.3
- Language: Scala v2.13.0

### Steps to Reproduce
[Linked Repo](https://github.com/Hoekz/NeoTransactionInvestigation). Run twice at the same time, once in write mode, and once in read mode.

`sbt "run 0"` - runs in write mode with 0ms delay between delete and create in same transaction.
`sbt "run read-mode"` - runs in read mode with a constant read of the same node and reports when the node is either found or not found.

These two commands should be run at the same time in two different terminals (better to kick off the write first, but should be very little delay between starting the read).

### Expected

No matter the delay used, the read should always see a node.

### Actual

With no or very little delay (<= 10ms), the read intermittently fails to find the node.
With a longer delay (1s), the read always finds the node.
