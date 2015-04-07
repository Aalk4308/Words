package words.ast;

import words.ast.ASTValue.ValueType;
import words.environment.WordsEnvironment;
import words.environment.WordsObject;
import words.environment.WordsProperty;
import words.environment.WordsSay;
import words.environment.WordsProperty.PropertyType;
import words.exceptions.WordsInvalidTypeException;
import words.exceptions.WordsRuntimeException;

public class INodeQueueSay extends INode {
	public INodeQueueSay(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException {
		ASTValue referenceObject = children.get(0).eval(environment);
		ASTValue identifier = children.get(1).eval(environment);
		ASTValue message = children.get(2).eval(environment).tryCoerceTo(ASTValue.ValueType.STRING);
		ASTValue doNow = children.get(3) != null ? children.get(3).eval(environment) : null;
		
		WordsObject object;
		if (referenceObject.type.equals(ASTValue.ValueType.OBJ)){
			WordsProperty property = referenceObject.objValue.getProperty(identifier.stringValue);
			if (property.type != WordsProperty.PropertyType.OBJECT) {
				throw new WordsInvalidTypeException(ASTValue.ValueType.OBJ.toString(), property.type.toString());
			}
			object = property.objProperty;
		} else {
			object = environment.getObject(identifier.stringValue);
		}
		
		if (message.type != ASTValue.ValueType.STRING) {
			throw new WordsInvalidTypeException(ASTValue.ValueType.STRING.toString(), message.type.toString());
		}
		WordsSay action = new WordsSay(message.stringValue);
		
		if (doNow == null) {
			object.enqueueAction(action);
		} else {
			object.enqueueActionAtFront(action);
		}
		
		return null;
	}
}