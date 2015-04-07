package words.ast;

import words.ast.ASTValue.ValueType;
import words.environment.WordsEnvironment;
import words.exceptions.WordsRuntimeException;

public class INodeNot extends INode {
	public INodeNot(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException {
		ASTValue predicate = children.get(0).eval(environment);
		
		assert predicate.type == ASTValue.ValueType.BOOLEAN : "Predicate has type " + predicate.type.toString();
		
		return new ASTValue(!predicate.booleanValue);
	}
}