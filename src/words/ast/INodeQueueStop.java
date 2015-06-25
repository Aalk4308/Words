package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeQueueStop extends INode {
	public INodeQueueStop(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		AST referenceObject = children.get(0);
		AST identifier = children.get(1);
		
		Variable property = lookupProperty(environment, referenceObject, identifier);
		if (property.type != Variable.Type.OBJ) {
			throw new InvalidTypeException(Variable.Type.OBJ.toString(), property.type.toString());
		}
		WordsObject object = property.objValue;
		
		object.clearActionQueue();
		
		return null;
	}
}