package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.ast.*;
import words.exceptions.*;

public class TestINodeNegate extends TestINode {
	@Test
	public void numberNegateCorrectly() throws WordsRuntimeException {
		AST numLeaf1 = new LNodeNum(3);
		
		INode testNode = new INodeNegate(numLeaf1);
		ASTValue result = testNode.eval(environment);
		assertTrue("Returns NUM value", result.type == ASTValue.Type.NUM);
		assertEquals("Correct Negation", result.numValue, -3.0, 0.0001);
	}
	
	@Test (expected = InvalidTypeException.class)
	public void onlyOperatesOnNumbers() throws WordsRuntimeException {
		INode testNode = new INodeNegate(stringLeaf);
		ASTValue result = testNode.eval(environment);
	}
}