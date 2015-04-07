package words.ast;

import words.environment.WordsEnvironment;
import words.environment.WordsProgramException;
import words.exceptions.WordsRuntimeException;

public class INodeStatementList extends INode {
	public INodeStatementList(Object... children) {
		super(children);
	}
	
	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException {
		for (int i = 0; i < children.size(); i++) {
			// The parser could potentially return null for a statement if there are syntax errors
			// Avoid attempting to do any evaluation in that case
			if (children.get(i) == null)
				continue;
			
			try {
				children.get(i).eval(environment);
			} catch (WordsRuntimeException e) {
				// Add the AST to the exception, and then print it.
				WordsProgramException decoratedException = new WordsProgramException(children.get(i), e);
				System.err.println();
				System.err.println(decoratedException);
				System.out.print("> ");
			}
		}
		
		return null;
	}
}