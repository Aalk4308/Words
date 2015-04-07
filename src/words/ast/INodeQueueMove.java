package words.ast;

import words.ast.ASTValue.ValueType;
import words.environment.WordsEnvironment;
import words.environment.WordsMove;
import words.environment.WordsObject;
import words.environment.WordsProperty;
import words.environment.WordsProperty.PropertyType;
import words.exceptions.WordsInvalidTypeException;
import words.exceptions.WordsObjectNotFoundException;
import words.exceptions.WordsRuntimeException;

public class INodeQueueMove extends INode {
	public INodeQueueMove(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException {
		ASTValue referenceObject = children.get(0).eval(environment);
		ASTValue identifier = children.get(1).eval(environment);
		ASTValue direction = children.get(2).eval(environment);
		AST distance = children.get(3);
		ASTValue doNow = children.get(4) != null ? children.get(4).eval(environment) : null;
		
		WordsObject object;
		if (referenceObject.type.equals(ASTValue.ValueType.OBJ)){
			WordsProperty property = referenceObject.objValue.getProperty(identifier.stringValue);
			if (property.type != WordsProperty.PropertyType.OBJECT) {
				throw new WordsInvalidTypeException(ASTValue.ValueType.OBJ.toString(), property.type.toString());
			}
			object = property.objProperty;
		} else {
			object = environment.getObject(identifier.stringValue);
			if (object == null) {
				throw new WordsObjectNotFoundException(identifier.stringValue);
			}
		}
		
		assert(direction.type == ASTValue.ValueType.DIRECTION) : "Expected direction";
		
		//TODO: Distance = 0 should create a wait method
		WordsMove action = new WordsMove(direction.directionValue, distance);
		
		if (doNow == null) {
			object.enqueueAction(action);
		} else {
			object.enqueueActionAtFront(action);
		}
		
		return null;
	}
}