package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.ast.*;
import words.exceptions.WordsRuntimeException;


public class TestINodeAnd extends TestINode {
	@Test
	public void logicalAndShouldGiveCorrectResults() throws WordsRuntimeException {
		INode testNode;
		ASTValue result;
		
		testNode = new INodeAnd(trueLeaf, trueLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
		
		testNode = new INodeAnd(falseLeaf, trueLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
		
		testNode = new INodeAnd(trueLeaf, falseLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
		
		testNode = new INodeAnd(falseLeaf, falseLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
}
