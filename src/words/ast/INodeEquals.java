package words.ast;

import words.ast.ASTValue.ValueType;
import words.environment.WordsEnvironment;
import words.exceptions.WordsRuntimeException;

public class INodeEquals extends INode {
	public INodeEquals(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException {
		ASTValue lhs = children.get(0).eval(environment);
		ASTValue rhs = children.get(1).eval(environment);
		
		// The special type Nothing is equal only to Nothing
		if ((lhs.type == ASTValue.ValueType.NOTHING && rhs.type != ASTValue.ValueType.NOTHING) || 
			(lhs.type != ASTValue.ValueType.NOTHING && rhs.type == ASTValue.ValueType.NOTHING)) {
			return new ASTValue(false);
		}
		
		if (lhs.type == ASTValue.ValueType.NOTHING && rhs.type == ASTValue.ValueType.NOTHING) {
			return new ASTValue(true);
		}
		
		// Objects only equal another object and cannot be equal to non-objects
		if ((lhs.type == ASTValue.ValueType.OBJ && rhs.type != ASTValue.ValueType.OBJ) || 
			(lhs.type != ASTValue.ValueType.OBJ && rhs.type == ASTValue.ValueType.OBJ)) {
			return new ASTValue(false);
		}
		
		if ((lhs.type == ASTValue.ValueType.OBJ && rhs.type == ASTValue.ValueType.OBJ) &&
			(lhs.objValue == rhs.objValue)) {
			return new ASTValue(true);
		}
		
		// Remaining types must be a number or string
		assert lhs.type == ASTValue.ValueType.NUM || lhs.type == ASTValue.ValueType.STRING : "Left side has type " + lhs.type.toString();
		assert rhs.type == ASTValue.ValueType.NUM || rhs.type == ASTValue.ValueType.STRING : "Right side has type " + rhs.type.toString();
		
		// Number/string type coercion
		if (lhs.type != rhs.type) {
			if (lhs.type == ASTValue.ValueType.STRING) {
				lhs = lhs.tryCoerceTo(ASTValue.ValueType.NUM);
				
				// If string to number coercion failed on lhs, do number to string coercion on rhs
				if (lhs.type == ASTValue.ValueType.STRING)
					rhs = rhs.tryCoerceTo(ASTValue.ValueType.STRING);
			} else {
				// lhs must be a number
				rhs = rhs.tryCoerceTo(ASTValue.ValueType.NUM);
				
				// If string to number coercion failed on rhs, do number to string coercion on lhs
				if (rhs.type == ASTValue.ValueType.STRING)
					lhs = lhs.tryCoerceTo(ASTValue.ValueType.STRING);
			}
		}
		
		// lhs and rhs are now the same type
		assert lhs.type == rhs.type : "lhs has type " + lhs.type.toString() + " and rhs has type " + rhs.type.toString();
		switch (lhs.type) {
			case NUM:
				return new ASTValue(lhs.numValue == rhs.numValue);
			case STRING:
				return new ASTValue(lhs.stringValue.equals(rhs.stringValue));
			default:
				throw new AssertionError("Attempted to evaluate relational operator on ValueType " + lhs.type);			
		}
	}
}