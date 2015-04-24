package words.ast;

import words.environment.*;
import words.exceptions.*;

public class INodeQueueAssignPropertyList extends INode {
	public INodeQueueAssignPropertyList(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(Environment environment) throws WordsRuntimeException {
		assert false : "Requires an inherited object";
		return null;
	}
	
	@Override
	public ASTValue eval(Environment environment, Object inherited) throws WordsRuntimeException {
		for (AST ast : children) {
			ast.eval(environment, inherited);
		}
		
		return null;
	}
}