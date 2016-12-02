import java.math.*;
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
	private BigInteger n, g, x, r;
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
	
	public void successfulVote()
	{
		didVote = true;
	}
	
	public void didVote(BigInteger vote, ElectionBoard EB)
	{
		if (!didVote)
		{
			clearVote = vote;
			BigInteger[] encrypted = EB.encryptVote(clearVote);
			paillierVote = encrypted[0];
			x = encrypted[1];
			
			BigInteger e = EB.getE();
			BigInteger n = EB.getN();

			
			do {
				r = new BigInteger(8, new Random());
				System.out.println("asdkfjadsklfj r: "+r);
			} while(r.divide(r.gcd(n)).multiply(n).compareTo(BigInteger.ONE) == 0);
			
			rsaEncryptedVote = clearVote.multiply(r.pow(e.intValue())).mod(n);
			
			System.out.println("rsa: "+rsaEncryptedVote);
			
			// MOVE DAT SHIT HERE
//			(signedVote == null)
//			{
//				signedVote = vote.divide(r);
//			}
		}
		
		else
		{
			System.out.println("User "+name+" has already voted");
		}
	}
	
	public void receiveSignature(BigInteger vote)
	{
//		for (int i = 0; i < 1000; i++)
		System.out.println("Received: "+vote);
		if (signedVote == null)
		{
			System.out.println("rec sig r: "+r);
			signedVote = vote.multiply(r.modInverse(n));
			System.out.println("rec sig signed: "+signedVote);
		}
	}
	
	public BigInteger[] zkpSigned(BigInteger e)
	{
		BigInteger[] answer = new BigInteger[3];
		// BigInteger s = new BigInteger(8, new Random()).mod(n);
		// s must be coprime to n
		BigInteger x = this.r;
//		BigInteger x = new BigInteger("5");
		System.out.println("x: "+x);
		BigInteger s = r.modPow(n, n.pow(2));
//		BigInteger s = new BigInteger("4");
		BigInteger r = new BigInteger(8, new Random()).mod(n);
//		BigInteger r = new BigInteger("1");
		BigInteger g = ElectionBoard.getInstance().getE();
//		BigInteger g = new BigInteger("7");
		BigInteger n = ElectionBoard.getInstance().getN();
//		BigInteger n = new BigInteger("6");
		
		answer[0] = g.modPow(r, n.pow(2)).multiply(s.modPow(n, n.pow(2)));
		answer[0] = answer[0].mod(n.pow(2));
		
		answer[1] = r.add(e.multiply(clearVote));
		answer[2] = s.multiply(x.pow(e.intValue()));
	
		return answer;
	}
	
	public BigInteger[] zkpPaillier(BigInteger e)
	{
		BigInteger[] answer = new BigInteger[3];
		BigInteger r = new BigInteger(8, new Random()).mod(n);
		// BigInteger s = new BigInteger(8, new Random()).mod(n);
		// s must be coprime to n
		BigInteger s;
		do {
			s = new BigInteger(8, new Random()).mod(n);
		} while (s.compareTo(BigInteger.ZERO) == 0);
		System.out.println("second");
		
		System.out.println("S: "+s+" x: "+x+" e: "+e);
		
		answer[0] = g.modPow(r, n.pow(2)).multiply(s.modPow(n, n.pow(2))).mod(n.pow(2));
		answer[0] = answer[0].mod(n.pow(2));
		
		answer[1] = r.add(e.multiply(clearVote));
		answer[2] = s.multiply(x.pow(e.intValue()));
	
		return answer;
	}
}