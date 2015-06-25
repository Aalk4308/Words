package words.ast;

import java.util.HashSet;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeWaitsPredicate extends INodeBasicActionPredicate {
	public INodeWaitsPredicate(Object... children) {
		super(children);
	}
	
	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		assert false : "Cannot eval INodeWaitsPredicate without inherited Statement List";
		return null;
	}

	@Override
	public Variable eval(Environment environment, Object inheritedStmts) throws WordsRuntimeException {
		Variable subject = children.get(0).eval(environment);
		Variable objectAlias = children.get(1).eval(environment);
		
		INodeStatementList stmtList = (INodeStatementList) inheritedStmts;
		
		HashSet<WordsObject> objectsToCheck = getObjectsToCheck(subject, environment);
		Variable returnVal = new Variable(false);

		for (WordsObject object : objectsToCheck) {
			Action lastAction = object.getLastAction();
			if (lastAction instanceof WaitAction) {
				returnVal.booleanValue = true;
				environment.pushNewScope();
				if (objectAlias.type.equals(Variable.Type.STRING)) {
					environment.addToCurrentScope(objectAlias.stringValue, new Variable(object));
				}
				stmtList.eval(environment);
				environment.popScope();
			}
		}
		return returnVal;
	}
}