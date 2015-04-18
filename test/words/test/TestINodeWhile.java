package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.ast.*;
import words.environment.*;
import words.exceptions.WordsInvalidTypeException;
import words.exceptions.WordsObjectAlreadyExistsException;
import words.exceptions.WordsObjectNotFoundException;
import words.exceptions.WordsRuntimeException;


public class TestINodeWhile extends TestINode {
	@Test
	public void testWorkingWhile() throws WordsRuntimeException {
		environment.createObject("Fred", "thing", new WordsPosition(0,0));
		LNodeReference fredLNodeRef = new LNodeReference("Fred's");
		INodeReferenceList fredRefList = new INodeReferenceList(fredLNodeRef);
		
		AST statementList = new INodeStatementList(new INodeAssign(fredRefList, new LNodeIdentifier("row"), new LNodeNum(environment.getObject("Fred").getCurrentCell().x++)));
		AST relation = new INodeLess(new LNodeNum(environment.getObject("Fred").getCurrentCell().x), new LNodeNum(2));
		AST testNode = new INodeWhile(relation, statementList);
		
		testNode.eval(environment);
		loop.fastForwardEnvironment(1);
		assertEquals("Fred has moved correctly", environment.getObject("Fred").getCurrentCell(), new WordsPosition(2,0));
	}
	
	// grammar always ensures boolean passed to while, no need to test.
	
	
}
