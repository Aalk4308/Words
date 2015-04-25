package words.ast;

import words.environment.*;
import words.exceptions.*;

public class INodeEquals extends INode {
	public INodeEquals(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(Environment environment) throws WordsRuntimeException {
		ASTValue lhs = children.get(0).eval(environment);
		ASTValue rhs = children.get(1).eval(environment);
		
		// The special type Nothing is equal only to Nothing
		if ((lhs.type == ASTValue.Type.NOTHING && rhs.type != ASTValue.Type.NOTHING) || 
			(lhs.type != ASTValue.Type.NOTHING && rhs.type == ASTValue.Type.NOTHING)) {
			return new ASTValue(false);
		}
		
		if (lhs.type == ASTValue.Type.NOTHING && rhs.type == ASTValue.Type.NOTHING) {
			return new ASTValue(true);
		}
		
		// Objects only equal another object and cannot be equal to non-objects
		if ((lhs.type == ASTValue.Type.OBJ && rhs.type != ASTValue.Type.OBJ) || 
			(lhs.type != ASTValue.Type.OBJ && rhs.type == ASTValue.Type.OBJ)) {
			return new ASTValue(false);
		}
		
		if (lhs.type == ASTValue.Type.OBJ && rhs.type == ASTValue.Type.OBJ) {
			return new ASTValue(lhs.objValue == rhs.objValue);
		}
		
		// Remaining types must be a number or string
		assert lhs.type == ASTValue.Type.NUM || lhs.type == ASTValue.Type.STRING : "Left side has type " + lhs.type.toString();
		assert rhs.type == ASTValue.Type.NUM || rhs.type == ASTValue.Type.STRING : "Right side has type " + rhs.type.toString();
		
		// Number/string type coercion
		if (lhs.type != rhs.type) {
			if (lhs.type == ASTValue.Type.STRING) {
				lhs = lhs.tryCoerceTo(ASTValue.Type.NUM);
				
				// If string to number coercion failed on lhs, do number to string coercion on rhs
				if (lhs.type == ASTValue.Type.STRING)
					rhs = rhs.tryCoerceTo(ASTValue.Type.STRING);
			} else {
				// lhs must be a number
				rhs = rhs.tryCoerceTo(ASTValue.Type.NUM);
				
				// If string to number coercion failed on rhs, do number to string coercion on lhs
				if (rhs.type == ASTValue.Type.STRING)
					lhs = lhs.tryCoerceTo(ASTValue.Type.STRING);
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