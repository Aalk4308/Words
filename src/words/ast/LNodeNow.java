package words.ast;

import words.ast.ASTValue.ValueType;
import words.environment.WordsEnvironment;

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
	public ASTValue eval(WordsEnvironment environment) {
		return new ASTValue(ASTValue.ValueType.NOW);
	}
}