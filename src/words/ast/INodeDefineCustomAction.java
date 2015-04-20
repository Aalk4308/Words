package words.ast;

import words.environment.*;
import words.exceptions.*;

public class INodeDefineCustomAction extends INode {
	public INodeDefineCustomAction(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(Environment environment, Object wordsClassObj) throws WordsRuntimeException {
		WordsClass wordsClass = (WordsClass) wordsClassObj;
		ASTValue actionName = children.get(0).eval(environment);
		AST parameterList = children.get(1);
		AST statementList = children.get(2);
		
		assert actionName.type == ASTValue.Type.STRING : "Custom Action name must be a string";
		
		CustomActionDefinition newAction = new CustomActionDefinition(statementList);
		if (parameterList != null) {
			parameterList.eval(environment, newAction);
		}
		wordsClass.defineCustomAction(actionName.stringValue, newAction);
		
		return null;
	}

	@Override
	public ASTValue eval(Environment environment) throws WordsRuntimeException {
		assert false : "Custom Action definitions must inherit a Words Class";
		return null;
	}
}