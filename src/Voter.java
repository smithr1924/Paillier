public class Voter
{
	private String name;
	private int id;

	public Voter() {

	}

	public Voter(String n)
	{
		name = n;
	}

	public static String getName()
	{
		return name;
	}

	public static int getID()
	{
		return id;
	}
}