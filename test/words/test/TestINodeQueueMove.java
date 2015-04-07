package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.environment.WordsPosition;
import words.exceptions.WordsRuntimeException;


public class TestINodeQueueMove extends TestINode {
	@Test
	public void testWorkingMove() throws WordsRuntimeException {
		environment.createObject("Fred", "thing", new WordsPosition(0,0));
		loop.fastForwardEnvironment(1); //object is created with a 1 frame wait, so use it up. 
		assertEquals("Fred in good start position", environment.getObject("Fred").getCurrentCell(), new WordsPosition(0,0));
		loop.enqueueAST(moveFredLeft2);
		loop.fastForwardEnvironment(1);
		assertEquals("Fred moved", environment.getObject("Fred").getCurrentCell(), new WordsPosition(-1,0));
		loop.fastForwardEnvironment(1);
		assertEquals("Fred moved", environment.getObject("Fred").getCurrentCell(), new WordsPosition(-2,0));
	}
}
