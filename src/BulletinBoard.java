import java.math.BigInteger;
import java.util.ArrayList;

public class BulletinBoard {
	private static final BulletinBoard instance = new BulletinBoard();
	
	private static BigInteger[] matrix;
	
	private BulletinBoard() 
	{
		int n = ElectionBoard.numVoters();
		matrix = new BigInteger[n];
	}
	
	public static BulletinBoard getInstance()
	{
		return instance;
	}
	
	public static void receiveVote(Voter voter)
	{
		BigInteger signedVote = voter.getSignedVote();
		BigInteger vote = voter.getVote();
		if (zkpPaillier(voter, vote) && zkpSigned(voter, signedVote))
			matrix[voter.getID()] = new BigInteger(vote.toString());
	}
	
	public static void tallyVotes()
	{
		BigInteger answer = matrix[0];
		for (int i = 1; i < matrix.length; i++)
		{
			answer = answer.multiply(matrix[i]);
		}
		
		ElectionBoard.decryptVotes(answer);
	}

	// Zero Knowledge Proof function
	// Don't even know if it will be necessary, but it's here
	// if we need it.
	public static Boolean zkpPaillier(Voter voter, BigInteger vote)
	{
		
		
		return true;
	}
	
	// Zero Knowledge Proof function
	// Don't even know if it will be necessary, but it's here
	// if we need it.
	public static Boolean zkpSigned(Voter voter, BigInteger vote)
	{
		
		
		return true;
	}


}
