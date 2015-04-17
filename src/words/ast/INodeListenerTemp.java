package words.ast;

import words.environment.WordsEnvironment;
import words.exceptions.WordsRuntimeException;

public class INodeListenerTemp extends INode {
	public INodeListenerTemp(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException {
		AST predicate = children.get(0);
		AST statementList = children.get(1);
		
		ASTValue predicateValue = predicate.eval(environment);

		// currently only restricted to boolean predicate
		assert predicateValue.type == ASTValue.ValueType.BOOLEAN : "Predicate has type " + predicateValue.type.toString();

		// temporary listener is created only if the predicate is true
		if (predicateValue.booleanValue == true) {
			environment.createListener(predicate, statementList, true);
		}
		
		return null;
	}
}