package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.exceptions.*;

public class TestINodeAnd extends TestINode {
	@Test
	public void logicalAndShouldGiveCorrectResults() throws WordsRuntimeException {
		INode testNode;
		Variable result;
		
		testNode = new INodeAnd(trueLeaf, trueLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
		
		testNode = new INodeAnd(falseLeaf, trueLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
		
		testNode = new INodeAnd(trueLeaf, falseLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
		
		testNode = new INodeAnd(falseLeaf, falseLeaf);
		result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
}
