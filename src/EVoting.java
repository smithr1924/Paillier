import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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

	public static int candidateMenuHARDCODE()
	{
		String[] encrypt = {"Print", "Encrypt1", "Decrypt1", "Encrypt2",
				"Decrypt2", "Exit"};
		int input = (JOptionPane.showOptionDialog(null, 
			"What do you want to do?", "Print",
			0, 3, null, encrypt, null));
		return input;
	}

	// Menu with buttons containing candidate names. Returns the
	// user's input
	public static int candidateMenu(String[] candidates)
	{
		// String[] encrypt = {"Print", "Encrypt1", "Decrypt1", "Encrypt2",
		// 		"Decrypt2", "Exit"};
		int input = (JOptionPane.showOptionDialog(null, 
			"Vote for whom?", "Print",
			0, 3, null, candidates, null));
		return input;
	}

	// Menu that appears at the start, allowing logins or
	// viewing of the final election results.
	public static int startMenu()
	{
		String[] menu = {"Login and vote", "End the poll", "Exit"};
		int input = (JOptionPane.showOptionDialog(null, 
			"What would you like to do?", "Print",
			0, 3, null, menu, null));
		return input;
	}

	// Menu that allows the user to input their name and vote
	public static void login(String[] candidates)
	{
		String username = JOptionPane.showInputDialog("What is your voter name?");

		// Handle invalid names, people who have already voted
		// If that's valid, call vote method, maybe with their name?
		
		List<Voter> voters = EB.getVoters();
		Voter thisVoter;
		for (int i = 0; i < EB.numVoters(); i++)
		{
			if (voters.get(i).getName().equals(username))
			{
				thisVoter = voters.get(i);
				if (voters.get(i).getVoteStatus())
				{
					System.out.println("User "+thisVoter.getName()+" already voted");
					return;
				}
				
				else
				{
					vote(thisVoter, candidates);
				}
			}
		}

	}

	public static void vote(Voter voter, String[] candidates)
	{
		int vote = candidateMenu(candidates);

		vote = (int)(Math.pow(10.0, (double)vote));

		// Process the vote
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
        System.out.println("signed: ");
        sendVoteToBB(voter);
	}

	// Send the vote to the bulletin board.
	public static void sendVoteToBB(Voter voter)
	{
		BB.receiveVote(voter);
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
		
		for(int i = 0; i < tally.length(); i++)
		{
			display += candidates.get(tally.length()-1-i) + ": " + tally.charAt(i);
			display += "\n";
		}

		JOptionPane.showMessageDialog(null, display);
		
		System.exit(0);
	}

	public static void main(String[] args)
	{
		changeJOP(); // Uncomment this to enforce the colors/fonts
		
		EB = ElectionBoard.getInstance();
		BB = BulletinBoard.getInstance();
		
		BB.setSize(EB.numVoters());
		// Repeat the login screen as often as necessary
		
		String[] candidates = new String[EB.numCandidates()];
		candidates = EB.getCandidates().toArray(candidates);
		int choice = startMenu();
		while(choice != 2)
		{
			switch(choice)
			{
				case 0: login(candidates); break;
				case 1: displayElectionResults(); break;
			}
			choice = startMenu();
		}

		// force the final tally somehow, print election results to
		// the console

		// allow user to verify that their vote was obtained correctly
	}
}