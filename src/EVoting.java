import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class EVoting
{
	// Function to change the look of the popup windows
	public static void changeJOP()
	{
		UIManager.put("Label.font", new FontUIResource
				(new Font("Comic Sans Ms", Font.BOLD, 20)));
		UIManager.put("OptionPane.messageForeground",new Color(0,0,255));
		UIManager.put("Panel.background",new Color(175,238,238));
		UIManager.put("OptionPane.background",new Color(3,144,205));
		UIManager.put("Button.background",new Color(65,105,255));
		UIManager.put("Button.foreground", new Color(0,0,205));
		UIManager.put("Button.font", new FontUIResource
				(new Font("Comic Sans MS", Font.BOLD, 14)));
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

	public static int candidateMenu(String[] candidates, int size)
	{
		// String[] encrypt = {"Print", "Encrypt1", "Decrypt1", "Encrypt2",
		// 		"Decrypt2", "Exit"};
		int input = (JOptionPane.showOptionDialog(null, 
			"Vote for whom?", "Print",
			0, size, null, candidates, null));
		return input;
	}

	public static int startMenu()
	{
		String[] menu = {"Login and vote", "End the poll", "Exit"}
		int input = (JOptionPane.showOptionDialog(null, 
			"What would you like to do?", "Print",
			0, 3, null, candidates, null));
		return input;
	}

	public static void login()
	{
		String a = JOptionPane.showInputDialog("What is your voter name?");

		// Handle invalid names, people who have already voted
		// If that's valid, call vote method, maybe with their name?

		vote();
	}

	public static void vote()
	{
		int vote = candidateMenu(candidates, numCandidates);
	}

	public static void main(String[] args)
	{
		// changeJOP();
		// This stuff will have to be in a while loop of some sort 
		int choice = startMenu()
		while(choice != 2)
		{
			switch(choice)
			{
				case 0: login()
				case 1: // force the final tally
			}
			choice = menu();
		}
	}
}