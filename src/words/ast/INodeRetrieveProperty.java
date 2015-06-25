package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeRetrieveProperty extends INode {
	public INodeRetrieveProperty(Object... children) {
		super(children);
	}

	@Override
	/**
	 * Returns the value of the second child (identifier).
	 * If the first child(reference list) evaluates to NOTHING, the identifier must be an object.
	 * Otherwise, the reference list evaluates to an object, and identifier refers to one of the object's properties.
	 * Returns NOTHING if the property isn't found.
	 * Throws a WordsReferenceException if the reference_list or identifier doesn't refer to an object when it's expected to.
	 */
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable refList = children.get(0).eval(environment);
		if (refList.type == Variable.Type.NOTHING) {
			Variable id = children.get(1).eval(environment);
			
			Variable property = environment.getVariable(id.stringValue);
			return property;

		}
		
		WordsObject obj = refList.objValue;
		assert obj != null : "Obj was null when it shouldn't have been.";

		Variable id = children.get(1).eval(environment);
		String propName = id.stringValue;
		
		Variable wordsProp = obj.getProperty(propName);
		
		return wordsProp;
	}

}