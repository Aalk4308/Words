package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.exceptions.*;


public class TestINodeLess extends TestINode {
	@Test
	public void numberShouldNotBeLesserItself() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(1.4);
		AST numLeaf2 = new LNodeNum(1.4);
		
		INode testNode = new INodeLess(numLeaf1, numLeaf2);
		Variable result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
	
	@Test
	public void numberShouldBeLesserAnother() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(1.4);
		AST numLeaf2 = new LNodeNum(4.7);
		
		INode testNode = new INodeLess(numLeaf1, numLeaf2);
		Variable result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test
	public void stringShouldNotBeLesserItself() throws WordsRuntimeException {
		AST stringLeaf1 = new LNodeString("string");
		AST stringLeaf2 = new LNodeString("string");
		
		INode testNode = new INodeLess(stringLeaf1, stringLeaf2);
		Variable result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
	
	@Test
	public void stringShouldBeLesserAnother() throws WordsRuntimeException {
		AST stringLeaf1 = new LNodeString("abcde");
		AST stringLeaf2 = new LNodeString("fghij");
		
		INode testNode = new INodeLess(stringLeaf1, stringLeaf2);
		Variable result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test (expected = OperatorTypeMismatchException.class)
	public void numberAndStringsShouldNotBeLesser() throws WordsRuntimeException {
		INode testNode = new INodeLess(numLeaf, stringLeaf);
		testNode.eval(environment);
	}
}
