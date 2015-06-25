package words.ast;

import java.util.HashSet;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeTouchesPredicate extends INodeBasicActionPredicate {
	public INodeTouchesPredicate(Object... children) {
		super(children);
	}
	
	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		assert false : "Cannot eval INodeTouchesPredicate without inherited Statement List";
		return null;
	}

	@Override
	public Variable eval(Environment environment, Object inheritedStmts) throws WordsRuntimeException {
		Variable subject1 = children.get(0).eval(environment);
		Variable objectAlias1 = children.get(1).eval(environment);
		Variable subject2 = children.get(2).eval(environment);
		Variable objectAlias2 = children.get(3).eval(environment);
		
		INodeStatementList stmtList = (INodeStatementList) inheritedStmts;
		
		if (objectAlias1.type.equals(Variable.Type.STRING) 
				&& objectAlias2.type.equals(Variable.Type.STRING)
				&& objectAlias1.stringValue.equals(objectAlias2.stringValue)) {
			throw new AliasException();
		}
		
		HashSet<WordsObject> objectsToCheck1 = getObjectsToCheck(subject1, environment);
		HashSet<WordsObject> objectsToCheck2 = getObjectsToCheck(subject2, environment);
		Variable returnVal = new Variable(false);
		
		for (WordsObject object1: objectsToCheck1) {
			for (WordsObject object2: objectsToCheck2) {
				boolean objectsMoved = object1.movedInThisFrame() || object2.movedInThisFrame();
				if (object1 != object2 && objectsMoved && object1.getCurrentPosition().equals(object2.getCurrentPosition())) {
					returnVal.booleanValue = true;
					environment.pushNewScope();
					if (objectAlias1.type.equals(Variable.Type.STRING)) {
						environment.addToCurrentScope(objectAlias1.stringValue, new Variable(object1));
					}
					if (objectAlias2.type.equals(Variable.Type.STRING)) {
						environment.addToCurrentScope(objectAlias2.stringValue, new Variable(object2));
					}
					stmtList.eval(environment);
					environment.popScope();
				}
			}
		}
		return returnVal;
	}
}