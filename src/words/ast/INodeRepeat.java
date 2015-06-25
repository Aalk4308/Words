package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeRepeat extends INode {
	public INodeRepeat(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable times = children.get(0).eval(environment).tryCoerceTo(Variable.Type.NUM);
		AST statementList = children.get(1);
		
		if (times.type != Variable.Type.NUM) {
			throw new InvalidTypeException(Variable.Type.NUM.toString(), times.type.toString());
		}
		
		for (int i = 0; i < times.numValue; i++) {
			environment.pushNewScope();
			statementList.eval(environment);
			environment.popScope();
		}
		
		return null;
	}
}