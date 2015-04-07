package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.ast.*;
import words.exceptions.WordsRuntimeException;


public class TestINodeEquals extends TestINode {
	@Test
	public void nothingShouldEqualNothing() throws WordsRuntimeException {
		AST nothingLeaf1 = new LNodeNothing();
		AST nothingLeaf2 = new LNodeNothing();
		
		INode testNode = new INodeEquals(nothingLeaf1, nothingLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test
	public void nothingShouldNotEqualAnythingElse() throws WordsRuntimeException {
		INode testNode;
		ASTValue result;
		
		testNode = new INodeEquals(nothingLeaf, numLeaf);
		result = testNode.eval(environment);
		assertFalse("Result is false", result.booleanValue);
		
		testNode = new INodeEquals(nothingLeaf, stringLeaf);
		result = testNode.eval(environment);
		assertFalse("Result is false", result.booleanValue);
	}
	
	@Test
	public void numberShouldEqualItself() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(1.4);
		AST numLeaf2 = new LNodeNum(1.4);
		
		INode testNode = new INodeEquals(numLeaf1, numLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}

	@Test
	public void numberShouldNotEqualAnother() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(1.4);
		AST numLeaf2 = new LNodeNum(4.7);
		
		INode testNode = new INodeEquals(numLeaf1, numLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
	
	@Test
	public void stringShouldEqualItself() throws WordsRuntimeException {
		AST stringLeaf1 = new LNodeString("test");
		AST stringLeaf2 = new LNodeString("test");
		
		INode testNode = new INodeEquals(stringLeaf1, stringLeaf1);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}

	@Test
	public void stringShouldNotEqualAnother() throws WordsRuntimeException {
		AST stringLeaf1 = new LNodeString("this");
		AST stringLeaf2 = new LNodeString("that");
		
		INode testNode = new INodeEquals(stringLeaf1, stringLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
	
	@Test
	public void numberShouldEqualEquivalentString() throws WordsRuntimeException {
		AST numLeaf = new LNodeNum(1.4);
		AST stringLeaf = new LNodeString("1.4");
		
		INode testNode = new INodeEquals(numLeaf, stringLeaf);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test
	public void numberShouldNotEqualNonequivalentString() throws WordsRuntimeException {
		AST numLeaf = new LNodeNum(1.4);
		AST stringLeaf = new LNodeString("4.7");
		
		INode testNode = new INodeEquals(numLeaf, stringLeaf);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
}
