package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeSubject extends INode {
	public INodeSubject(Object... children) {
		super(children);
	}

	/**
	 * Can return either a string, which is a class name
	 * or an object, which is an object. 
	 */
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable className = children.get(0) == null ? null : children.get(0).eval(environment);
		AST referenceObject = children.get(1);
		AST identifier = children.get(2);
		
		if (className != null) {
			return new Variable(className.stringValue);
		} else {
			Variable property = lookupProperty(environment, referenceObject, identifier);
			if (property.type != Variable.Type.OBJ) {
				throw new InvalidTypeException(Variable.Type.OBJ.toString(), property.type.toString());
			}
			WordsObject object = property.objValue;
			return new Variable(object);
		}
	}
}