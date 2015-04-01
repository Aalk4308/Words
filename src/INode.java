import java.util.ArrayList;
import java.lang.StringBuilder;


/**
 * An abstract syntax tree internal node.
 */
public class INode extends AST {
	public ArrayList<AST> children;
	
	public INode(ASTType type, int lineNo, Object... children) {
		super(type, lineNo);
		
		this.children = new ArrayList<AST>(children.length);
		
		for (int i = 0; i < children.length; i++) {
			if (children[i] == null)
				this.children.add(null);
			else
				this.children.add((AST) children[i]);
		}
	}
	
	/**
	 * Appends a given list of nodes to this node's list of children. 
	 * 
	 * @param nodes the list of nodes to be added
	 * @return this node
	 */
	public INode add(ArrayList<AST> nodes) {
		for (AST node : nodes)
			this.children.add(node);
		
		return this;
	}
	
	private void indent(int level) {
		for (int i = 0; i < level; i++)
			System.err.printf("  ");		
	}
	
	public void dump(int level) {
		indent(level);
		System.err.println(this.type.toString());
		
		if (children.size() > 0) {
			for (AST child : children) {
				if (child != null) {
					child.dump(level + 1);
				}
			}
		} else {
			indent(level + 1);
			System.err.println("  empty");
		}		
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		s.append("[" + this.lineNo + ": " + type.toString() + ": ");
		
		for (AST child : children)
			if (child != null)
				s.append(child.toString());
		
		s.append("]");

		return s.toString();
	}

	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsProgramException {
		switch(this.type) {
			case ADD:
				return evalAdd(environment);
			case ALIAS:
				return evalAlias(environment);
			case AND:
				return evalAnd(environment);
			case ASSIGN:
				return evalAssign(environment);
			case CLASS_STATEMENT_LIST:
				return evalClassStatementList(environment);
			case CREATE_CLASS:
				return evalCreateClass(environment);
			case CREATE_OBJ:
				return evalCreateObj(environment);
			case DEFINE_ACTION:
				return evalDefineAction(environment);
			case DEFINE_PROPERTY:
				return evalDefineProperty(environment);
			case EQUALS:
				return evalEquals(environment);
			case EXIT:
				return evalExit(environment);
			case EXPONENTIATE:
				return evalExponentiate(environment);
			case GEQ:
				return evalGEQ(environment);
			case GREATER:
				return evalGreater(environment);
			case IDENTIFIER_LIST:
				return evalIdentifierList(environment);
			case IF:
				return evalIf(environment);
			case LEQ:
				return evalLEQ(environment);
			case LESS:
				return evalLess(environment);
			case LISTENER_PERM:
				return evalListenerPerm(environment);
			case LISTENER_TEMP:
				return evalListenerTemp(environment);
			case MOVES_PREDICATE:
				return evalMovesPredicate(environment);
			case MULTIPLY:
				return evalMultiply(environment);
			case NEGATE:
				return evalNegate(environment);
			case NOT:
				return evalNot(environment);
			case OR:
				return evalOr(environment);
			case PARAMETER:
				return evalParameter(environment);
			case PARAMETER_LIST:
				return evalParameterList(environment);
			case POSITION:
				return evalPosition(environment);
			case QUEUE_ACTION:
				return evalQueueAction(environment);
			case QUEUE_ASSIGN:
				return evalQueueAssign(environment);
			case QUEUE_ASSIGN_PROPERTY:
				return evalQueueAssignProperty(environment);
			case QUEUE_ASSIGN_PROPERTY_LIST:
				return evalQueueAssignPropertyList(environment);
			case QUEUE_MOVE:
				return evalQueueMove(environment);
			case QUEUE_SAY:
				return evalQueueSay(environment);
			case QUEUE_STOP:
				return evalQueueStop(environment);
			case QUEUE_WAIT:
				return evalQueueWait(environment);
			case REFERENCE_LIST:
				return evalReferenceList(environment);
			case REMOVE:
				return evalRemove(environment);
			case REPEAT:
				return evalRepeat(environment);
			case RESET:
				return evalReset(environment);
			case RETRIEVE_PROPERTY:
				return evalRetrieveProperty(environment);
			case SAYS_PREDICATE:
				return evalSaysPredicate(environment);
			case STATEMENT_LIST:
				return evalStatementList(environment);
			case SUBJECT:
				return evalSubject(environment);
			case SUBTRACT:
				return evalSubtract(environment);
			case TOUCHES_PREDICATE:
				return evalTouchesPredicate(environment);
			case WAITS_PREDICATE:
				return evalWaitsPredicate(environment);
			case WHILE:
				return evalWhile(environment);
			default:
				throw new AssertionError("INode evaluated with unexpected AST node type " + this.type.toString());
		}
	}

