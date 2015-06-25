package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeIf extends INode {
	public INodeIf(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable predicate = children.get(0).eval(environment);
		AST statementList = children.get(1);
		
		assert predicate.type == Variable.Type.BOOLEAN : "Predicate has type " + predicate.type.toString();
		
		if (predicate.booleanValue == true) {
			environment.pushNewScope();
			statementList.eval(environment);
			environment.popScope();
		}
		
		return null;
	}
}