package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeCreateObject extends INode {
	public INodeCreateObject(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable objName = children.get(0).eval(environment);
		Variable className = children.get(1).eval(environment);
		Variable position = children.get(2).eval(environment);

		environment.createObject(objName.stringValue, className.stringValue, position.positionValue);
		
		return null;
	}
}