package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.ast.*;
import words.exceptions.WordsRuntimeException;


public class TestINodeOr extends TestINode {
	@Test
	public void logicalOrShouldGiveCorrectResults() throws WordsRuntimeException {
		INode testNode;
		ASTValue result;
		
		testNode = new INodeOr(trueLeaf, trueLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
		
		testNode = new INodeOr(falseLeaf, trueLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
		
		testNode = new INodeOr(trueLeaf, falseLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
		
		testNode = new INodeOr(falseLeaf, falseLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
}
