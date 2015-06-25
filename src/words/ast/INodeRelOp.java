package words.ast;

import words.Variable;
import words.exceptions.*;

public abstract class INodeRelOp extends INode {
	public INodeRelOp(Object... children) {
		super(children);
	}

	/**
	 * Checks that the arguments to a relational operator <, <=, >, >= are appropriate and throws an appropriate
	 * exception if not.
	 */
	protected void checkRelOpArgTypes(Variable lhs, Variable rhs) throws WordsRuntimeException {
		if ((lhs.type != Variable.Type.NUM && lhs.type != Variable.Type.STRING) ||
			(lhs.type != rhs.type)) {
			throw new OperatorTypeMismatchException(lhs.type.toString(), rhs.type.toString());
		}
	}
}