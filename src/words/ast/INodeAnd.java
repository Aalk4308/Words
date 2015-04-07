package words.ast;

import words.ast.ASTValue.ValueType;
import words.environment.WordsEnvironment;
import words.exceptions.WordsRuntimeException;

public class INodeAnd extends INode {
	public INodeAnd(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException {
		// First evaluate just the left side to provide for short-circuit evaluation
		ASTValue lhs = children.get(0).eval(environment);
		assert lhs.type == ASTValue.ValueType.BOOLEAN : "Left side has type " + lhs.type.toString();
		
		// Short circuit
		if (lhs.booleanValue == false)
			return new ASTValue(false);
		
		// Now we can evaluate the right side
		ASTValue rhs = children.get(1).eval(environment);
		assert rhs.type == ASTValue.ValueType.BOOLEAN : "Right side has type " + rhs.type.toString();
		
		return new ASTValue(lhs.booleanValue && rhs.booleanValue);
	}	
}