package words.ast;

import words.ast.ASTValue.ValueType;
import words.environment.WordsEnvironment;

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
	public ASTValue eval(WordsEnvironment environment) {
		return new ASTValue(ASTValue.ValueType.NOTHING);
	}
}