package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.ast.*;
import words.exceptions.WordsOperatorTypeMismatchException;
import words.exceptions.WordsRuntimeException;


public class TestINodeLEQ extends TestINode {
	@Test
	public void numberShouldBeLEQItself() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(1.4);
		AST numLeaf2 = new LNodeNum(1.4);
		
		INode testNode = new INodeLEQ(numLeaf1, numLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test
	public void numberShouldBeLEQAnother() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(1.4);
		AST numLeaf2 = new LNodeNum(4.7);
		
		INode testNode = new INodeLEQ(numLeaf1, numLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test
	public void stringShouldBeLEQItself() throws WordsRuntimeException {
		AST stringLeaf1 = new LNodeString("string");
		AST stringLeaf2 = new LNodeString("string");
		
		INode testNode = new INodeLEQ(stringLeaf1, stringLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test
	public void stringShouldBeLEQAnother() throws WordsRuntimeException {
		AST stringLeaf1 = new LNodeString("abcde");
		AST stringLeaf2 = new LNodeString("fghij");
		
		INode testNode = new INodeLEQ(stringLeaf1, stringLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test (expected = WordsOperatorTypeMismatchException.class)
	public void numberAndStringsShouldNotBeLEQ() throws WordsRuntimeException {
		INode testNode = new INodeLEQ(numLeaf, stringLeaf);
		ASTValue result = testNode.eval(environment);
	}
}
