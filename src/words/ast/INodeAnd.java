package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeAnd extends INode {
	public INodeAnd(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		// First evaluate just the left side to provide for short-circuit evaluation
		Variable lhs = children.get(0).eval(environment);
		assert lhs.type == Variable.Type.BOOLEAN : "Left side has type " + lhs.type.toString();
		
		// Short circuit
		if (lhs.booleanValue == false)
			return new Variable(false);
		
		// Now we can evaluate the right side
		Variable rhs = children.get(1).eval(environment);
		assert rhs.type == Variable.Type.BOOLEAN : "Right side has type " + rhs.type.toString();
		
		return new Variable(lhs.booleanValue && rhs.booleanValue);
	}	
}