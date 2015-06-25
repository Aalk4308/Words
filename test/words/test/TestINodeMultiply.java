package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.exceptions.*;

public class TestINodeMultiply extends TestINode {
	@Test
	public void twoNumbersMultiplyCorrectly() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(3);
		AST numLeaf2 = new LNodeNum(4);
		
		INode testNode = new INodeMultiply(numLeaf1, numLeaf2);
		Variable result = testNode.eval(environment);
		assertTrue("Returns NUM value", result.type == Variable.Type.NUM);
		assertEquals("Correct Multiplication", result.numValue, 12.0, 0.0001);
	}
	
	@Test (expected = WordsArithmeticException.class)
	public void onlyOperatesOnTwoNumbers() throws WordsRuntimeException {
		INode testNode = new INodeMultiply(numLeaf, stringLeaf);
		testNode.eval(environment);
	}
}