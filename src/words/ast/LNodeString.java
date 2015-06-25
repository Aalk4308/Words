package words.ast;

import words.Variable;
import words.environment.*;

/**
 * A syntax tree leaf node for a string literal.
 */
public class LNodeString extends LNode {
	public String string;
	
	public LNodeString(String string) {
		super();
		
		// Remove leading and trailing " characters
		// Do not do a full string replace, since some " characters may be escaped
		if (string.startsWith("\""))
			string = string.substring(1, string.length());
		
		if (string.endsWith("\""))
			string = string.substring(0, string.length() - 1);
		
		// Replace supported escape sequences
		string = string.replace("\\n", "\n");
		string = string.replace("\\\\", "\\");
		string = string.replace("\\\"", "\"");
		
		this.string = string;
	}
	
	@Override
	protected String valueAsString() {
		return string;
	}
	
	@Override
	public Variable eval(Environment environment) {
		return new Variable(this.string);
	}
}