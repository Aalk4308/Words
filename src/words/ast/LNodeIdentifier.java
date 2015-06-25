package words.ast;

import words.Variable;
import words.environment.*;

/**
 * A syntax tree leaf node for an identifier.
 */
public class LNodeIdentifier extends LNode {
	public String identifier;
	
	public LNodeIdentifier(String identifier) {
		super();
		this.identifier = identifier;
	}
	
	@Override
	protected String valueAsString() {
		return identifier;		
	}
	
	@Override
	public Variable eval(Environment environment) {
		return new Variable(this.identifier);
	}
}