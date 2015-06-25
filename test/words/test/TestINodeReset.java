package words.test;

import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.environment.*;
import words.exceptions.*;


public class TestINodeReset extends TestINode {
	
	@Test
	public void testObjectDisappears() throws WordsRuntimeException {
		environment.createObject("Alex", "thing", new Position(0, 0));
		
		INodeReset testNode = new INodeReset();
		testNode.eval(environment);
		
		Variable property = environment.getVariable("Alex");
		assertEquals("Object has disappeared", property.type, Variable.Type.NOTHING);
	}
}
