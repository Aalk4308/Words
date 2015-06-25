package words.ast;

import words.Variable;
import words.environment.*;

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
	public Variable eval(Environment environment) {
		return new Variable(this.num);
	}
}