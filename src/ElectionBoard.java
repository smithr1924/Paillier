import java.io.File;
import java.math.*;
import java.util.*;
import java.util.Scanner;

// Singleton election board class
public class ElectionBoard {
	private static final ElectionBoard instance = new ElectionBoard();
	
	// Somehow has a list of registered voters already, is that an
	// argument passed in when the program starts?
	private static List<String> candidates;
	private static List<String> voters;
	private static Dictionary<String, Boolean> voterStatus;
	private static Paillier encrypt = new Paillier();
	
	private ElectionBoard() {
		try {
			// File myFile = new File("voters.txt");
			Scanner inFile = new Scanner(new File("voters.txt"));

			String line;

			candidates = new ArrayList<String>();
			voters = new ArrayList<String>();
			voterStatus = new Hashtable<String, Boolean>();

			while(inFile.hasNext())
			{
				line = inFile.nextLine();				
				
				voters.add(line);
				voterStatus.put(line, false);
			}
			
			inFile.close();
			
		} catch (Exception e) {
			System.out.println("error reading in file 'voters.txt': " + e);
		}
		
		try {
			Scanner inFile = new Scanner(new File("Candidates.txt"));

			String line;

			while(inFile.hasNext()) {
				line = inFile.nextLine();
				
				candidates.add(line);
			}
			
			inFile.close();
			
		} catch (Exception e) {
			System.out.println("error reading in file 'candidates.txt': " + e);
		}
	}
	
	// Returns the EM instance to be used where this class is needed
	public static ElectionBoard getInstance()
	{
		return instance;
	}
	
	public static int numVoters()
	{
		return voters.size();
	}
	
	public static int numCandidates()
	{
		return candidates.size();
	}
	
	public static List<String> getCandidates()
	{
		return Collections.unmodifiableList(candidates);
	}
	
	// Receives a vote to be counted
	// Params:		voter is an object indicating who voted
	//				vote is an encrypted array of voter's votes (cleartext = [0,0,1,0])
	// Modifies:	voterStatus
	// Effects:		if voter hasn't voted, value for key 'voter' in voterStatus changed to true
	//				else, do nothing.
	// Returns:		null if the voter has already voted
	//				otherwise, a signed vote array
	public static BigInteger[] receiveVote(String voter, BigInteger[] vote)
	{
		if (voterStatus.get(voter))
		{
			// Voter already voted once
			System.out.println("Voter " + voter + " has already voted!");
			return null;
		}
		
		voterStatus.put(voter, true);
		
		BigInteger[] answer = new BigInteger[ElectionBoard.numCandidates()];
		
		for (int i = 0; i < vote.length; i++)
		{
			answer[i] = encrypt.Encryption(vote[i]);
		}
		return answer;
	}
	
	// Receive encrypted tallies of votes, decrypt and announce
	// Params:		votes is an array of BigIntegers representing the tallied votes for each candidate.
	//					votes[0] is the first candidates tally and so on.
	public static void tallyVotes(BigInteger[] votes)
	{
		BigInteger[] answer = new BigInteger[numCandidates()];
		
		for (int i = 0; i < numCandidates(); i++)
		{
			answer[i] = encrypt.Decryption(votes[i]);
		}
	}
}
