package words.environment;

import java.util.HashSet;

import words.exceptions.*;
import words.ast.*;

public class CustomActionDefinition {
	private AST statementList;
	private HashSet<String> parameters;
	
	public CustomActionDefinition(AST actions) {
		this.statementList = actions;
		this.parameters = new HashSet<String>();
	}
	
	/**
	 * Registers a parameter name as available for this custom action definition.
	 * Registering the same name twice has no effect.
	 */
	public void addParameter(String paramName) {
		parameters.add(paramName);
	}
	
	/**
	 * Invoke this custom action definition on a given object using a given list of arguments.
	 * Arguments may be null if there are no arguments.
	 */
	public void invoke(Environment environment, WordsObject object, Scope arguments) throws WordsProgramException {
		// Custom action definitions can only appear in class definitions, which can only appear in the global scope
		environment.pushNewScope(environment.getGlobalScope());
		
		// Install the pronouns to point to the given object
		String[] pronouns = {"them", "it", "him", "her", "his", "its", "their", "Her", "His", "Its", "Their"};
		for (String pronoun : pronouns) {
			environment.addToCurrentScope(pronoun, new Property(object));
		}

		try {
			for (String argumentName : arguments.variables.keySet()) {
				if (parameters.contains(argumentName)) {
					environment.addToCurrentScope(argumentName, arguments.variables.get(argumentName));
				}
			}
			
			statementList.eval(environment);
		} catch (WordsRuntimeException e) {
			throw new WordsProgramException(statementList, e);
		}
		
		environment.popScope();
	}
}