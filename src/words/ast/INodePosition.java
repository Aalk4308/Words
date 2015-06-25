package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodePosition extends INode {
	public INodePosition(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		Variable row = children.get(0).eval(environment).tryCoerceTo(Variable.Type.NUM);
		Variable col = children.get(1).eval(environment).tryCoerceTo(Variable.Type.NUM);
		
		if (row.type != Variable.Type.NUM) {
			throw new InvalidTypeException(Variable.Type.NUM.toString(), row.type.toString());
		}
		
		if (col.type != Variable.Type.NUM) {
			throw new InvalidTypeException(Variable.Type.NUM.toString(), col.type.toString());
		}
		
		return new Variable(new Position(row.numValue, col.numValue));
	}
}