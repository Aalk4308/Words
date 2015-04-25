package words.environment;
import java.util.LinkedList;

import words.exceptions.*;
import words.ast.*;

/**
 * A custom action for a WordsObject's action queue.
 */
public class CustomAction extends Action {
	private CustomActionDefinition actionDefinition;
	private AST arguments;
	
	public CustomAction(Scope scope, CustomActionDefinition actionDefinition, AST arguments) {
		super(scope);
		this.actionDefinition = actionDefinition;
		this.arguments = arguments;
	}

	@Override
	public boolean isExecutable() {
		return false;
	}

	@Override
	protected void doExecute(WordsObject object, Environment environment) {}

	@Override
	protected LinkedList<Action> doExpand(WordsObject object, Environment environment) throws WordsProgramException {
		// Evaluate the arguments (in the current scope) that will be passed to the formal parameters
		Scope evaluatedArguments = new Scope(null);
		if (arguments != null) {
			try {
				arguments.eval(environment, evaluatedArguments);
			} catch (WordsRuntimeException e) {
				throw new WordsProgramException(arguments, e);
			}
		}
		
		// Set up the object so that new actions are enqueued on a temporary list rather than the main action queue
		object.startExpandingCustomAction();
		actionDefinition.invoke(environment, object, evaluatedArguments);
		return object.finishExpandingCustomAction();
	}
}