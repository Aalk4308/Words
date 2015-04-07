package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.ast.*;
import words.exceptions.WordsOperatorTypeMismatchException;
import words.exceptions.WordsRuntimeException;


public class TestINodeGreater extends TestINode {
	@Test
	public void numberShouldNotBeGreaterItself() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(1.4);
		AST numLeaf2 = new LNodeNum(1.4);
		
		INode testNode = new INodeGreater(numLeaf1, numLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
	
	@Test
	public void numberShouldBeGreaterAnother() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(4.7);
		AST numLeaf2 = new LNodeNum(1.4);
		
		INode testNode = new INodeGreater(numLeaf1, numLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test
	public void stringShouldNotBeGreaterItself() throws WordsRuntimeException {
		AST stringLeaf1 = new LNodeString("string");
		AST stringLeaf2 = new LNodeString("string");
		
		INode testNode = new INodeGreater(stringLeaf1, stringLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
	
	@Test
	public void stringShouldBeGreaterAnother() throws WordsRuntimeException {
		AST stringLeaf1 = new LNodeString("fghij");
		AST stringLeaf2 = new LNodeString("abcde");
		
		INode testNode = new INodeGreater(stringLeaf1, stringLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test (expected = WordsOperatorTypeMismatchException.class)
	public void numberAndStringsShouldNotBeGreater() throws WordsRuntimeException {
		INode testNode = new INodeGreater(numLeaf, stringLeaf);
		ASTValue result = testNode.eval(environment);
	}
}
