import java.io.File;
import java.math.*;
import java.util.*;

// Singleton election board class
public class ElectionBoard {
	private static final ElectionBoard instance = new ElectionBoard();
	
	// Somehow has a list of registered voters already, is that an
	// argument passed in when the program starts?
	private static List<String> candidates;
	private static List<Voter> voters;
	private static Paillier encrypt = new Paillier();
	
	private static BigInteger e,d,n;
	
	private ElectionBoard() {
		try {
			Scanner inFile = new Scanner(new File("voters.txt"));

			String line;
			
			List<Voter> votes = new ArrayList<Voter>();

			while(inFile.hasNext())
			{
				line = inFile.nextLine();				
				
				votes.add(new Voter(line, encrypt));
			}
			
			inFile.close();
			
			voters = Collections.unmodifiableList(votes);
			
		} catch (Exception e) {
			System.out.println("error reading in file 'voters.txt': " + e);
		}
		
		try {
			Scanner inFile = new Scanner(new File("Candidates.txt"));

			String line;
			
			List<String> cands = new ArrayList<String>();

			while(inFile.hasNext()) {
				line = inFile.nextLine();
				
				cands.add(line);
			}
			
			inFile.close();
			
			candidates = Collections.unmodifiableList(cands);
			
		} catch (Exception e) {
			System.out.println("error reading in file 'candidates.txt': " + e);
		}
		
		BigInteger p = new BigInteger(256, 64, new Random());
        BigInteger q = new BigInteger(256, 64, new Random());       
        n = p.multiply(q);
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        e = new BigInteger("65537").mod(phiN);
        while (e.gcd(phiN) != BigInteger.ONE)
        {
        	e = new BigInteger(128, new Random()).mod(phiN);
        }
    	
        d = e.modInverse(phiN);
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

	public static List<Voter> getVoters()
	{
		return Collections.unmodifiableList(voters);
	}
	
	public static BigInteger getN()
	{
		return new BigInteger(n.toString());
	}
	
	public static BigInteger getE()
	{
		return new BigInteger(e.toString());
	}
	
	// Receives a vote to be counted
	// Params:		voter is an object indicating who voted
	//				vote is an encrypted array of voter's votes (cleartext = [0,0,1,0])
	// Modifies:	voterStatus
	// Effects:		if voter hasn't voted, value for key 'voter' in voterStatus changed to true
	//				else, do nothing.
	// Returns:		null if the voter has already voted
	//				otherwise, a signed vote array
	public static BigInteger receiveVote(Voter voter, BigInteger vote)
	{
		if (voter.getVoteStatus())
		{
			// Voter already voted once
			System.out.println("Voter " + voter + " has already voted!");
			return null;
		}
		
		voter.didVote(vote);
		BigInteger answer = vote.pow(d.intValue());
		
		return answer;
	}
	
	// Receive encrypted tallies of votes, decrypt and announce
	// Params:		votes is an array of BigIntegers representing the tallied votes for each candidate.
	//					votes[0] is the first candidates tally and so on.
	public static void decryptVotes(BigInteger votes)
	{
		BigInteger answer = encrypt.Decryption(votes);
				
		EVoting.displayElectionResults(answer);
	}
}
