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
//	private static ElectionBoard EB = ElectionBoard.getInstance();
//	private static BulletinBoard BB = BulletinBoard.getInstance();
	
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

		vote(candidates);
	}

	public static void vote(String[] candidates)
	{
		int vote = candidateMenu(candidates);

		// Process the vote
		getVoteBlindSigned();
	}

	// Take the user's vote and have it blindly signed by
	// the Election Board.
	public static void getVoteBlindSigned()
	{

	}

	// Send the vote to the bulletin board.
	public static void sendVoteToBB()
	{

	}

	// Show message dialog containing the results of the election.
	public static void displayElectionResults(BigInteger results, ElectionBoard EB)
	{
		String display = "";
		List<String> candidates = EB.getCandidates();
		String tally = results.toString();

		for(int i = 0; i < candidates.size(); i++)
		{
			display += candidates.get(i) + ": " + tally.charAt(i);
			display += "\n";
		}

		JOptionPane.showMessageDialog(null, display);
	}

	public static void main(String[] args)
	{
		changeJOP(); // Uncomment this to enforce the colors/fonts
		
		System.out.println("pls");
		ElectionBoard EB = ElectionBoard.getInstance();
		// Repeat the login screen as often as necessary
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Num candidates: ");
		System.out.println(EB.numCandidates());
		String[] candidates = new String[EB.numCandidates()];
		candidates = EB.getCandidates().toArray(candidates);
		System.out.println("sdfksdjf");
		int choice = startMenu();
		while(choice != 2)
		{
			switch(choice)
			{
				case 0: login(candidates);
				case 1: // force the final tally
			}
			choice = startMenu();
		}

		// force the final tally somehow, print election results to
		// the console

		// allow user to verify that their vote was obtained correctly
	}
}