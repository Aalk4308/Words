package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeGreater extends INodeRelOp {
	public INodeGreater(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable lhs = children.get(0).eval(environment);
		Variable rhs = children.get(1).eval(environment);
		
		checkRelOpArgTypes(lhs, rhs);
		
		// lhs and rhs are now the same type
		switch (lhs.type) {
			case NUM:
				return new Variable(lhs.numValue > rhs.numValue);
			case STRING:
				return new Variable(lhs.stringValue.compareTo(rhs.stringValue) > 0);
			default:
				throw new AssertionError("Attempted to evaluate relational operator on ValueType " + lhs.type);			
		}
	}	
}