package words.ast;

import words.Console;
import words.Variable;
import words.environment.*;
import words.exceptions.*;

public class INodeStatementList extends INode {
	public INodeStatementList(Object... children) {
		super(children);
	}

	@Override
	public Variable eval(Environment environment) throws WordsRuntimeException {
		for (int i = 0; i < children.size(); i++) {
			// At the beginning and end of each statement list, we should be at the same level of scope
			int startingScopeLevel = environment.getScopeDepth();
			
			// The parser could potentially return null for a statement if there are syntax errors
			// Avoid attempting to do any evaluation in that case
			if (children.get(i) == null)
				continue;

			try {
				children.get(i).eval(environment);
			} catch (WordsRuntimeException e) {
				// Add the AST to the exception, and then print it.
				WordsProgramException decoratedException = new WordsProgramException(children.get(i), e);
				System.err.println("\r");
				System.err.println(decoratedException);
				Console.showPrompt();
			}
			
			assert startingScopeLevel == environment.getScopeDepth() : "Scope error during statement list";
		}

		return null;
	}
}