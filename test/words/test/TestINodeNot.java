package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.ast.*;
import words.exceptions.WordsRuntimeException;

public class TestINodeNot extends TestINode {
	@Test
	public void logicalNotShouldGiveCorrectResults() throws WordsRuntimeException {
		INode testNode;
		ASTValue result;
		
		testNode = new INodeNot(trueLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
		
		testNode = new INodeNot(falseLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
}
