package words.ast;

import words.ast.ASTValue.ValueType;
import words.exceptions.WordsOperatorTypeMismatchException;
import words.exceptions.WordsRuntimeException;

public abstract class INodeRelOp extends INode {
	public INodeRelOp(Object... children) {
		super(children);
	}

	/**
	 * Checks that the arguments to a relational operator <, <=, >, >= are appropriate and throws an appropriate
	 * exception if not.
	 */
	protected void checkRelOpArgTypes(ASTValue lhs, ASTValue rhs) throws WordsRuntimeException {
		if ((lhs.type != ASTValue.ValueType.NUM && lhs.type != ASTValue.ValueType.STRING) ||
			(lhs.type != rhs.type)) {
			throw new WordsOperatorTypeMismatchException(lhs.type.toString(), rhs.type.toString());
		}
	}
}