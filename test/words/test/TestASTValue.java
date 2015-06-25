package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;

public class TestASTValue {
	
	@Test
	public void coerceStringToNum() {
		Variable coercableString = new Variable("5.1");
		Variable nonCoercableString = new Variable("ham");
		coercableString.tryCoerceTo(Variable.Type.NUM);
		nonCoercableString.tryCoerceTo(Variable.Type.NUM);
		assertEquals("Coerced a string into a num successfully", coercableString.type, Variable.Type.NUM);
		assertEquals("Did not coerce a non-coercable string into a num", nonCoercableString.type, Variable.Type.STRING);
		assertEquals("Received the right num of string to num conversion", coercableString.numValue, 5.1, .0001);
	}
	
	@Test
	public void coerceNumToString() {
		Variable num = new Variable(5);
		num.tryCoerceTo(Variable.Type.STRING);
		assertEquals("Successfully coerced a number to a string.", num.stringValue, "5.000000");
	}
}
