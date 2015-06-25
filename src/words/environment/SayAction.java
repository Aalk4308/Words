package words.environment;
import java.util.LinkedList;

import words.Variable;
import words.exceptions.*;
import words.ast.*;

/**
 * A say action (basic action) for a WordsObject's action queue.
 */
public class SayAction extends Action {
	private AST message;

	public SayAction(Scope scope, AST message) {
		super(scope);
		this.message = message;
	}

	@Override
	public boolean isExecutable() {
		return true;
	}

	@Override
	protected void doExecute(WordsObject object, Environment environment) throws WordsProgramException {
		assert message != null : "Say message is null";

		Variable value;
		try {
			value = message.eval(environment).tryCoerceTo(Variable.Type.STRING);
		} catch (WordsRuntimeException e) {
			throw new WordsProgramException(message, e);
		}

		if (value.type != Variable.Type.STRING) {
			throw new WordsProgramException(message, new InvalidTypeException(Variable.Type.STRING.toString(), value.type.toString()));
		}

		object.setMessage(value.stringValue);
	}

	@Override
	protected LinkedList<Action> doExpand(WordsObject object, Environment environment) { return null; }
}