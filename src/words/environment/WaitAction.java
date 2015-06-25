package words.environment;
import java.util.LinkedList;

import words.Variable;
import words.exceptions.*;
import words.ast.*;

/**
 * A wait action (basic action) for a WordsObject's action queue.
 */
public class WaitAction extends Action {
	private AST lengthExpression;

	public WaitAction(Scope scope) {
		super(scope);
		this.lengthExpression = null;
	}

	public WaitAction(Scope scope, AST lengthExpression) {
		super(scope);
		this.lengthExpression = lengthExpression;
	}

	@Override
	public boolean isExecutable() {
		return (lengthExpression == null);
	}

	@Override
	public void doExecute(WordsObject object, Environment environment) throws WordsProgramException {
		// Nothing to do, this will simply cause the object to wait 1 frame
	}

	@Override
	public LinkedList<Action> doExpand(WordsObject object, Environment environment) throws WordsProgramException {
		assert lengthExpression != null : "Length expression for Wait is null";

		Variable value;
		try {
			value = lengthExpression.eval(environment).tryCoerceTo(Variable.Type.NUM);
		} catch (WordsRuntimeException e) {
			throw new WordsProgramException(lengthExpression, e);
		}

		if (value.type != Variable.Type.NUM) {
			throw new WordsProgramException(lengthExpression, new InvalidTypeException(Variable.Type.NUM.toString(), value.type.toString()));
		}

		int lengthValue = (int) Math.round(value.numValue);

		// Throw an appropriate WordsException if lengthValue is zero or negative
		if (lengthValue < 1) {
			throw new WordsProgramException(lengthExpression, new InvalidTypeException("a positive number", String.format("%d", lengthValue)));
		}

		// Decompose into executable 1-frame waits
		LinkedList<Action> list = new LinkedList<Action>();
		for (int i = 0; i < lengthValue; i++)
			list.add(new WaitAction(scope));

		return list;
	}
}