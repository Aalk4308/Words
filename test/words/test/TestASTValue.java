package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.ast.ASTValue;

public class TestASTValue {
	
	@Test
	public void coerceStringToNum() {
		ASTValue coercableString = new ASTValue("5.1");
		ASTValue nonCoercableString = new ASTValue("ham");
		coercableString.tryCoerceTo(ASTValue.ValueType.NUM);
		nonCoercableString.tryCoerceTo(ASTValue.ValueType.NUM);
		assertEquals("Coerced a string into a num successfully", coercableString.type, ASTValue.ValueType.NUM);
		assertEquals("Did not coerce a non-coercable string into a num", nonCoercableString.type, ASTValue.ValueType.STRING);
		assertEquals("Received the right num of string to num conversion", coercableString.numValue, 5.1, .0001);
	}
	
	@Test
	public void coerceNumToString() {
		ASTValue num = new ASTValue(5);
		num.tryCoerceTo(ASTValue.ValueType.STRING);
		assertEquals("Successfully coerced a number to a string.", num.stringValue, "5.000000");
	}
}
