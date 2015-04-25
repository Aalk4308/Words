package words.ast;

import words.environment.*;
import words.exceptions.*;

public class INodePosition extends INode {
	public INodePosition(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(Environment environment) throws WordsRuntimeException {
		ASTValue row = children.get(0).eval(environment).tryCoerceTo(ASTValue.Type.NUM);
		ASTValue col = children.get(1).eval(environment).tryCoerceTo(ASTValue.Type.NUM);
		
		if (row.type != ASTValue.Type.NUM) {
			throw new InvalidTypeException(ASTValue.Type.NUM.toString(), row.type.toString());
		}
		
		if (col.type != ASTValue.Type.NUM) {
			throw new InvalidTypeException(ASTValue.Type.NUM.toString(), col.type.toString());
		}
		
		return new ASTValue(new Position(col.numValue, row.numValue));
	}
}