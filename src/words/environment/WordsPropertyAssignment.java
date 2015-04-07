package words.environment;
import java.util.LinkedList;

import words.ast.AST;

public class WordsPropertyAssignment extends WordsAction {
	private String propertyName;
	private AST propertyValue;
	
	@Override
	public boolean isExecutable() {
		return true;
	}

	@Override
	protected void doExecute(WordsObject object, WordsEnvironment environment) {
		// TODO
		
		/*
		 * Evaluate propertyValue, check result if needed, and assign it to propertyName using the object's methods for that
		 */
	}

	@Override
	protected LinkedList<WordsAction> doExpand(WordsObject object, WordsEnvironment environment) { return null; }
}
