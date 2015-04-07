package words.ast;
import words.environment.WordsEnvironment;
import words.exceptions.WordsRuntimeException;
/**
 * An abstract syntax tree node, which may be either an internal node or leaf node.
 */
public abstract class AST {
	public int lineNumber;
	
	public AST() {
		lineNumber = -1;
	}
	
	/**
	 * Debugging method to dump this node and all its children, if any, to standard error for inspection.
	 * 
	 * @param level the indentation level
	 */
	abstract public void dump(int level);
	
	/**
	 * Evaluate an AST node to return an ASTValue and possibly have side effects on the passed environment.
	 * @throws WordsRuntimeException 
	 */
	public abstract ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException;
}