	/**
	 * Checks that the arguments to a relational operator <, <=, >, >= are appropriate and throws an appropriate
	 * exception if not.
	 */
	private void checkRelOpArgTypes(ASTValue lhs, ASTValue rhs) throws WordsProgramException {
		if ((lhs.type != ValueType.NUM && lhs.type != ValueType.STRING) ||
			(rhs.type != ValueType.NUM && rhs.type != ValueType.STRING) ||
			(lhs.type == ValueType.NUM && rhs.type != ValueType.NUM)	||
			(lhs.type == ValueType.STRING && rhs.type != ValueType.STRING)) {
			// TODO: Probably need a new OperatorTypeMismatch exception, since InvalidTypeException isn't quite right
			throw new WordsProgramException(lineNo, new InvalidTypeException("", ""));
		}
	}
	
	private ASTValue evalAdd(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalAlias(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalAnd(WordsEnvironment environment) throws WordsProgramException {
		ASTValue lhs = children.get(0).eval(environment);
		ASTValue rhs = children.get(1).eval(environment);
		
		assert lhs.type == ValueType.BOOLEAN : "Left predicate has type " + lhs.type.toString();
		assert rhs.type == ValueType.BOOLEAN : "Right predicate has type " + rhs.type.toString();
		
		return new ASTValue(lhs.booleanValue && rhs.booleanValue);
	}

	private ASTValue evalAssign(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");	
	}

	private ASTValue evalClassStatementList(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalCreateClass(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");	
	}

	private ASTValue evalCreateObj(WordsEnvironment environment) throws WordsProgramException {
		ASTValue objName = children.get(0).eval(environment);
		ASTValue className = children.get(1).eval(environment);
		ASTValue properties = children.get(2) != null ? children.get(2).eval(environment) : null;
		ASTValue position = children.get(3).eval(environment);

		try {
			WordsObject newObject = environment.createObject(objName.stringValue, className.stringValue, position.positionValue);
		} catch (WordsEnvironmentException e) {
			throw new WordsProgramException(this.lineNo, e);
		}
		// TODO
		if (properties != null) {
			// For each element of properties, add property to newObject
		}
		
		return null;
	}

	private ASTValue evalDefineAction(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");	
	}

	private ASTValue evalDefineProperty(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");	
	}

	private ASTValue evalEquals(WordsEnvironment environment) throws WordsProgramException {
		ASTValue lhs = children.get(0).eval(environment);
		ASTValue rhs = children.get(1).eval(environment);
		
		// The special type Nothing is equal only to Nothing
		if ((lhs.type == ValueType.NOTHING && rhs.type != ValueType.NOTHING) || 
			(lhs.type != ValueType.NOTHING && rhs.type == ValueType.NOTHING)) {
			return new ASTValue(false);
		}
		
		if (lhs.type == ValueType.NOTHING && rhs.type == ValueType.NOTHING) {
			return new ASTValue(true);
		}
		
		// Objects only equal another object and cannot be equal to non-objects
		if ((lhs.type == ValueType.OBJ && rhs.type != ValueType.OBJ) || 
			(lhs.type != ValueType.OBJ && rhs.type == ValueType.OBJ)) {
			return new ASTValue(false);
		}
		
		if ((lhs.type == ValueType.OBJ && rhs.type == ValueType.OBJ) &&
			(lhs.objValue == rhs.objValue)) {
			return new ASTValue(true);
		}
		
		// Remaining types must be a number or string
		assert lhs.type == ValueType.NUM || lhs.type == ValueType.STRING : "Left side has type " + lhs.type.toString();
		assert rhs.type == ValueType.NUM || rhs.type == ValueType.STRING : "Right side has type " + rhs.type.toString();
		
		// Number/string type coercion
		ValueType comparisonType;
		
		if (lhs.type == ValueType.STRING && rhs.type == ValueType.NUM) {
			try {
				double num = Double.parseDouble(lhs.stringValue);
				lhs = new ASTValue(num);
				comparisonType = ValueType.NUM;
			} catch (NumberFormatException e) {
				rhs = new ASTValue((new Double(rhs.numValue)).toString());
				comparisonType = ValueType.STRING;
			}
		} else if (lhs.type == ValueType.NUM && rhs.type == ValueType.STRING) {
			try {
				double num = Double.parseDouble(rhs.stringValue);
				rhs = new ASTValue(num);
				comparisonType = ValueType.NUM;
			} catch (NumberFormatException e) {
				lhs = new ASTValue((new Double(lhs.numValue)).toString());
				comparisonType = ValueType.STRING;
			}
		} else {
			// Types are the same
			comparisonType = lhs.type;
		}
		
		// lhs and rhs are now the same type equal to comparisonType
		assert lhs.type == rhs.type && lhs.type == comparisonType : "lhs has type " + lhs.type.toString() + " and rhs has type " + rhs.type.toString();
		switch (comparisonType) {
			case NUM:
				return new ASTValue(lhs.numValue == rhs.numValue);
			case STRING:
				return new ASTValue(lhs.stringValue.equals(rhs.stringValue));
			default:
				throw new AssertionError("Attempted to evaluate relational operator on ValueType " + lhs.type);			
		}
	}

	private ASTValue evalExit(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");	
	}

	private ASTValue evalExponentiate(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");	
	}

	private ASTValue evalGEQ(WordsEnvironment environment) throws WordsProgramException {
		ASTValue lhs = children.get(0).eval(environment);
		ASTValue rhs = children.get(1).eval(environment);
		
		checkRelOpArgTypes(lhs, rhs);
		
		// lhs and rhs are now the same type
		switch (lhs.type) {
			case NUM:
				return new ASTValue(lhs.numValue >= rhs.numValue);
			case STRING:
				return new ASTValue(lhs.stringValue.compareTo(rhs.stringValue) >= 0);
			default:
				throw new AssertionError("Attempted to evaluate relational operator on ValueType " + lhs.type);			
		}
	}

	private ASTValue evalGreater(WordsEnvironment environment) throws WordsProgramException {
		ASTValue lhs = children.get(0).eval(environment);
		ASTValue rhs = children.get(1).eval(environment);
		
		checkRelOpArgTypes(lhs, rhs);
		
		// lhs and rhs are now the same type
		switch (lhs.type) {
			case NUM:
				return new ASTValue(lhs.numValue > rhs.numValue);
			case STRING:
				return new ASTValue(lhs.stringValue.compareTo(rhs.stringValue) > 0);
			default:
				throw new AssertionError("Attempted to evaluate relational operator on ValueType " + lhs.type);			
		}
	}

	private ASTValue evalIdentifierList(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");	
	}

	private ASTValue evalIf(WordsEnvironment environment) throws WordsProgramException {
		ASTValue predicate = children.get(0).eval(environment);
		AST statementList = children.get(1);
		
		assert predicate.type == ValueType.BOOLEAN : "Predicate has type " + predicate.type.toString();
		
		if (predicate.booleanValue == true) {
			statementList.eval(environment);
		}
		
		return null;
	}

	private ASTValue evalLEQ(WordsEnvironment environment) throws WordsProgramException {
		ASTValue lhs = children.get(0).eval(environment);
		ASTValue rhs = children.get(1).eval(environment);
		
		checkRelOpArgTypes(lhs, rhs);
		
		// lhs and rhs are now the same type
		switch (lhs.type) {
			case NUM:
				return new ASTValue(lhs.numValue <= rhs.numValue);
			case STRING:
				return new ASTValue(lhs.stringValue.compareTo(rhs.stringValue) <= 0);
			default:
				throw new AssertionError("Attempted to evaluate relational operator on ValueType " + lhs.type);			
		}
	}

	private ASTValue evalLess(WordsEnvironment environment) throws WordsProgramException {
		ASTValue lhs = children.get(0).eval(environment);
		ASTValue rhs = children.get(1).eval(environment);
		
		checkRelOpArgTypes(lhs, rhs);
		
		// lhs and rhs are now the same type
		switch (lhs.type) {
			case NUM:
				return new ASTValue(lhs.numValue < rhs.numValue);
			case STRING:
				return new ASTValue(lhs.stringValue.compareTo(rhs.stringValue) < 0);
			default:
				throw new AssertionError("Attempted to evaluate relational operator on ValueType " + lhs.type);			
		}
	}

	private ASTValue evalListenerPerm(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");	
	}

	private ASTValue evalListenerTemp(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");	
	}

	private ASTValue evalMovesPredicate(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");	
	}

	private ASTValue evalMultiply(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");	
	}

	private ASTValue evalNegate(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");	
	}

	private ASTValue evalNot(WordsEnvironment environment) throws WordsProgramException {
		ASTValue predicate = children.get(0).eval(environment);
		
		assert predicate.type == ValueType.BOOLEAN : "Predicate has type " + predicate.type.toString();
		
		return new ASTValue(!predicate.booleanValue);
	}

	private ASTValue evalOr(WordsEnvironment environment) throws WordsProgramException {
		ASTValue lhs = children.get(0).eval(environment);
		ASTValue rhs = children.get(1).eval(environment);
		
		assert lhs.type == ValueType.BOOLEAN : "Left predicate has type " + lhs.type.toString();
		assert rhs.type == ValueType.BOOLEAN : "Right predicate has type " + rhs.type.toString();
		
		return new ASTValue(lhs.booleanValue || rhs.booleanValue);
	}

	private ASTValue evalParameter(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");	
	}

	private ASTValue evalParameterList(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalPosition(WordsEnvironment environment) throws WordsProgramException {
		ASTValue row = children.get(0).eval(environment).getNumCoercedVal();
		ASTValue col = children.get(1).eval(environment).getNumCoercedVal();
		
		if (row.type != ValueType.NUM) {
			throw new WordsProgramException(lineNo, new InvalidTypeException(ValueType.NUM.toString(), row.type.toString()));
		}
		
		if (col.type != ValueType.NUM) {
			throw new WordsProgramException(lineNo, new InvalidTypeException(ValueType.NUM.toString(), col.type.toString()));
		}
		
		return new ASTValue(new WordsPosition(row.numValue, col.numValue));
	}

	private ASTValue evalQueueAction(WordsEnvironment environment) {
		
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalQueueAssign(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalQueueAssignProperty(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalQueueAssignPropertyList(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalQueueMove(WordsEnvironment environment) throws WordsProgramException {
		ASTValue referenceObject = children.get(0).eval(environment);
		ASTValue identifier = children.get(1).eval(environment);
		ASTValue direction = children.get(2).eval(environment);
		AST distance = children.get(3);
		ASTValue doNow = children.get(4) != null ? children.get(4).eval(environment) : null;
		
		WordsObject object;
		if (referenceObject.type.equals(ValueType.OBJ)){
			WordsProperty property = referenceObject.objValue.getProperty(identifier.stringValue);
			if (property.type != WordsProperty.PropertyType.OBJECT) {
				throw new WordsProgramException(lineNo, new InvalidTypeException(ValueType.OBJ.toString(), property.type.toString()));
			}
			object = property.objProperty;
		} else {
			object = environment.getObject(identifier.stringValue);
			if (object == null) {
				throw new WordsProgramException(lineNo, new WordsObjectNotFoundException(identifier.stringValue));
			}
		}
		
		assert(direction.type == ValueType.DIRECTION) : "Expected direction";
		
		//TODO: Distance = 0 should create a wait method
		WordsMove action = new WordsMove(direction.directionValue, distance);
		
		if (doNow == null) {
			object.enqueueAction(action);
		} else {
			object.enqueueActionAtFront(action);
		}
		
		return null;
	}

	private ASTValue evalQueueSay(WordsEnvironment environment) throws WordsProgramException {
		ASTValue referenceObject = children.get(0).eval(environment);
		ASTValue identifier = children.get(1).eval(environment);
		ASTValue message = children.get(2).eval(environment).getStringCoercedVal();
		ASTValue doNow = children.get(3) != null ? children.get(3).eval(environment) : null;
		
		WordsObject object;
		if (referenceObject.type.equals(ValueType.OBJ)){
			WordsProperty property = referenceObject.objValue.getProperty(identifier.stringValue);
			if (property.type != WordsProperty.PropertyType.OBJECT) {
				throw new WordsProgramException(lineNo, new InvalidTypeException(ValueType.OBJ.toString(), property.type.toString()));
			}
			object = property.objProperty;
		} else {
			object = environment.getObject(identifier.stringValue);
		}
		
		if (message.type != ValueType.STRING) {
			throw new WordsProgramException(lineNo, new InvalidTypeException(ValueType.STRING.toString(), message.type.toString()));
		}
		WordsSay action = new WordsSay(message.stringValue);
		
		if (doNow == null) {
			object.enqueueAction(action);
		} else {
			object.enqueueActionAtFront(action);
		}
		
		return null;
	}

	private ASTValue evalQueueStop(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalQueueWait(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalReferenceList(WordsEnvironment environment) {
		// TODO
		//throw new AssertionError("Not yet implemented");
		return new ASTValue(ValueType.NOTHING);
	}

	private ASTValue evalRemove(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalRepeat(WordsEnvironment environment) throws WordsProgramException {
		ASTValue times = children.get(0).eval(environment).getNumCoercedVal();
		AST statementList = children.get(1);
		
		if (times.type != ValueType.NUM) {
			throw new WordsProgramException(lineNo, new InvalidTypeException(ValueType.NUM.toString(), times.type.toString()));
		}
		
		for (int i = 0; i < times.numValue; i++) {
			statementList.eval(environment);
		}
		
		return null;
	}

	private ASTValue evalReset(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalRetrieveProperty(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalSaysPredicate(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalStatementList(WordsEnvironment environment) {
		for (int i = 0; i < children.size(); i++) {
			try {
				children.get(i).eval(environment);
			} catch (WordsProgramException e) {
				System.err.println();
				System.err.println(e);
				System.out.print("> ");
			}
		}
		
		return null;
	}

	private ASTValue evalSubject(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalSubtract(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalTouchesPredicate(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalWaitsPredicate(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}

	private ASTValue evalWhile(WordsEnvironment environment) {
		// TODO
		throw new AssertionError("Not yet implemented");
	}
}
