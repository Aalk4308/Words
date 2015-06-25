package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeAlias extends INode {
	public INodeAlias(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable identifier = children.size() == 0 ? null : children.get(0).eval(environment);
		if (identifier != null) {
			return identifier;
		} else {
			return new Variable(Variable.Type.NOTHING);
		}
	}
}