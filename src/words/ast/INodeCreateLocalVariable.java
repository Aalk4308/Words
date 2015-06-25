package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeCreateLocalVariable extends INode {
	public INodeCreateLocalVariable(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable varName = children.get(0).eval(environment);
		Variable rawValue = children.get(1).eval(environment);
		Variable value = null;
		
		if (rawValue.type == Variable.Type.NUM) {
			value = new Variable(rawValue.numValue);
		} else if (rawValue.type == Variable.Type.STRING) {
			value = new Variable(rawValue.stringValue);
		}
		
		environment.createLocalVariable(varName.stringValue, value);
		
		return null;
	}
}
