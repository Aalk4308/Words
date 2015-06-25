package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeExponentiate extends INode {
	public INodeExponentiate(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable lhs = children.get(0).eval(environment).tryCoerceTo(Variable.Type.NUM);
		Variable rhs = children.get(1).eval(environment).tryCoerceTo(Variable.Type.NUM);

		if ((lhs.type != Variable.Type.NUM) || (rhs.type != Variable.Type.NUM)) {
			throw new WordsArithmeticException(lhs.type.toString(), rhs.type.toString());
		}

		return new Variable(Math.pow(lhs.numValue, rhs.numValue));	
	}
}