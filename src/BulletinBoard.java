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
//		BigInteger signedVote = voter.getSignedVote();
		BigInteger signedVote = new BigInteger("7");
		BigInteger vote = voter.getPaillierVote();
		
		if (zkpSigned(voter, signedVote))
		{
			if (zkpPaillier(voter, vote))
			{
				matrix[voter.getID()] = vote;
				System.out.println("woop");
				return true;
			}
			
			else
			{
				System.out.println("Paillier encrypted vote did not pass the ZKP! Vote rejected.");
				return false;
			}
		}
		
		else
		{
			System.out.println("Signed vote did not pass the ZKP! Vote rejected.");
			return false;
		}
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
	public Boolean zkpSigned(Voter voter, BigInteger vote)
	{
		BigInteger e = new BigInteger("2");
//		do {
//			e = new BigInteger(2, new Random()).mod(EB.getPaillierN());
//		} while (e == BigInteger.ZERO);
		
		BigInteger[] results = voter.zkpSigned(e);
		System.out.println("here");
		BigInteger u = results[0];
		BigInteger v = results[1];
		BigInteger w = results[2];
//		BigInteger g = EB.getE();
		BigInteger g = new BigInteger("7");
		System.out.println("v: "+v+" g: "+g+" c: "+vote+" u: "+u+" e: "+e);
		System.out.println("here1");

//		BigInteger n = EB.getN();
		BigInteger n = new BigInteger("6");
		BigInteger nSquared = n.pow(2);
		
		System.out.println("n^2:            "+nSquared);

		BigInteger nicosInt = g.modPow(v, nSquared);
		System.out.println("g^v mod n^2:    "+nicosInt);
		nicosInt = nicosInt.multiply(vote.modPow(e.negate(), nSquared)).mod(nSquared);
		System.out.println("vote^e mod n^2: " + vote.modPow(e.negate(), nSquared).mod(nSquared));
		nicosInt = nicosInt.mod(nSquared);
		nicosInt = nicosInt.multiply(w.modPow(n, nSquared)).mod(nSquared);
		System.out.println("w^n mod n^2:    "+w.modPow(n, nSquared).mod(nSquared));
		nicosInt = nicosInt.mod(nSquared);

		Boolean answer = nicosInt.equals(u);

		System.out.println("u:              "+u);
		System.out.println("newU:           "+nicosInt);
		System.out.println("nicosInt == u: " + answer);

		return answer;
	}
	
	// Zero Knowledge Proof function
	// Don't even know if it will be necessary, but it's here
	// if we need it.
	public Boolean zkpPaillier(Voter voter, BigInteger vote)
	{
		BigInteger e;
		do {
			e = new BigInteger(2, new Random()).mod(EB.getPaillierN());
		} while (e == BigInteger.ZERO);
		BigInteger[] results = voter.zkpPaillier(e);
		BigInteger u = results[0];
		BigInteger v = results[1];
		BigInteger w = results[2];
		BigInteger g = EB.getPaillierG();
		System.out.println("g: "+g+" c: "+vote+" u: "+u+" vote: "+vote+" w: "+ w);

		BigInteger n = EB.getPaillierN();
		BigInteger nSquared = n.pow(2);	
		System.out.println("n2:"+nSquared);
		BigInteger nicosInt = g.modPow(v, nSquared);
		System.out.println("g^v mod n^2: "+nicosInt);
		nicosInt = nicosInt.multiply(vote.modPow(e.negate(), nSquared));
		System.out.println("vote^e mod n^2: " + vote.modPow(e.negate(), nSquared));
		nicosInt = nicosInt.mod(nSquared);
		nicosInt = nicosInt.multiply(w.modPow(n, nSquared)).mod(nSquared);
		System.out.println("w^n mod n^2:"+nicosInt);
		nicosInt = nicosInt.mod(nSquared);
		
		System.out.println("nicosInt: "+nicosInt);
		System.out.println("u:        "+u);

		Boolean answer = nicosInt.equals(u);

		System.out.println("nicosInt == u: " + answer);

		return answer;

		// Boolean answerA = u.mod(EB.getPaillierG().pow(v.intValue())).mod(EB.getPaillierN().pow(2)) == (BigInteger.ZERO);
		// Boolean answerB = u.mod(vote.pow(e.intValue())) == (BigInteger.ZERO);
		// Boolean answerC = u.mod(w.pow(EB.getPaillierN().intValue())).mod(EB.getPaillierN().pow(2)) == (BigInteger.ZERO);
		// System.out.println("a: "+answerA+" b: "+answerB+" c: "+answerC);
		
		// System.out.println("a: "+u.mod(EB.getPaillierG().pow(v.intValue()))+" b: "+answerB+" c: "+u.mod(w.pow(EB.getPaillierN().intValue())));
		
		// Boolean answer = (u.mod(EB.getPaillierG().pow(v.intValue())) == BigInteger.ZERO
		// 		&& u.mod(vote.pow(e.intValue())) == BigInteger.ZERO && u.mod(w.pow(EB.getPaillierN().intValue())) == BigInteger.ZERO);
//		System.out.println("here2");
//		newU = newU.multiply(vote.pow(e.intValue()));
//		System.out.println("here3");
//		newU = newU.multiply(w.pow(EB.getPaillierN().intValue()));
//		System.out.println("here4");
		
//		System.out.println("u: "+u);
////		System.out.println("newu: "+newU);
//		Boolean answer = u == newU;
		// System.out.println(answer);
		// return answer;
	}
}
