package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.ast.*;
import words.environment.*;
import words.exceptions.*;


public class TestINodeWhile extends TestINode {
	
	@Test
	public void testWorkingWhile() throws WordsRuntimeException {
		environment.createObject("Fred", "thing", new Position(0,0));
		LNodeReference fredLNodeRef = new LNodeReference("Fred's");
		INodeReferenceList fredRefList = new INodeReferenceList(fredLNodeRef);
		
		AST assignFredCounter = new INodeAssign(fredRefList, new LNodeIdentifier("counter"), new LNodeNum(0));
		AST retrieveFredCounter = new INodeRetrieveProperty(fredRefList, new LNodeIdentifier("counter"));
		
		assignFredCounter.eval(environment);
		// contains one statement that increments fred's counter
		AST statementList = new INodeStatementList(new INodeAssign(fredRefList, new LNodeIdentifier("counter"), new INodeAdd(retrieveFredCounter, new LNodeNum(4))));
		
		AST relation = new INodeLess(retrieveFredCounter, new LNodeNum(5));
		AST testNode = new INodeWhile(relation, statementList);
		
		testNode.eval(environment);
		loop.fastForwardEnvironment(5);
		//Counter should have been incremented by 4 two times. 
		assertEquals("Fred's counter was incremented", environment.getVariable("Fred").objValue.getProperty("counter").numValue, 8, .0001);
	}
}
