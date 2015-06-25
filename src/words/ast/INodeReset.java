package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeReset extends INode {
	public INodeReset(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		environment.resetEnvironment();
		
		return null;
	}
}