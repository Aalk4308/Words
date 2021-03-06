package words.ast;

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
	public ASTValue eval(Environment environment) throws WordsRuntimeException {
		return null;
	}
	
	@Override
	public ASTValue eval(Environment environment, Object inherited) throws WordsRuntimeException {
		ASTValue propertyName = id.eval(environment);
		assert literal != null : "Null property assignment on class creation not allowed by grammar.";
		
		ASTValue propertyValue = literal.eval(environment);
		assert propertyValue.type == ASTValue.Type.STRING || propertyValue.type == ASTValue.Type.NUM:
			"Literal wasn't string or number";
		
		WordsClass wordsClass = (WordsClass) inherited;
		wordsClass.setProperty(propertyName.stringValue, propertyValue.toWordsProperty());
		
		return null;
	}
	
	
}



	
