package words.ast;

import words.environment.WordsEnvironment;
import words.exceptions.WordsRuntimeException;

public class INodeLess extends INodeRelOp {
	public INodeLess(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException {
		ASTValue lhs = children.get(0).eval(environment);
		ASTValue rhs = children.get(1).eval(environment);
		
		checkRelOpArgTypes(lhs, rhs);
		
		// lhs and rhs are now the same type
		switch (lhs.type) {
			case NUM:
				return new ASTValue(lhs.numValue < rhs.numValue);
			case STRING:
				return new ASTValue(lhs.stringValue.compareTo(rhs.stringValue) < 0);
			default:
				throw new AssertionError("Attempted to evaluate relational operator on ValueType " + lhs.type);			
		}
	}
}