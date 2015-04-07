package words.ast;

import words.environment.WordsEnvironment;

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
	public ASTValue eval(WordsEnvironment environment) {
		return new ASTValue(this.identifier);
	}
}