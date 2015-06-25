package words.test;

import static org.junit.Assert.*;
import words.Variable;
import words.ast.*;
import words.environment.*;
import words.exceptions.WordsRuntimeException;

import org.junit.Test;

public class TestINodeCreateLocalVariable extends TestINode {

	@Test
	public void canCreateLocalVariable() throws WordsRuntimeException {
		AST createNumberVariable = new INodeCreateLocalVariable(new LNodeString("var"), new LNodeNum(5));
		createNumberVariable.eval(environment);
		assertEquals(environment.getVariable("var").numValue, 5, .0001);
		assertEquals(environment.getVariable("var").type, Variable.Type.NUM);
	}
	
	@Test
	public void canOverwriteVariable() throws WordsRuntimeException {
		AST createNumberVariable = new INodeCreateLocalVariable(new LNodeString("var"), new LNodeNum(5));
		createNumberVariable.eval(environment);
		assertEquals(environment.getVariable("var").numValue, 5, .0001);
		assertEquals(environment.getVariable("var").type, Variable.Type.NUM);
		AST createStringVariable = new INodeCreateLocalVariable(new LNodeString("var"), new LNodeString("Hello"));
		createStringVariable.eval(environment);
		assertEquals(environment.getVariable("var").stringValue, "Hello");
		assertEquals(environment.getVariable("var").type, Variable.Type.STRING);
	}

}
