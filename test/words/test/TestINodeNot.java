package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.exceptions.*;

public class TestINodeNot extends TestINode {
	@Test
	public void logicalNotShouldGiveCorrectResults() throws WordsRuntimeException {
		INode testNode;
		Variable result;
		
		testNode = new INodeNot(trueLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
		
		testNode = new INodeNot(falseLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
}
