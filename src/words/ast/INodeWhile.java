package words.ast;

import words.ast.ASTValue.ValueType;
import words.environment.WordsEnvironment;
import words.exceptions.WordsInvalidTypeException;
import words.exceptions.WordsRuntimeException;

public class INodeWhile extends INode {
	public INodeWhile(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException {
		AST conditional = children.get(0);
		AST statementList = children.get(1);
		ASTValue conditionalValue = conditional.eval(environment);
		
		assert conditionalValue.type == ASTValue.ValueType.BOOLEAN;

		while (conditionalValue.booleanValue == true) {
			environment.enterNewLocalScope();
			statementList.eval(environment);
			environment.exitLocalScope();
			conditionalValue = conditional.eval(environment);
		}
		
		return null;
	}
}