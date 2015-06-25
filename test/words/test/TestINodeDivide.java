package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.exceptions.*;

public class TestINodeDivide extends TestINode {
	@Test
	public void twoNumbersDivideCorrectly() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(1);
		AST numLeaf2 = new LNodeNum(2);
		
		INode testNode = new INodeDivide(numLeaf1, numLeaf2);
		Variable result = testNode.eval(environment);
		assertTrue("Returns NUM value", result.type == Variable.Type.NUM);
		assertEquals("Correct Division", result.numValue, 0.5, 0.0001);
	}
	
	@Test (expected = WordsArithmeticException.class)
	public void onlyOperatesOnTwoNumbers() throws WordsRuntimeException {
		INode testNode = new INodeDivide(numLeaf, stringLeaf);
		testNode.eval(environment);
	}

	@Test (expected = DivideByZeroException.class)
	public void cannotDivideByZero() throws WordsRuntimeException {
		INode testNode = new INodeDivide(twoLeaf, numLeaf);
		testNode.eval(environment);
	}
}