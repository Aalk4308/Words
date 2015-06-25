package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.environment.*;
import words.exceptions.*;


public class TestINodePosition extends TestINode {
	@Test
	public void positionWithNothingWrong() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(0.0);
		AST numLeaf2 = new LNodeNum(2.1);
		
		INode testNode = new INodePosition(numLeaf1, numLeaf2);
		Variable result = testNode.eval(environment);
		assertEquals("Creates a position", result.type, Variable.Type.POSITION);
		assertEquals("The position is the right position", result.positionValue, new Position(0,2));
	}
	
	@Test
	public void positionWithCoercableString() throws WordsRuntimeException {
		AST numLeaf = new LNodeNum(0.0);
		AST stringLeaf = new LNodeString("2.1");
		
		INode testNode = new INodePosition(numLeaf, stringLeaf);
		Variable result = testNode.eval(environment);
		assertEquals("Creates a position", result.type, Variable.Type.POSITION);
		assertEquals("The position is the right position", result.positionValue, new Position(0,2));
	}
	
	@Test (expected = InvalidTypeException.class)
	public void positionWithNonCoercableString() throws WordsRuntimeException {
		AST numLeaf = new LNodeNum(0.0);
		AST stringLeaf = new LNodeString("ham");
		
		INode testNode = new INodePosition(numLeaf, stringLeaf);
		testNode.eval(environment);
	}
}
