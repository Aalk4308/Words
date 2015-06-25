package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeQueueMove extends INode {
	public INodeQueueMove(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		AST referenceObject = children.get(0);
		AST identifier = children.get(1);
		Variable direction = children.get(2).eval(environment);
		AST distance = children.get(3);
		Variable doNow = children.get(4) != null ? children.get(4).eval(environment) : null;
		
		Variable property = lookupProperty(environment, referenceObject, identifier);
		if (property.type != Variable.Type.OBJ) {
			throw new InvalidTypeException(Variable.Type.OBJ.toString(), property.type.toString());
		}
		WordsObject object = property.objValue;
		
		assert(direction.type == Variable.Type.DIRECTION) : "Expected direction";
		
		MoveAction action = new MoveAction(environment.getCurrentScope(), direction.directionValue, distance);
		
		if (doNow == null) {
			object.enqueueAction(action);
		} else {
			object.enqueueActionAtFront(action);
		}
		
		return null;
	}
}