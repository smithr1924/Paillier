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
	private BigInteger clearVote = null;
	private BigInteger signedVote = null;
	private BigInteger n, g, x;
//	private static ElectionBoard EB;
//	private static BulletinBoard BB;

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
//		EB = ElectionBoard.getInstance();
//		BB = BulletinBoard.getInstance();
		
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
	
	public BigInteger getRSAVote()
	{
		return new BigInteger(rsaEncryptedVote.toString());
	}
	
	public BigInteger getSignedVote()
	{
		return new BigInteger(signedVote.toString());
	}
	
	public BigInteger getPaillierVote()
	{
		return new BigInteger(paillierVote.toString());
	}
	
	public void didVote(BigInteger vote, ElectionBoard EB)
	{
		if (!didVote)
		{
			didVote = true;
			clearVote = vote;
			BigInteger[] encrypted = EB.encryptVote(vote);
			paillierVote = encrypted[0];
			x = encrypted[1];
			
			BigInteger e = EB.getE();
			BigInteger n = EB.getN();

	        BigInteger r = new BigInteger(512, new Random());
	        rsaEncryptedVote = vote.multiply(r.pow(e.intValue())).mod(n);
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
	}
		
	public BigInteger[] zkp(BigInteger e)
	{
		BigInteger[] answer = new BigInteger[3];
        BigInteger r = new BigInteger(8, new Random()).mod(n);
        BigInteger s = new BigInteger(8, new Random()).mod(n);
        System.out.println("second");        
        
        System.out.println("S: "+s+" x: "+x+" e: "+e);
        
        answer[0] = g.modPow(r, n.pow(2)).multiply(s.modPow(n, n.pow(2)));
        answer[0] = answer[0].mod(n.pow(2));
        
        answer[1] = r.subtract(e.multiply(clearVote));
        answer[2] = s.multiply(x.pow(e.intValue()));
	
        return answer;
	}
}