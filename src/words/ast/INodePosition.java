package words.ast;

import words.ast.ASTValue.ValueType;
import words.environment.WordsEnvironment;
import words.environment.WordsPosition;
import words.exceptions.WordsInvalidTypeException;
import words.exceptions.WordsRuntimeException;

public class INodePosition extends INode {
	public INodePosition(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException {
		ASTValue row = children.get(0).eval(environment).tryCoerceTo(ASTValue.ValueType.NUM);
		ASTValue col = children.get(1).eval(environment).tryCoerceTo(ASTValue.ValueType.NUM);
		
		if (row.type != ASTValue.ValueType.NUM) {
			throw new WordsInvalidTypeException(ASTValue.ValueType.NUM.toString(), row.type.toString());
		}
		
		if (col.type != ASTValue.ValueType.NUM) {
			throw new WordsInvalidTypeException(ASTValue.ValueType.NUM.toString(), col.type.toString());
		}
		
		return new ASTValue(new WordsPosition(row.numValue, col.numValue));
	}
}