package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.ast.*;
import words.exceptions.WordsOperatorTypeMismatchException;
import words.exceptions.WordsRuntimeException;


public class TestINodeGEQ extends TestINode {
	@Test
	public void numberShouldBeGEQItself() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(1.4);
		AST numLeaf2 = new LNodeNum(1.4);
		
		INode testNode = new INodeGEQ(numLeaf1, numLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test
	public void numberShouldBeGEQAnother() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(4.7);
		AST numLeaf2 = new LNodeNum(1.4);
		
		INode testNode = new INodeGEQ(numLeaf1, numLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test
	public void stringShouldBeGEQItself() throws WordsRuntimeException {
		AST stringLeaf1 = new LNodeString("string");
		AST stringLeaf2 = new LNodeString("string");
		
		INode testNode = new INodeGEQ(stringLeaf1, stringLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test
	public void stringShouldBeGEQAnother() throws WordsRuntimeException {
		AST stringLeaf1 = new LNodeString("fghij");
		AST stringLeaf2 = new LNodeString("abcde");
		
		INode testNode = new INodeGEQ(stringLeaf1, stringLeaf2);
		ASTValue result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, ASTValue.ValueType.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test (expected = WordsOperatorTypeMismatchException.class)
	public void numberAndStringsShouldNotBeGEQ() throws WordsRuntimeException {
		INode testNode = new INodeGEQ(numLeaf, stringLeaf);
		ASTValue result = testNode.eval(environment);
	}
}
