package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeQueueAssign extends INode {
	public INodeQueueAssign(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable referenceObject = children.get(0).eval(environment);
		// Right now, we don't support queued assignments on local variables
		if (referenceObject.type != Variable.Type.OBJ) {
			throw new InvalidTypeException("Object", referenceObject.type.toString());
		}
		AST propertyList = children.get(1);
		Variable doNow = children.get(2) != null ? children.get(2).eval(environment) : null;
		
		PropertyAssignAction action = new PropertyAssignAction(environment.getCurrentScope(), propertyList);
		WordsObject object = referenceObject.objValue;
		
		if (doNow == null) {
			object.enqueueAction(action);
		} else {
			object.enqueueActionAtFront(action);
		}
		
		return null;
	}
}