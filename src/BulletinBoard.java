import java.math.BigInteger;
import java.util.ArrayList;
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
	
	public void receiveVote(Voter voter)
	{
		BigInteger signedVote = voter.getSignedVote();
		BigInteger vote = voter.getPaillierVote();
		
		if (zkpSigned(voter, vote))
			matrix[voter.getID()] = vote;
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
	public Boolean zkpPaillier(Voter voter, BigInteger vote)
	{
		
		
		return true;
	}
	
	// Zero Knowledge Proof function
	// Don't even know if it will be necessary, but it's here
	// if we need it.
	public Boolean zkpSigned(Voter voter, BigInteger vote)
	{

		BigInteger e = new BigInteger(2, new Random()).mod(EB.getPaillierN());
		BigInteger[] results = voter.zkp(e);
		System.out.println("here");
		BigInteger u = results[0];
		BigInteger v = results[1];
		BigInteger w = results[2];
		System.out.println("G; "+EB.getPaillierG()+" c: "+vote+" w: "+ w);
		System.out.println("here1");
		Boolean answer = (u.mod(EB.getPaillierG().pow(v.intValue())) == BigInteger.ZERO
				&& u.mod(vote.pow(e.intValue())) == BigInteger.ZERO && u.mod(w.pow(EB.getPaillierN().intValue())) == BigInteger.ZERO);
//		System.out.println("here2");
//		newU = newU.multiply(vote.pow(e.intValue()));
//		System.out.println("here3");
//		newU = newU.multiply(w.pow(EB.getPaillierN().intValue()));
//		System.out.println("here4");
		
//		System.out.println("u: "+u);
////		System.out.println("newu: "+newU);
//		Boolean answer = u == newU;
		System.out.println(answer);
		return answer;
	}


}
