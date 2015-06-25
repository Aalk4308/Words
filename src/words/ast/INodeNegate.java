package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeNegate extends INode {
	public INodeNegate(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable value = children.get(0).eval(environment).tryCoerceTo(Variable.Type.NUM);

		if (value.type != Variable.Type.NUM) {
			throw new InvalidTypeException(Variable.Type.NUM.toString(), value.type.toString());
		}

		return new Variable(value.numValue * -1);
	}
}