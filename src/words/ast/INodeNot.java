package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeNot extends INode {
	public INodeNot(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable predicate = children.get(0).eval(environment);
		
		assert predicate.type == Variable.Type.BOOLEAN : "Predicate has type " + predicate.type.toString();
		
		return new Variable(!predicate.booleanValue);
	}
}