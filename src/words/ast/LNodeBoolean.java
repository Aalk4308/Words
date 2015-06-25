package words.ast;

import words.Variable;
import words.environment.Environment;
import words.exceptions.WordsRuntimeException;

public class LNodeBoolean extends LNode {

	private boolean val;
	
	public LNodeBoolean(boolean b) {
		this.val = b;
	}
	
	@Override
	protected String valueAsString() {
		return Boolean.toString(val);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		return new Variable(val);
	}

}
