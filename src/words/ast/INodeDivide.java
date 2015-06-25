package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeDivide extends INode {
	public INodeDivide(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable lhs = children.get(0).eval(environment).tryCoerceTo(Variable.Type.NUM);
		Variable rhs = children.get(1).eval(environment).tryCoerceTo(Variable.Type.NUM);

		if ((lhs.type != Variable.Type.NUM) || (rhs.type != Variable.Type.NUM)) {
			throw new WordsArithmeticException(lhs.type.toString(), rhs.type.toString());
		}
		if (rhs.numValue == 0.0) {
			throw new DivideByZeroException();
		}

		return new Variable(lhs.numValue/rhs.numValue);
	}
}