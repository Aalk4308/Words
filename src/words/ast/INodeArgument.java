package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeArgument extends INode {
	public INodeArgument(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		assert false : "Requires an inherited object";
		return null;
	}
	
	@Override
	public Variable eval(Environment environment, Object inherited) throws WordsRuntimeException {
		Scope evaluatedArguments = (Scope) inherited;
		Variable argumentName = children.get(0).eval(environment);
		Variable argumentValue = children.get(1).eval(environment);
		evaluatedArguments.variables.put(argumentName.stringValue, argumentValue);
		
		return null;
	}
}