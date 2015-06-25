package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeQueueAssignProperty extends INode {
	public INodeQueueAssignProperty(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		assert false : "Requires an inherited object";
		return null;
	}
	
	@Override
	public Variable eval(Environment environment, Object inherited) throws WordsRuntimeException {
		WordsObject object = (WordsObject) inherited;
		
		String propertyName = children.get(0).eval(environment).stringValue;
		Variable propertyASTValue = children.get(1).eval(environment);
		object.setProperty(propertyName, propertyASTValue);
		
		return null;
	}
}