import static org.junit.Assert.*;

import org.junit.Test;

public class ElectionBoardTest {

	@Test
	public void ElectionBoardInitializationVoterTest() {
		ElectionBoard EB = ElectionBoard.getInstance();
		assertEquals("incorrect number of voters!", 6, EB.numVoters());
	}
	
	@Test
	public void ElectionBoardInitCandidateTest() {
		ElectionBoard EB = ElectionBoard.getInstance();
		assertEquals("Incorrect number of candidates!", 2, EB.numCandidates());
	}

}
