import java.io.File;
import java.math.*;
import java.util.*;

// Singleton election board class
public class ElectionBoard {
	private static ElectionBoard instance = new ElectionBoard();
	
	// Somehow has a list of registered voters already, is that an
	// argument passed in when the program starts?
	private static List<String> candidates;
	private static List<Voter> voters;
	private static Paillier encrypt;
	
	private static BigInteger e,d,n;
	
	private ElectionBoard() {		
		candidates = new ArrayList<String>();
		voters = new ArrayList<Voter>();
		try {
			File file = new File("voters.txt");
			Scanner inFile = new Scanner(file);
			
			encrypt = new Paillier();

			String line;
			String[] data;
			
			List<Voter> votes = new ArrayList<Voter>();

			while(inFile.hasNext())
			{
				line = inFile.nextLine();	
				data = line.split(" ");
				String newName = data[0] + " " + data[1];
				
				Voter newVoter = new Voter(newName, encrypt.getN(), encrypt.getG());
				votes.add(newVoter);
				System.out.println(newName);
			}
			
			inFile.close();
			
			voters = Collections.unmodifiableList(votes);
			
		} catch (Exception e) {
			System.out.println("error reading in file 'voters.txt': " + e);
		}
		
		try {
			File file = new File("candidates.txt");
			Scanner inFile = new Scanner(file);

			String line;
			
			List<String> cands = new ArrayList<String>();

			while(inFile.hasNext()) {
				line = inFile.nextLine();
				
				cands.add(line);
				System.out.println(line);
			}
			
			inFile.close();
			
			candidates = Collections.unmodifiableList(cands);
			System.out.println("here: "+candidates.size()+", "+cands.size());
			
		} catch (Exception e) {
			System.out.println("error reading in file 'candidates.txt': " + e);
		}
		
		BigInteger p = new BigInteger(256, 64, new Random());
        BigInteger q = new BigInteger(256, 64, new Random());       
        n = p.multiply(q);
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        //e = new BigInteger("65537").mod(phiN);
        e = phiN.subtract(BigInteger.ONE);
//        while (e.gcd(phiN) != BigInteger.ONE)
//        {
//        	System.out.println("plsno");
//        	e = new BigInteger(128, new Random()).mod(phiN);
//        }
    	
        d = e.modInverse(phiN);
	}
	
	// Returns the EM instance to be used where this class is needed
	public static ElectionBoard getInstance()
	{
		return instance;
	}
	
	public int numVoters()
	{
		return voters.size();
	}
	
	public int numCandidates()
	{
		return candidates.size();
	}
	
	public List<String> getCandidates()
	{
		return Collections.unmodifiableList(candidates);
	}

	public List<Voter> getVoters()
	{
		return Collections.unmodifiableList(voters);
	}
	
	public BigInteger getN()
	{
		return new BigInteger(n.toString());
	}
	
	public BigInteger getE()
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
	public BigInteger receiveVote(Voter voter, BigInteger vote)
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
	public BigInteger decryptVotes(BigInteger votes)
	{
		BigInteger answer = encrypt.Decryption(votes);
		return answer;
	}
}
