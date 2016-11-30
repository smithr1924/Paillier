import static org.junit.Assert.*;

import org.junit.Test;

public class ElectionBoardTest {

	@Test
	public void ElectionBoardInitializationVoterTest() {
		assertEquals("incorrect number of voters!", 6, ElectionBoard.numVoters());
	}
	
	@Test
	public void ElectionBoardInitCandidateTest() {
		assertEquals("Incorrect number of candidates!", 2, ElectionBoard.numCandidates());
	}

}
