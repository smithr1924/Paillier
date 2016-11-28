import static org.junit.Assert.*;

import org.junit.Test;

public class ElectionBoardTest {

	@Test
	public void ElectionBoardInitializationTest() {
		ElectionBoard EB = ElectionBoard.getInstance();
		assertEquals("Incorrect number of candidates!", EB.numCandidates(), 2);
		assertEquals("incorrect number of voters!", EB.numVoters(), 6);
	}

}
