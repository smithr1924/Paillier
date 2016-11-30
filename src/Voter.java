import java.math.*;
import java.util.List;
import java.util.Random;

public class Voter
{
	private static int numVoters = 0;
	private String name;
	private int id;
	private Boolean didVote = false;
	private BigInteger rsaEncryptedVote = null;
	private BigInteger paillierVote = null;
	private BigInteger vote = null;
	private BigInteger signedVote = null;
	private Paillier encrypt;

	// Initializer for the class
	// Params		name is the name of the voter
	//				id is a unique number assigned to this voter
	public Voter(String name, Paillier p) {
		if (numVoters >= ElectionBoard.numVoters())
		{
			return;
		}
		
//		List<Voter> voters = ElectionBoard.getVoters();
//		for (int i = 0; i < ElectionBoard.numVoters(); i++)
//		{
//			if (voters.get(i).getName().equals(name))
//			{
//				if (voters.get(i).getVoteStatus())
//				{
//					return;
//				}
//				break;
//			}
//		}
		
		this.name = name;
		encrypt = p;
		id = numVoters;
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
	
	public BigInteger getVote()
	{
		return new BigInteger(rsaEncryptedVote.toString());
	}
	
	public BigInteger getSignedVote()
	{
		return new BigInteger(signedVote.toString());
	}
	
	public void didVote(BigInteger vote)
	{
		if (!didVote)
		{
			didVote = true;
			this.vote = vote;
			
			BigInteger e = ElectionBoard.getE();
			BigInteger n = ElectionBoard.getN();

	        BigInteger r = new BigInteger(512, new Random());
	        rsaEncryptedVote = vote.multiply(r.pow(e.intValue())).mod(n);
	        
	        ElectionBoard.receiveVote(this, rsaEncryptedVote);
		}
		
		else
		{
			System.out.println("User "+name+" has already voted");
		}
	}
	
	public void receiveSignature(BigInteger vote)
	{
		if (signedVote == null)
		{
			signedVote = vote;
		}
		
		for (int i = 0; i < ElectionBoard.numCandidates(); i++)
		{
			
		}
		
		BigInteger r = new BigInteger(512, 64, new Random());
		paillierVote = encrypt.Encryption(this.vote, r);
		BulletinBoard.receiveVote(this);
	}
	
	public BigInteger[][] zkp(BigInteger e)
	{
		BigInteger[][] answer = new BigInteger[ElectionBoard.numCandidates()][3];
        BigInteger r = new BigInteger(512, new Random()).mod(encrypt.getN());
        BigInteger s = new BigInteger(512, new Random()).mod(encrypt.getN());

        BigInteger x = new BigInteger(512, new Random()).mod(encrypt.getN());
        
        answer[0] = encrypt.getG().pow(r.intValue()).multiply(s.pow(encrypt.getN().intValue())).mod(encrypt.getN().pow(2));
        answer[1] = r.subtract(e.multiply());
	
	}
}