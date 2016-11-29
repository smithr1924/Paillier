import java.math.*;

public class Voter
{
	private static int numVoters = 0;
	private String name;
	private int id;
	private Boolean didVote = false;
	private BigInteger[] vote = new BigInteger[ElectionBoard.numCandidates()];

	// Initializer for the class
	// Params		name is the name of the voter
	//				id is a unique number assigned to this voter
	public Voter(String name) {
		if (numVoters >= ElectionBoard.numVoters())
		{
			throw new SecurityException("Maximum number of voters already reached");
		}
		
		this.name = name;
		this.id = numVoters;
		numVoters++;
	}

	public String getName()
	{
		return new String(name);
	}

	public int getID()
	{
		return new Integer(id).intValue();
	}
	
	public Boolean getVoteStatus()
	{
		return new Boolean(didVote);
	}
	
	public BigInteger[] getVote()
	{
		return vote.clone(); 
	}
	
	public void didVote(BigInteger[] encryptedVote)
	{
		if (!didVote)
		{
			didVote = true;
			vote = encryptedVote.clone();
		}
		
		else
		{
			throw new SecurityException("User "+name+" has already voted");
		}
	}
}