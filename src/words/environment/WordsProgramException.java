package words.environment;

import words.ast.AST;
import words.exceptions.WordsRuntimeException;

/**
 * This exception decorates an existing WordsRuntimeException by including the AST that caused it.
 * The WordsEnvironmentException contains what went wrong, while the AST contains where it went wrong.
 */
@SuppressWarnings("serial")
public class WordsProgramException extends Exception {
	
	private WordsRuntimeException exception;
	private AST offendingAST;
	
	public WordsProgramException(AST offendingAST, WordsRuntimeException exception) {
		this.offendingAST = offendingAST;
		this.exception = exception;
	}
	
	public WordsRuntimeException getEnvironmentException() {
		return exception;
	}
	
	@Override
	public String toString() {
		return String.format("Error at line %d: %s", offendingAST.lineNumber, exception.toString());
	}
}
