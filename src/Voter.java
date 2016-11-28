import java.util.*;

public class Voter
{
	private String name;
	private int id;
	private Boolean didVote = false;

	public Voter(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String getName()
	{
		return new String(name);
	}

	public int getID()
	{
		return new Integer(id).intValue();
	}
	
	public Boolean getVoteStatus()
	{
		return new Boolean(didVote);
	}
	
	public void didVote()
	{
		didVote = true;
	}
}