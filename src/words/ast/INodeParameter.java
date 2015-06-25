package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeParameter extends INode {
	public INodeParameter(Object... children) {
		super(children);
	}
	
	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		assert false: "Requires an inherited object";
		return null;
	}

	@Override
	public Variable eval(Environment environment, Object inherited) throws WordsRuntimeException {
		CustomActionDefinition customAction = (CustomActionDefinition) inherited;
		Variable param = children.get(0).eval(environment);
		assert param.type == Variable.Type.STRING : "Parameter names should always be strings";
		customAction.addParameter(param.stringValue);
		return null;
	}
}