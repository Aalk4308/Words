package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.exceptions.*;


public class TestINodeEquals extends TestINode {
	@Test
	public void nothingShouldEqualNothing() throws WordsRuntimeException {
		AST nothingLeaf1 = new LNodeNothing();
		AST nothingLeaf2 = new LNodeNothing();
		
		INode testNode = new INodeEquals(nothingLeaf1, nothingLeaf2);
		Variable result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test
	public void nothingShouldNotEqualAnythingElse() throws WordsRuntimeException {
		INode testNode;
		Variable result;
		
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
		Variable result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}

	@Test
	public void numberShouldNotEqualAnother() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(1.4);
		AST numLeaf2 = new LNodeNum(4.7);
		
		INode testNode = new INodeEquals(numLeaf1, numLeaf2);
		Variable result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
	
	@Test
	public void stringShouldEqualItself() throws WordsRuntimeException {
		AST stringLeaf1 = new LNodeString("test");
		
		INode testNode = new INodeEquals(stringLeaf1, stringLeaf1);
		Variable result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}

	@Test
	public void stringShouldNotEqualAnother() throws WordsRuntimeException {
		AST stringLeaf1 = new LNodeString("this");
		AST stringLeaf2 = new LNodeString("that");
		
		INode testNode = new INodeEquals(stringLeaf1, stringLeaf2);
		Variable result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
	
	@Test
	public void numberShouldEqualEquivalentString() throws WordsRuntimeException {
		AST numLeaf = new LNodeNum(1.4);
		AST stringLeaf = new LNodeString("1.4");
		
		INode testNode = new INodeEquals(numLeaf, stringLeaf);
		Variable result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test
	public void numberShouldNotEqualNonequivalentString() throws WordsRuntimeException {
		AST numLeaf = new LNodeNum(1.4);
		AST stringLeaf = new LNodeString("4.7");
		
		INode testNode = new INodeEquals(numLeaf, stringLeaf);
		Variable result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
	
	@Test
	public void objectShouldEqualItself() throws WordsRuntimeException {
		createObjectFred.eval(environment);		
		INode testNode = new INodeEquals(new INodeReferenceList(fredStringLeaf), new INodeReferenceList(fredStringLeaf));
		Variable result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertTrue("Result is true", result.booleanValue);
	}
	
	@Test
	public void objectShouldNotEqualAnotherObject() throws WordsRuntimeException {
		createObjectFred.eval(environment);
		createObjectGeorge.eval(environment);
		INode testNode = new INodeEquals(new INodeReferenceList(fredStringLeaf), new INodeReferenceList(georgeStringLeaf));
		Variable result = testNode.eval(environment);
		assertEquals("Creates a boolean", result.type, Variable.Type.BOOLEAN);
		assertFalse("Result is false", result.booleanValue);
	}
}
