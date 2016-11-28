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
	
	public static void receiveVote(int voter, BigInteger[] vote)
	{
		
	}
	
	public static 
}
