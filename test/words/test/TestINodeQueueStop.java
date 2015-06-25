package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.ast.*;
import words.environment.*;
import words.exceptions.*;


public class TestINodeQueueStop extends TestINode {
	@Test
	public void testWorkingStop() throws WordsRuntimeException {
		environment.createObject("Fred", "thing", new Position(0,0));
		loop.fastForwardEnvironment(1); //object is created with a 1 frame wait, so use it up. 
		assertEquals("Fred in good start position", environment.getVariable("Fred").objValue.getCurrentPosition(), new Position(0,0));
		environment.getVariable("Fred").objValue.enqueueAction(new MoveAction(environment.getCurrentScope(), Direction.RIGHT, new LNodeNum(1)));
		
		AST idLeaf = new LNodeIdentifier("Fred");
		INode testNode = new INodeQueueStop(new INodeReferenceList(), idLeaf);
		testNode.eval(environment);
		loop.fastForwardEnvironment(1);
		assertEquals("Fred never moved", environment.getVariable("Fred").objValue.getCurrentPosition(), new Position(0,0));
	}
}