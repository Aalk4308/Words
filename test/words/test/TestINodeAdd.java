package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.environment.Position;
import words.exceptions.*;

public class TestINodeAdd extends TestINode {
	@Test
	public void twoNumbersAddCorrectly() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(4);
		AST numLeaf2 = new LNodeNum(3);
		
		INode testNode = new INodeAdd(numLeaf1, numLeaf2);
		Variable result = testNode.eval(environment);
		assertTrue("Returns NUM value", result.type == Variable.Type.NUM);
		assertEquals("Correct Addition", result.numValue, 7.0, 0.0001);
	}
	
	@Test
	public void numbersInStringFormAreTreatedAsNumbers() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(6);
		AST stringLeaf1 = new LNodeString("7");
		
		INode testNode = new INodeAdd(numLeaf1, stringLeaf1);
		Variable result = testNode.eval(environment);
		assertTrue("Returns NUM value", result.type == Variable.Type.NUM);
		assertEquals("Correct Addition", result.numValue, 13.0, 0.0001);
	}

	@Test
	public void stringConcatenation() throws WordsRuntimeException {
		AST stringLeaf1 = new LNodeString("a");
		AST stringLeaf2 = new LNodeString("b");
		
		INode testNode = new INodeAdd(stringLeaf1, stringLeaf2);
		Variable result = testNode.eval(environment);
		assertTrue("Returns STRING value", result.type == Variable.Type.STRING);
		assertEquals("Correct String Concatenation", result.stringValue, "ab");
	}
	
	@Test
	public void stringConcatenationwithObject() throws WordsRuntimeException {
		AST stringLeaf1 = new LNodeString("a");
		environment.createObject("Fred", "thing", new Position(0,0));
		LNodeReference fredLNodeRef = new LNodeReference("Fred's");
		INodeReferenceList fredRefList = new INodeReferenceList(fredLNodeRef);
		
		INode testNode = new INodeAdd(stringLeaf1, fredRefList);
		Variable result = testNode.eval(environment);
		assertTrue("Returns STRING value", result.type == Variable.Type.STRING);
		assertEquals("Correct String Concatenation", result.stringValue, "aFred");
	}
	
	@Test
	public void numberToStringCoercionIdiom() throws WordsRuntimeException {
		AST numLeaf = new LNodeNum(5);
		AST stringLeaf = new LNodeString("");
		
		INode testNode = new INodeAdd(numLeaf, stringLeaf);
		Variable result = testNode.eval(environment);
		assertTrue("Returns STRING value", result.type == Variable.Type.STRING);
		assertEquals("Correct string value", result.stringValue, "5.000000");
	}
	
	@Test
	public void stringToNumberCoercionIdiom() throws WordsRuntimeException {
		AST stringLeaf = new LNodeString("5");
		AST numLeaf = new LNodeNum(0);
		
		INode testNode = new INodeAdd(stringLeaf, numLeaf);
		Variable result = testNode.eval(environment);
		assertTrue("Returns NUM value", result.type == Variable.Type.NUM);
		assertEquals("Correct number value", result.numValue, 5.0, 0.0001);
	}
}
