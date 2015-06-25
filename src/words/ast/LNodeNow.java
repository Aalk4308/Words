package words.ast;

import words.Variable;
import words.environment.*;

/**
 * A syntax tree leaf node for the keyword 'now'.
 */
public class LNodeNow extends LNode {	
	public LNodeNow() {
		super();
	}
	
	@Override
	protected String valueAsString() {
		return "now";
	}
	
	@Override
	public Variable eval(Environment environment) {
		return new Variable(Variable.Type.NOW);
	}
}