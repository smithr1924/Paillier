/************************************************************
Voter.java

Written by Ben Abramowitz, Ryan Smith, and Nicolaas Verbeek.
Class represents a Voter and contains necessary private keys
and functions to interact with the ElectionBoard and
BulletinBoard.


************************************************************/
import java.io.File;
import java.math.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
	private BigInteger n, g, x, r, u, zkpr, s;
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

		List<String> voters = new ArrayList<String>();
		
		try {
			File file = new File("voters.txt");
			Scanner inFile = new Scanner(file);

			String line;
			String[] data;
			
			while(inFile.hasNext())
			{
				line = inFile.nextLine();	
				data = line.split(" ");
				String newName = data[0] + " " + data[1];
				
				voters.add(newName);
				System.out.println(newName);
			}
			
			inFile.close();			
		} catch (Exception e) {
			System.out.println("error reading in file 'voters.txt': " + e);
		}
		
		if (voters.contains(name))
		{
			this.name = name;
			this.n = n;
			this.g = g;
			id = numVoters;
			numVoters++;
		}
		
		else
		{
			throw new IllegalArgumentException(name+" is not a verified voter!");
		}
	}

	public String getName()
	{
		return new String(name);
	}

	public int getID()
	{
		return new Integer(id).intValue();
	}
	
	// Check if the user has voted or not.
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
	
	// Function called when the voter casts their vote. Verifies that
	// the voter has not already voted and sends the vote off to be
	// signed.
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
//				System.out.println("asdkfjadsklfj r: "+r);
			} while(r.divide(r.gcd(n)).multiply(n).compareTo(BigInteger.ONE) == 0);
			
			rsaEncryptedVote = clearVote.multiply(r.pow(e.intValue())).mod(n);
			
			signedVote = EB.receiveVote(this);
			
//	        System.out.println("signed: "+signedVote);		
//			System.out.println("rsa: "+rsaEncryptedVote);
		}
		
		else
		{
			System.out.println("User "+name+" has already voted");
		}
	}
	
//	public BigInteger[] zkpSigned(BigInteger e)
//	{
//		BigInteger[] answer = new BigInteger[3];
//		// BigInteger s = new BigInteger(8, new Random()).mod(n);
//		// s must be coprime to n
//		BigInteger x = this.r;
////		BigInteger x = new BigInteger("5");
//		System.out.println("x: "+x);
//		BigInteger s = r.modPow(n, n.pow(2));
////		BigInteger s = new BigInteger("4");
//		BigInteger r = new BigInteger(8, new Random()).mod(n);
////		BigInteger r = new BigInteger("1");
//		BigInteger g = ElectionBoard.getInstance().getE();
////		BigInteger g = new BigInteger("7");
//		BigInteger n = ElectionBoard.getInstance().getN();
////		BigInteger n = new BigInteger("6");
//		
//		answer[0] = g.modPow(r, n.pow(2)).multiply(s.modPow(n, n.pow(2)));
//		answer[0] = answer[0].mod(n.pow(2));
//		
//		answer[1] = r.add(e.multiply(clearVote));
//		answer[2] = s.multiply(x.pow(e.intValue()));
//	
//		return answer;
//	}
	
	// Function to calculate u in order to send to the BulletinBoard
	// for use in the Zero-Knowledge Proofs.
	public BigInteger getU()
	{
		zkpr = new BigInteger(16, new Random()).mod(n);
		do {
			s = new BigInteger(16, new Random()).mod(n);
			System.out.println(1);
		} while(s.divide(s.gcd(n)).multiply(n).compareTo(BigInteger.ONE) == 0);
		u = g.modPow(r, n.pow(2)).multiply(s.modPow(n, n.pow(2))).mod(n.pow(2));
		u = u.mod(n.pow(2));
		
		return u;
	}
	
	public BigInteger[] zkpPaillier(BigInteger e)
	{
		BigInteger[] answer = new BigInteger[9];
//		BigInteger r = new BigInteger(16, new Random()).mod(n);
		// BigInteger s = new BigInteger(8, new Random()).mod(n);
		// s must be coprime to n
		BigInteger y = new BigInteger(16, new Random()).mod(n);
		BigInteger nSquared = n.pow(2);
		BigInteger h1, h2, newRandom;
//		do {
//			s = new BigInteger(16, new Random()).mod(n);
//			System.out.println(1);
//		} while(s.divide(s.gcd(n)).multiply(n).compareTo(BigInteger.ONE) == 0);
		
		do {
			h1 = new BigInteger(16, new Random()).mod(n);
//			System.out.println(2);
		} while(h1.divide(h1.gcd(nSquared)).multiply(nSquared).compareTo(BigInteger.ONE) == 0);
		
		do {
			h2 = new BigInteger(16, new Random()).mod(n);
//			System.out.println(3);
		} while(h2.divide(h2.gcd(nSquared)).multiply(nSquared).compareTo(BigInteger.ONE) == 0);
		
		do {
			newRandom = new BigInteger(16, new Random()).mod(n);
//			System.out.println(4);
		} while (newRandom.compareTo(BigInteger.ZERO) == 0);
		
//		System.out.println("second");
//		
//		System.out.println("S: "+s+" x: "+x+" e: "+e);
		
//		BigInteger u = g.modPow(r, n.pow(2)).multiply(s.modPow(n, n.pow(2))).mod(n.pow(2));
//		u = u.mod(n.pow(2));
		
		BigInteger v = zkpr.add(e.multiply(clearVote));
		BigInteger w = s.multiply(x.pow(e.intValue()));
		
		BigInteger d = (u.modPow(zkpr, nSquared).multiply(newRandom.modPow(n, nSquared))).mod(nSquared);
		
		BigInteger a = (u.modPow(y, nSquared)).multiply(h2.modPow(n, nSquared)).mod(nSquared);
		BigInteger b = (g.modPow(y, nSquared)).multiply(h1.modPow(n, nSquared)).mod(nSquared);
		BigInteger l = y.add(e.multiply(zkpr)).mod(n);
		
		BigInteger t = ((y.add(e.multiply(zkpr))).subtract(l)).divide(n);
		
		BigInteger z = h1.multiply(s.pow(e.intValue())).multiply(g.modPow(t, nSquared));
		BigInteger f = h2.multiply(u.pow(t.intValue())).multiply(newRandom.modPow(e, nSquared));
		
//		System.out.println("h1: "+h1+" h2: "+h2+" y: "+y+" r: "+r+" newR: "+newRandom+" t: "+t);
		
		answer[0] = a; 
		answer[1] = b; 
		answer[2] = d; 
		answer[3] = l; 
		answer[4] = z; 
		answer[5] = f; 
		answer[6] = v; 
		answer[7] = w;
			
		return answer;
	}
}