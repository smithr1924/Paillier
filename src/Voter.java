import java.math.*;
import java.util.Random;

public class Voter
{
	private static int numVoters = 0;
	private String name;
	private int id;
	private Boolean didVote = false;
	private BigInteger[] encryptedVote = new BigInteger[ElectionBoard.numCandidates()];
	private BigInteger[] encryptionVars;
	private Paillier encrypt;

	// Initializer for the class
	// Params		name is the name of the voter
	//				id is a unique number assigned to this voter
	public Voter(String name, Paillier p) {
		if (numVoters >= ElectionBoard.numVoters())
		{
			return;
		}
		
		
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
	
	public BigInteger[] getVote()
	{
		return encryptedVote.clone(); 
	}
	
	public void didVote(BigInteger[] vote)
	{
		if (!didVote)
		{
			didVote = true;
			
			BigInteger p = new BigInteger(256, 64, new Random());
	        BigInteger q = new BigInteger(256, 64, new Random());       
	        BigInteger n = p.multiply(q);
	        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

	        BigInteger e = new BigInteger("65537").mod(phiN);
	        while (e.gcd(phiN) != BigInteger.ONE)
	        {
	        	e = new BigInteger(128, new Random()).mod(phiN);
	        }
        	
	        BigInteger d = e.modInverse(phiN);

	        for (int i = 0; i < ElectionBoard.numCandidates(); i++)
	        {
		        
		        BigInteger r = new BigInteger(512, new Random());
		        encryptionVars[i] = r;
		        encryptedVote[i] = vote[i].multiply(r.pow(e.intValue())).mod(n);
	        }
		}
		
		else
		{
			System.out.println("User "+name+" has already voted");
		}
	}
	
	public BigInteger[] zkp(BigInteger e)
	{
		BigInteger[] answer = new BigInteger[3];
        BigInteger r = new BigInteger(512, new Random()).mod(encrypt.getN());
        BigInteger s = new BigInteger(512, new Random()).mod(encrypt.getN());
		
        answer[0] = encrypt.getG().pow(r.intValue()).multiply(s.pow(encrypt.getN().intValue())).mod(encrypt.getN().pow(2));
        answer[1] = r.subtract(e.multiply(val));
	
	}
}