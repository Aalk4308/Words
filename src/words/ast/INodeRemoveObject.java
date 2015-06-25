package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeRemoveObject extends INode {
	public INodeRemoveObject(Object... children) {
		super(children);
	}	

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		WordsObject obj = null;
		Variable refList = children.get(0).eval(environment);
		Variable id = children.get(1).eval(environment);
		
		Variable property;
		if (refList.type == Variable.Type.NOTHING) {
			property = environment.getVariable(id.stringValue);
			if (property.type != Variable.Type.OBJ) {
				throw new ObjectNotFoundException(id.stringValue);
			}
		} else {
			property = refList.objValue.getProperty(id.stringValue);
			if (property.type != Variable.Type.OBJ) {
				throw new ReferenceException(id.stringValue, property.type);
			}
		}
		
		obj = property.objValue;
		obj.flagForRemoval();
		
		return null;
	}
}