package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.environment.*;
import words.exceptions.*;

public class TestINodeQueueWait extends TestINode {
	@Test
	public void testWorkingQueueWait() throws WordsRuntimeException {
		environment.createObject("Fred", "thing", new Position(0, 0));
		loop.fastForwardEnvironment(1); //object is created with a 1 frame wait, so use it up.

		AST idLeaf = new LNodeIdentifier("Fred");
		AST numLeaf = new LNodeNum(3);
		AST strLeaf = new LNodeString("Hello World");
		INode testNode = new INodeQueueWait(new INodeReferenceList(), idLeaf, numLeaf, null);
		testNode.eval(environment);
		INode sayNode = new INodeQueueSay(new INodeReferenceList(), idLeaf, strLeaf, null);
		sayNode.eval(environment);

		loop.fastForwardEnvironment(1);
		assertEquals("Wait 1 turn", environment.getVariable("Fred").objValue.getCurrentMessage(), null);
		loop.fastForwardEnvironment(1);
		assertEquals("Wait 2 turns", environment.getVariable("Fred").objValue.getCurrentMessage(), null);
		loop.fastForwardEnvironment(1);
		assertEquals("Wait 3 turns", environment.getVariable("Fred").objValue.getCurrentMessage(), null);
		loop.fastForwardEnvironment(1);
		assertEquals("End of wait", environment.getVariable("Fred").objValue.getCurrentMessage(), "Hello World");
	}

	@Test
	public void testWaitWithReferenceList() throws WordsRuntimeException {
		WordsObject alexObject = environment.createObject("Alex", "thing", new Position(0, 0));
		LNodeReference alexRef = new LNodeReference("Alex's");

		WordsObject bobObject = environment.createObject("Bob", "thing", new Position(0, 0));
		alexObject.setProperty("friend", new Variable(bobObject));
		LNodeReference friendRef = new LNodeReference("friend's");

		WordsObject chrisObject = environment.createObject("Chris", "thing", new Position(0, 0));
		bobObject.setProperty("enemy", new Variable(chrisObject));

		loop.fastForwardEnvironment(1); //object is created with a 1 frame wait, so use it up.

		AST refLeaf = new INodeReferenceList(alexRef, friendRef);
		AST idLeaf = new LNodeIdentifier("enemy");
		AST numLeaf = new LNodeNum(3);
		AST strLeaf = new LNodeString("Hello World");
		INode testNode = new INodeQueueWait(refLeaf, idLeaf, numLeaf, null);
		testNode.eval(environment);
		INode sayNode = new INodeQueueSay(refLeaf, idLeaf, strLeaf, null);
		sayNode.eval(environment);

		loop.fastForwardEnvironment(1);
		assertEquals("Wait 1 turn", environment.getVariable("Chris").objValue.getCurrentMessage(), null);
		loop.fastForwardEnvironment(1);
		assertEquals("Wait 2 turns", environment.getVariable("Chris").objValue.getCurrentMessage(), null);
		loop.fastForwardEnvironment(1);
		assertEquals("Wait 3 turns", environment.getVariable("Chris").objValue.getCurrentMessage(), null);
		loop.fastForwardEnvironment(1);
		assertEquals("End of wait", environment.getVariable("Chris").objValue.getCurrentMessage(), "Hello World");
	}

	@Test
	public void testWaitWithNow() throws WordsRuntimeException {
		environment.createObject("Fred", "thing", new Position(0, 0));
		loop.fastForwardEnvironment(1); //object is created with a 1 frame wait, so use it up.

		AST idLeaf = new LNodeIdentifier("Fred");
		AST numLeaf = new LNodeNum(3);
		AST strLeaf = new LNodeString("Queued message");
		INode testNode1 = new INodeQueueSay(new INodeReferenceList(), idLeaf, strLeaf, null);
		INode testNode2 = new INodeQueueWait(new INodeReferenceList(), idLeaf, numLeaf, new LNodeNow());
		testNode1.eval(environment);
		testNode2.eval(environment);

		loop.fastForwardEnvironment(1);
		assertEquals("Wait 1 turn", environment.getVariable("Fred").objValue.getCurrentMessage(), null);
		loop.fastForwardEnvironment(1);
		assertEquals("Wait 2 turns", environment.getVariable("Fred").objValue.getCurrentMessage(), null);
		loop.fastForwardEnvironment(1);
		assertEquals("Wait 3 turns", environment.getVariable("Fred").objValue.getCurrentMessage(), null);
		loop.fastForwardEnvironment(1);
		assertEquals("Queued message", environment.getVariable("Fred").objValue.getCurrentMessage(), "Queued message");
	}

	@Test
	public void testWaitWithExpression() throws WordsRuntimeException {
		environment.createObject("Fred", "thing", new Position(0, 0));
		loop.fastForwardEnvironment(1); //object is created with a 1 frame wait, so use it up.

		AST idLeaf = new LNodeIdentifier("Fred");
		AST numLeaf1 = new LNodeNum(1.2);
		AST numLeaf2 = new LNodeNum(2);
		AST expressionNode = new INodeSubtract(numLeaf1, new INodeNegate(numLeaf2));
		INode testNode = new INodeQueueWait(new INodeReferenceList(), idLeaf, expressionNode, null);
		testNode.eval(environment);
		INode sayNode = new INodeQueueSay(new INodeReferenceList(), idLeaf, new LNodeString("Hello World"), null);
		sayNode.eval(environment);

		loop.fastForwardEnvironment(1);
		assertEquals("Wait 1 turn", environment.getVariable("Fred").objValue.getCurrentMessage(), null);
		loop.fastForwardEnvironment(1);
		assertEquals("Wait 2 turns", environment.getVariable("Fred").objValue.getCurrentMessage(), null);
		loop.fastForwardEnvironment(1);
		assertEquals("Wait 3 turns", environment.getVariable("Fred").objValue.getCurrentMessage(), null);
		loop.fastForwardEnvironment(1);
		assertEquals("End of wait", environment.getVariable("Fred").objValue.getCurrentMessage(), "Hello World");
	}

}
