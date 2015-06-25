package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.exceptions.*;

public class TestINodeSubtract extends TestINode {
	@Test
	public void twoNumbersSubtractCorrectly() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(6);
		AST numLeaf2 = new LNodeNum(2);
		
		INode testNode = new INodeSubtract(numLeaf1, numLeaf2);
		Variable result = testNode.eval(environment);
		assertTrue("Returns NUM value", result.type == Variable.Type.NUM);
		assertEquals("Correct Subtraction", result.numValue, 4.0, 0.0001);
	}
	
	@Test (expected = WordsArithmeticException.class)
	public void onlyOperatesOnTwoNumbers() throws WordsRuntimeException {
		INode testNode = new INodeSubtract(numLeaf, stringLeaf);
		testNode.eval(environment);
	}
}