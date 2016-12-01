import java.math.BigInteger;
import java.util.ArrayList;

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
		matrix[voter.getID()] = vote;
//		if (zkpPaillier(voter, vote) && zkpSigned(voter, signedVote))
//			matrix[voter.getID()] = new BigInteger(vote.toString());
	}
	
	public BigInteger tallyVotes()
	{
		BigInteger answer = BigInteger.ONE;
		for (int i = 1; i < matrix.length; i++)
		{
			if (matrix[i] != null)
				answer = answer.multiply(matrix[i]);
		}
		
		return answer;
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
		
		
		return true;
	}


}
