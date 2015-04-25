package words.ast;

import words.environment.*;
import words.exceptions.*;

public class INodeArgument extends INode {
	public INodeArgument(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(Environment environment) throws WordsRuntimeException {
		assert false : "Requires an inherited object";
		return null;
	}
	
	@Override
	public ASTValue eval(Environment environment, Object inherited) throws WordsRuntimeException {
		Scope evaluatedArguments = (Scope) inherited;
		ASTValue argumentName = children.get(0).eval(environment);
		ASTValue argumentValue = children.get(1).eval(environment);
		evaluatedArguments.variables.put(argumentName.stringValue, argumentValue.toWordsProperty());
		
		return null;
	}
}