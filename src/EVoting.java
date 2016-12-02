/************************************************************
EVoting.java

Written by Ben Abramowitz, Ryan Smith,and Nicolaas Verbeek.
Class represents the engine that allows a user to interact 
with a UI and vote for candidates. Controls program flow and
contains instances of the other classes we created for the
project, ElectionBoard and BulletinBoard.



************************************************************/
import java.awt.Color;
import java.awt.Font;
import java.math.BigInteger;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class EVoting
{
	private static ElectionBoard EB;
	private static BulletinBoard BB;
	
	// Function to change the look of the popup windows
	public static void changeJOP()
	{
		UIManager.put("Label.font", new FontUIResource
						(new Font("Times", Font.PLAIN, 26)));
		UIManager.put("OptionPane.messageForeground",new Color(147,112,219));
		UIManager.put("Panel.background",new Color(240,255,255));
		UIManager.put("OptionPane.background",new Color(46,139,87));
		UIManager.put("Button.background",new Color(32,178,170));
		UIManager.put("Button.foreground", new Color(0,0,128));
		UIManager.put("Button.font", new FontUIResource
						(new Font("Times", Font.PLAIN, 14)));
		// UIManager.put("Label.font", new FontUIResource
		// 		(new Font("Comic Sans Ms", Font.BOLD, 20)));
		// UIManager.put("OptionPane.messageForeground",new Color(0,0,255));
		// UIManager.put("Panel.background",new Color(175,238,238));
		// UIManager.put("OptionPane.background",new Color(3,144,205));
		// UIManager.put("Button.background",new Color(65,105,255));
		// UIManager.put("Button.foreground", new Color(0,0,205));
		// UIManager.put("Button.font", new FontUIResource
		// 		(new Font("Comic Sans MS", Font.BOLD, 14)));
	}

	// Menu with buttons containing candidate names. Returns the
	// user's input
	public static int candidateMenu(String[] candidates)
	{
		// String[] encrypt = {"Print", "Encrypt1", "Decrypt1", "Encrypt2",
		// 		"Decrypt2", "Exit"};
		int input = (JOptionPane.showOptionDialog(null, 
			"Vote for whom?", "Candidates",
			0, 3, null, candidates, null));
		return input;
	}

	// Menu that appears at the start, allowing logins or
	// viewing of the final election results.
	public static int startMenu()
	{
		String[] menu = {"Login and vote", "End the poll", "Exit"};
		// int input = (JOptionPane.showOptionDialog(null, 
		// 	"What would you like to do?", "Print",
		// 	0, 3, null, menu, null));
		int input = (JOptionPane.showOptionDialog(null, 
			"What would you like to do?", "E-Voting Login",
			0, JOptionPane.PLAIN_MESSAGE, null, menu, null));
		return input;
	}

	// Menu that allows the user to input their name, checks if the
	// user is valid and hasn't voted already, and lets them vote.
	public static void login(String[] candidates)
	{
		String username = JOptionPane.showInputDialog("What is your voter name?");

		// Handle invalid names, people who have already voted
		List<Voter> voters = EB.getVoters();
		Voter thisVoter;
		
		for (int i = 0; i < EB.numVoters(); i++)
		{
			if (voters.get(i).getName().equals(username))
			{
				thisVoter = voters.get(i);
				if (voters.get(i).getVoteStatus())
				{
					// Bounce the user back if they've already voted
					System.out.println("User "+thisVoter.getName()+" already voted");

					String dialog = "You have already voted!";
					String title = "Error: Invalid Vote";
					JOptionPane.showMessageDialog(null, dialog, title,
												  JOptionPane.WARNING_MESSAGE);

					return;
				}
				
				else
				{
					// User is valid and hasn't voted; allow them
					// to vote.
					vote(thisVoter, candidates);
					return;
				}
			}
		}
		
		String dialog = "Voter does not exist!";
		String title = "Error";
		JOptionPane.showMessageDialog(null, dialog, title,
									  JOptionPane.WARNING_MESSAGE);
	}

	// Function that allows the user to vote, converts the vote to the 
	public static void vote(Voter voter, String[] candidates)
	{
		int vote = candidateMenu(candidates);
		vote++;

		vote = (int)(Math.pow(10.0, (double)vote));

		// Send the vote off to be blind signed by the
		// Election Board.
		getVoteBlindSigned(voter, vote);
	}

	// Take the user's vote and have it blindly signed by
	// the Election Board.
	public static void getVoteBlindSigned(Voter voter, int vote)
	{
		String tmp = "" + vote;
		voter.didVote(new BigInteger(tmp), EB);
        BigInteger signedVote = EB.receiveVote(voter);
        voter.receiveSignature(signedVote);
        System.out.println("signed: "+signedVote);
        sendVoteToBB(voter);
	}

	// Send the vote to the bulletin board.
	public static void sendVoteToBB(Voter voter)
	{
		String dialog = "";
		if (BB.receiveVote(voter))
		{
			voter.successfulVote();
			dialog += "Thanks for voting "+voter.getName()+"!\n Have a nice day!";
			JOptionPane.showMessageDialog(null, dialog);
		}
		
		else
		{
			dialog += "Sorry "+voter.getName()+", but there was a problem";
			dialog += " with your vote!\n Please try again.";
			String warningType = "Error: Invalid Vote";
			JOptionPane.showMessageDialog(null, dialog, warningType, JOptionPane.WARNING_MESSAGE);
		}
	}

	// Show message dialog containing the results of the election.
	public static void displayElectionResults()
	{
		BigInteger encryptedResults = BB.tallyVotes(EB.getPaillierN());
		BigInteger results = EB.decryptVotes(encryptedResults);
		String display = "";
		List<String> candidates = EB.getCandidates();
		String tally = results.toString();
		System.out.println("tally: "+tally);
		
		// Create a char array and fill it with 0s to represent
		// the initial number of each candidate's vote count.
		char[] numVotes = new char[candidates.size()];
		for(int i = 0; i < numVotes.length; i++) {
			numVotes[i] = '0';
		}

		// Order of tallies are are the reverse of the order of the names
		// on the buttons. Fill out the array with the correct tallies.
		for(int i = 0, j = candidates.size()-1; i < tally.length()-1; i++, j--) {
			numVotes[j] = tally.charAt(i);
		}

		// Display the results
		for(int i = 0; i < numVotes.length; i++) {
			display += candidates.get(i) + ": " + numVotes[i];
			display += "\n";
		}

		JOptionPane.showMessageDialog(null, display);
		
		// Quit the program after the election results are displayed;
		// election is over.
		System.exit(0);
	}

	public static void main(String[] args)
	{
		changeJOP();
		
		// Singleton instances of the Election Board and the Bulletin
		// Board.
		EB = ElectionBoard.getInstance();
		BB = BulletinBoard.getInstance();
		
		BB.setSize(EB.numVoters());
		
		String[] candidates = new String[EB.numCandidates()];
		candidates = EB.getCandidates().toArray(candidates);

		int choice = startMenu();
		// Repeat the login screen as often as necessary
		while(choice != 2)
		{
			switch(choice)
			{
				case 0: login(candidates); break;
				case 1: displayElectionResults(); break;
			}
			choice = startMenu();
		}
	}
}