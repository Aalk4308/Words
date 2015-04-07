package words.ast;

import words.environment.WordsEnvironment;

/**
 * A syntax tree leaf node for a number literal.
 */
public class LNodeNum extends LNode {
	public double num;
	
	public LNodeNum(double num) {
		super();
		this.num = num;
	}

	@Override
	protected String valueAsString() {
		return Double.toString(num);
	}
	
	@Override
	public ASTValue eval(WordsEnvironment environment) {
		return new ASTValue(this.num);
	}
}