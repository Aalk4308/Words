package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.exceptions.*;


public class TestINodeOr extends TestINode {
	@Test
	public void logicalOrShouldGiveCorrectResults() throws WordsRuntimeException {
		INode testNode;
		Variable result;
		
		testNode = new INodeOr(trueLeaf, trueLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
		
		testNode = new INodeOr(falseLeaf, trueLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
		
		testNode = new INodeOr(trueLeaf, falseLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
		
		testNode = new INodeOr(falseLeaf, falseLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
}
