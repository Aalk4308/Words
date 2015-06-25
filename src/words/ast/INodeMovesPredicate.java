package words.ast;

import java.util.HashSet;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeMovesPredicate extends INodeBasicActionPredicate {
	public INodeMovesPredicate(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		assert false : "Cannot eval INodeMovesPredicate without inherited Statement List";
		return null;
	}

	@Override
	public Variable eval(Environment environment, Object inheritedStmts) throws WordsRuntimeException {
		Variable subject = children.get(0).eval(environment);
		Variable objectAlias = children.get(1).eval(environment);
		Variable moveDirection = children.get(2) == null ? null : children.get(2).eval(environment);
		
		INodeStatementList stmtList = (INodeStatementList) inheritedStmts;
		
		HashSet<WordsObject> objectsToCheck = getObjectsToCheck(subject, environment);
		Variable returnVal = new Variable(false);

		for (WordsObject object : objectsToCheck) {
			Action lastAction = object.getLastAction();
			if (lastAction instanceof MoveAction) {
				MoveAction lastMove = (MoveAction) lastAction;
				if (moveDirection == null || moveDirection.directionValue == Direction.ANYWHERE 
						|| moveDirection.directionValue.equals(lastMove.getDirection())) {
					returnVal.booleanValue = true;
					environment.pushNewScope();
					if (objectAlias.type.equals(Variable.Type.STRING)) {
						environment.addToCurrentScope(objectAlias.stringValue, new Variable(object));
					}
					stmtList.eval(environment);
					environment.popScope();
				}
			}
		}
		return returnVal;
	}
}