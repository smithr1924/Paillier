import java.math.BigInteger;
import java.util.ArrayList;

public class BulletinBoard {
	private static final BulletinBoard instance = new BulletinBoard();
	
	private static BigInteger[][] matrix;
	
	private BulletinBoard() 
	{
		int m = ElectionBoard.numCandidates();
		int n = ElectionBoard.numVoters();
		matrix = new BigInteger[n][m];
	}
	
	public static BulletinBoard getInstance()
	{
		return instance;
	}
	
	public static void receiveVote(Voter voter, BigInteger[] vote)
	{
		if (zkp(voter, vote))
			matrix[voter.getID()] = vote.clone();
	}
	
	public static void tallyVotes()
	{
		
	}

	// Zero Knowledge Proof function
	// Don't even know if it will be necessary, but it's here
	// if we need it.
	public static Boolean zkp(Voter voter, BigInteger[] vote)
	{
		
		
		return true;
	}

}
