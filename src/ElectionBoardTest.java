import static org.junit.Assert.*;

import org.junit.Test;

public class ElectionBoardTest {
	ElectionBoard EB = ElectionBoard.getInstance();
	
	@Test
	public void ElectionBoardInitializationVoterTest() {
		assertEquals("incorrect number of voters!", 6, EB.numVoters());
	}
	
	@Test
	public void ElectionBoardInitCandidateTest() {
		assertEquals("Incorrect number of candidates!", 2, EB.numCandidates());
	}

}
