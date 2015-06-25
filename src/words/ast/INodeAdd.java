package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeAdd extends INode {
	public INodeAdd(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable lhs = children.get(0).eval(environment).tryCoerceTo(Variable.Type.NUM);
		Variable rhs = children.get(1).eval(environment).tryCoerceTo(Variable.Type.NUM);
		
		// check for objects
		if (lhs.type == Variable.Type.OBJ) {
			lhs = lhs.tryCoerceTo(Variable.Type.STRING);
		}
		if (rhs.type == Variable.Type.OBJ) {
			rhs = rhs.tryCoerceTo(Variable.Type.STRING);
		}
		
		if (((lhs.type == Variable.Type.STRING) && (rhs.type == Variable.Type.NUM)) || 
				((lhs.type == Variable.Type.NUM) && (rhs.type == Variable.Type.STRING)) ||
				((lhs.type == Variable.Type.STRING) && (rhs.type == Variable.Type.STRING))) {
			lhs.tryCoerceTo(Variable.Type.STRING);
			rhs.tryCoerceTo(Variable.Type.STRING);
			return new Variable(lhs.stringValue+rhs.stringValue);
		} else if ((lhs.type == Variable.Type.NUM) && (rhs.type == Variable.Type.NUM)) {
			return new Variable(lhs.numValue+rhs.numValue); 
		} else {
			throw new WordsArithmeticException(lhs.type.toString(), rhs.type.toString());
		}
	}
}