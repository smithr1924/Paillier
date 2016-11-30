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
	private BigInteger n, g;
	private Paillier encrypt;
	private static ElectionBoard EB = ElectionBoard.getInstance();
	private static BulletinBoard BB = BulletinBoard.getInstance();

	// Initializer for the class
	// Params		name is the name of the voter
	//				id is a unique number assigned to this voter
	public Voter(String name, BigInteger n, BigInteger g) {
//		if (numVoters > ElectionBoard.numVoters())
//		{
//			return;
//		}
		
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
		
		this.name = name;;
		this.n = n;
		this.g = g;
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
			
			BigInteger e = EB.getE();
			BigInteger n = EB.getN();

	        BigInteger r = new BigInteger(512, new Random());
	        rsaEncryptedVote = vote.multiply(r.pow(e.intValue())).mod(n);
	        
	        EB.receiveVote(this, rsaEncryptedVote);
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
		
		for (int i = 0; i < EB.numCandidates(); i++)
		{
			
		}
		
		BigInteger r = new BigInteger(512, 64, new Random());
		//paillierVote = encrypt.Encryption(this.vote, r);
		BB.receiveVote(this);
	}
	
	public BigInteger[] zkp(BigInteger e)
	{
		BigInteger[] answer = new BigInteger[3];
        BigInteger r = new BigInteger(512, new Random()).mod(n);
        BigInteger s = new BigInteger(512, new Random()).mod(n);

        BigInteger x = new BigInteger(512, new Random()).mod(n);
        
        answer[0] = g.pow(r.intValue()).multiply(s.pow(n.intValue())).mod(n.pow(2));
        answer[1] = r.subtract(e.multiply(vote));
        answer[2] = s.multiply(x.pow(-e.intValue())).multiply(g.pow((r.subtract(e.multiply(vote)).intValue())));
	
        return answer;
	}
}