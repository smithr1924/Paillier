# Secure E-Voting with Paillier Encryption

Final group project for Cryptography and Network Security I. Implementing a voting system using Paillier encryption, zero-knowledge proofs, and blind signatures. This project assumes the following:

* There are six (6) voters and two (2) candidates, represented in the text files included in this repository.

Download and Execution Instructions:
------------------------------------

To run the JAR file:

1. Download the contents of the ```exec``` folder within this repo. Note: votes will NOT tally correctly on macOS, so make sure to run this on Windows.
2. When prompted for a voter name, the list of legal voters specified in the ```voters.txt``` file are as follows: ```a a```, ```b c```, ```c c```, ```d d```, ```e e```, ```f f```. Log in with one of these users in order to vote.

To build from source:

1. Clone or download the repo on via the button on this page.
2. Open in Eclipse.
3. Run the EVoting.java class.


Open-Source Components
----------------------

* Paillier class for Java written by Kun Liu <http://www.csee.umbc.edu/~kunliu1/research/Paillier.html>. Used in encryption and decryption for the Bulletin Board 