package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeClassStatementList extends INode {
	public INodeClassStatementList(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		assert false : "Requires an inherited object";
		return null;
	}
	
	@Override
	public Variable eval(Environment environment, Object inherited) throws WordsRuntimeException {
		for (AST child : children) {
			child.eval(environment, inherited);
		}
		
		return null;
	}
}