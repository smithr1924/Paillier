import java.math.BigInteger;
import java.util.Random;

public class BulletinBoard {
	private static BulletinBoard instance = new BulletinBoard();
	private static ElectionBoard EB = ElectionBoard.getInstance();
	
	private static BigInteger[] matrix;
	
	private BulletinBoard() 
	{}
	
	public static BulletinBoard getInstance()
	{
		return instance;
	}
	
	public void setSize(int n)
	{
		matrix = new BigInteger[n];
	}
	
	public boolean receiveVote(Voter voter)
	{
//		for(int i = 0; i < 10000000; i++)
//		System.out.println("THE VOTER'S SIGNED VOTE: "+ signedVote);
		
		//if (zkpSigned(voter))
		//{
			if (zkpPaillier(voter))
			{
				matrix[voter.getID()] = voter.getPaillierVote();
//				System.out.println("woop");
				return true;
			}
			
			else
			{
				System.out.println("Paillier encrypted vote did not pass the ZKP! Vote rejected.");
				return false;
			}
		//}
		
//		else
//		{
//			System.out.println("Signed vote did not pass the ZKP! Vote rejected.");
//			return false;
//		}
	}
	
	public BigInteger tallyVotes(BigInteger n)
	{
		BigInteger answer = BigInteger.ONE;
		for (int i = 0; i < matrix.length; i++)
		{
			if (matrix[i] != null)
				answer = answer.multiply(matrix[i]);
		}
		
		return answer.mod(n.pow(2));
	}

	// Zero Knowledge Proof function
	// Don't even know if it will be necessary, but it's here
	// if we need it.
//	public Boolean zkpSigned(Voter voter)
//	{
//		BigInteger vote = voter.getSignedVote();
//		BigInteger e;
//		do {
//			e = new BigInteger(2, new Random()).mod(EB.getPaillierN());
//		} while (e == BigInteger.ZERO);
//		
//		BigInteger[] results = voter.zkpSigned(e);
//		System.out.println("vote: "+vote);
//		BigInteger u = results[0];
//		BigInteger v = results[1];
//		BigInteger w = results[2];
//		BigInteger g = EB.getE();
////		BigInteger g = new BigInteger("7");
//		System.out.println("v: "+v+" g: "+g+" c: "+vote+" u: "+u+" e: "+e);
//		System.out.println("here1");
//
//		BigInteger n = EB.getN();
////		BigInteger n = new BigInteger("6");
//		BigInteger nSquared = n.pow(2);
//		
//		System.out.println("n^2:            "+nSquared);
//
//		BigInteger nicosInt = g.modPow(v, nSquared);
//		System.out.println("g^v mod n^2:    "+nicosInt);
//		nicosInt = nicosInt.multiply(vote.modPow(e.negate(), nSquared)).mod(nSquared);
//		System.out.println("vote^e mod n^2: " + vote.modPow(e.negate(), nSquared).mod(nSquared));
//		nicosInt = nicosInt.mod(nSquared);
//		nicosInt = nicosInt.multiply(w.modPow(n, nSquared)).mod(nSquared);
//		System.out.println("w^n mod n^2:    "+w.modPow(n, nSquared).mod(nSquared));
//		nicosInt = nicosInt.mod(nSquared);
//
//		Boolean answer = nicosInt.equals(u);
//
//		System.out.println("u:              "+u);
//		System.out.println("newU:           "+nicosInt);
//		System.out.println("nicosInt == u: " + answer);
//
//		return answer;
//	}
	
	// Zero Knowledge Proof function
	// Don't even know if it will be necessary, but it's here
	// if we need it.
	public Boolean zkpPaillier(Voter voter)
	{
		Boolean answer;
		for (int i = 0; i < 20; i++)
		{
			BigInteger vote = voter.getPaillierVote();
			BigInteger e;
//			System.out.println("this loops");
			do {
				e = new BigInteger(8, new Random()).mod(EB.getPaillierN());
			} while (e.equals(BigInteger.ZERO));
			BigInteger u = voter.getU();
			BigInteger[] results = voter.zkpPaillier(e);
			BigInteger v = results[6];
			BigInteger w = results[7];
			BigInteger d = results[2];
			BigInteger a = results[0];
//			BigInteger b = results[1];
			BigInteger l = results[3];
//			BigInteger z = results[4];
			BigInteger f = results[5];
			BigInteger g = EB.getPaillierG();
//			System.out.println("g: "+g+" c: "+vote+" u: "+u+" vote: "+vote+" w: "+ w+" d: "+d+" z: "+z);
//			System.out.println("f: "+f+" l: "+l+" z: "+z+" a: "+a+" b: "+b);
			
			BigInteger n = EB.getPaillierN();
			BigInteger nSquared = n.pow(2);	
//			System.out.println("n2:"+nSquared);
			BigInteger nicosInt = g.modPow(v, nSquared);
//			System.out.println("g^v mod n^2: "+nicosInt);
			nicosInt = nicosInt.multiply(vote.modPow(e.negate(), nSquared));
//			System.out.println("vote^e mod n^2: " + vote.modPow(e.negate(), nSquared));
			nicosInt = nicosInt.mod(nSquared);
			nicosInt = nicosInt.multiply(w.modPow(n, nSquared)).mod(nSquared);
//			System.out.println("w^n mod n^2:"+nicosInt);
			nicosInt = nicosInt.mod(nSquared);
			
//			System.out.println("nicosInt: "+nicosInt);
//			System.out.println("u:        "+u);
	
			answer = nicosInt.equals(u);	
//			System.out.println("first check: "+answer);
			BigInteger left = u.modPow(l, nSquared).multiply(f.modPow(n, nSquared)).mod(nSquared);
			BigInteger right = (a.multiply(d.modPow(e, nSquared))).mod(nSquared);
			
//			System.out.println("left: "+left+", right: "+right);
			
//			answer = g.modPow(l, nSquared).multiply(z.modPow(n, nSquared)).mod(nSquared).equals((b.multiply(vote.modPow(e, nSquared))).mod(nSquared));
//			System.out.println("second check: "+answer);
			answer = left.equals(right);
			System.out.println("third check: "+answer);
			
			if (answer == false)
			{
				return false;
			}
	
//			System.out.println("nicosInt == u: " + answer);
			
		}

		return true;
	}
}
