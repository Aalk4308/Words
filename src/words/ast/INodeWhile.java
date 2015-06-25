package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeWhile extends INode {
	public INodeWhile(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable conditional = children.get(0).eval(environment);
		AST statementList = children.get(1);
		
		assert conditional.type == Variable.Type.BOOLEAN;

		while (conditional.booleanValue == true) {
			environment.pushNewScope();
			statementList.eval(environment);
			environment.popScope();
			conditional = children.get(0).eval(environment);
		}
		
		return null;
	}
}