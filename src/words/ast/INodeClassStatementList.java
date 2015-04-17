package words.ast;

import words.environment.WordsClass;
import words.environment.WordsEnvironment;
import words.exceptions.WordsRuntimeException;

public class INodeClassStatementList extends INode {
	public INodeClassStatementList(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException {
		// TODO
		throw new AssertionError("Not yet implemented");
	}
	
	@Override
	public ASTValue eval(WordsEnvironment environment, Object inherited) throws WordsRuntimeException {
		for (AST child : this.children) {
			child.eval(environment, inherited);
		}
		
		return null;
	}
}