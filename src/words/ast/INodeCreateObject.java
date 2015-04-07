package words.ast;

import words.environment.WordsEnvironment;
import words.environment.WordsObject;
import words.exceptions.WordsRuntimeException;

public class INodeCreateObject extends INode {
	public INodeCreateObject(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException {
		ASTValue objName = children.get(0).eval(environment);
		ASTValue className = children.get(1).eval(environment);
		ASTValue properties = children.get(2) != null ? children.get(2).eval(environment) : null;
		ASTValue position = children.get(3).eval(environment);

		WordsObject newObject = environment.createObject(objName.stringValue, className.stringValue, position.positionValue);
		// TODO
		if (properties != null) {
			// For each element of properties, add property to newObject
		}
		
		return null;
	}
}