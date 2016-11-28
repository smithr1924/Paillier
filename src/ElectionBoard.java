import java.io.File;
import java.math.*;
import java.util.*;
import java.util.Scanner;

// Singleton election board class
public class ElectionBoard {
	private static final ElectionBoard instance = new ElectionBoard();
	
	// Somehow has a list of registered voters already, is that an
	// argument passed in when the program starts?
	private static List<String> candidates;
	private static List<String> voters;
	private static Dictionary<String, Boolean> voterStatus;
	
	private ElectionBoard() {
		try {
			// File myFile = new File("voters.txt");
			Scanner inFile = new Scanner(new File("voters.text"));

			String line;
			String [] data;

			candidates = new ArrayList<String>();
			voters = new ArrayList<String>();
			voterStatus = new Hashtable<String, Boolean>();

			while(inFile.hasNext())
			{
				line = inFile.nextLine();				
				data = line.split(" ");
				String newName = data[0] + " " + data[1];
				
				ElectionBoard.voters.add(newName);
				ElectionBoard.voterStatus.put(newName, false);
			}
			
			inFile.close();
			
		} catch (Exception e) {
			System.out.println("error reading in file 'voters.txt': " + e);
		}
		
		try {
			Scanner inFile = new Scanner(new File("Candidates.txt"));

			String line;
			// String [] data;

			while(inFile.hasNext()) {
				line = inFile.nextLine();
				// data = line.split(" ")
				voters.add(line);
				voterStatus.put(line, false);
			}
			
			inFile.close();
			
		} catch (Exception e) {
			System.out.println("error reading in file 'candidates.txt': " + e);
		}
	}
	
	// Returns the EM instance to be used where this class is needed
	public static ElectionBoard getInstance()
	{
		return instance;
	}
	
	// Sets voter status to false to indicate no one has voted yet if the
	// dictionary hasn't yet been filled.
	public static void initializeVoters()
	{
		if (voterStatus.isEmpty())
		{
			for (int i = 0; i < voters.size(); i++)
			{
				voterStatus.put(voters.get(i), false);
			}
		}
	}
	
	public int numVoters()
	{
		return voters.size();
	}
	
	public int numCandidates()
	{
		return candidates.size();
	}
	
	// Receives a vote to be counted
	// Params:		voter is an object indicating who voted
	//				votes is an encrypted array of voter's votes (cleartext = [0,0,1,0])
	// Modifies:	voterStatus
	// Effects:		if voter hasn't voted, value for key 'voter' in voterStatus changed to true
	//				else, do nothing.
	// Returns:		
//	public static Object receiveVote(Object voter, ArrayList<BigInteger> votes)
//	{
//		if (voterStatus.get(voter))
//		{
//			// Voter already voted once
//			return
//		}
//		
//		
//	}
}
