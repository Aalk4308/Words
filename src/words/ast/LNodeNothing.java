package words.ast;

import words.Variable;
import words.environment.*;

/**
 * A syntax tree leaf node for the keyword 'nothing'.
 */
public class LNodeNothing extends LNode {	
	public LNodeNothing() {
		super();
	}
	
	@Override
	protected String valueAsString() {
		return "nothing";
	}
	
	@Override
	public Variable eval(Environment environment) {
		return new Variable(Variable.Type.NOTHING);
	}
}