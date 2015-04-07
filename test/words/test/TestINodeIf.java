package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.ast.*;
import words.environment.WordsPosition;
import words.exceptions.WordsRuntimeException;


public class TestINodeIf extends TestINode {
	@Test
	public void testIfOnTrue() throws WordsRuntimeException {
		AST statementList = new INodeStatementList(moveFredRight2);
		AST relationalExp = new INodeEquals(numLeaf, numLeaf);
		AST testNode = new INodeIf(relationalExp, statementList);
		
		environment.createObject("Fred", "thing", new WordsPosition(0,0));
		loop.fastForwardEnvironment(1); //object is created with a 1 frame wait, so use it up. 
		loop.enqueueAST(testNode);
		loop.fastForwardEnvironment(2);
		assertEquals("Fred has moved correctly", environment.getObject("Fred").getCurrentCell(), new WordsPosition(2,0));
	}
	
	@Test
	public void testIfOnFalse() throws WordsRuntimeException {
		AST statementList = new INodeStatementList(moveFredRight2);
		AST relationalExp = new INodeEquals(twoLeaf, fiveLeaf);
		AST testNode = new INodeIf(relationalExp, statementList);
		
		environment.createObject("Fred", "thing", new WordsPosition(0,0));
		loop.fastForwardEnvironment(1); //object is created with a 1 frame wait, so use it up. 
		loop.enqueueAST(testNode);
		loop.fastForwardEnvironment(2);
		assertEquals("Fred has not moved", environment.getObject("Fred").getCurrentCell(), new WordsPosition(0,0));
	}
}
