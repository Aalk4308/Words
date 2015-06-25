package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeListener extends INode {
	public INodeListener(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		AST predicate = children.get(0);
		AST statementList = children.get(1);
		Variable makeTemporary = children.get(2).eval(environment);
		
		environment.createListener(predicate, statementList, makeTemporary.booleanValue);
		
		return null;
	}
}