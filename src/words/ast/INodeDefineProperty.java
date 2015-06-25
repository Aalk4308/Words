package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeDefineProperty extends INode {
	
	public LNodeIdentifier id;
	public LNode literal;
	
	public INodeDefineProperty(Object... children) {
		super(children);
		id = (LNodeIdentifier) this.children.get(0);
		literal = (LNode) this.children.get(1);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		return null;
	}
	
	@Override
	public Variable eval(Environment environment, Object inherited) throws WordsRuntimeException {
		Variable propertyName = id.eval(environment);
		assert literal != null : "Null property assignment on class creation not allowed by grammar.";
		
		Variable propertyValue = literal.eval(environment);
		assert propertyValue.type == Variable.Type.STRING || propertyValue.type == Variable.Type.NUM:
			"Literal wasn't string or number";
		
		WordsClass wordsClass = (WordsClass) inherited;
		wordsClass.setProperty(propertyName.stringValue, propertyValue);
		
		return null;
	}
	
	
}



	
