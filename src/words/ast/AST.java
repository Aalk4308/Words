package words.ast;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

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
	public abstract void dump(int level);
	
	/**
	 * Evaluate an AST node to return an ASTValue and possibly have side effects on the passed environment.
	 * 
	 * @throws WordsRuntimeException 
	 */
	public abstract Variable eval(Environment environment) throws WordsRuntimeException;

	/**
	 * Evaluate an AST node using inherited attributes to return an ASTValue and possibly having side effects on the passed environment.
	 * 
	 * Subclasses may override this method if they use inherited attributes.  If not, their implementation of eval() without
	 * inherited attributes will be called.
	 * 
	 * @throws WordsRuntimeException
	 */
	public Variable eval(Environment environment, Object inherited) throws WordsRuntimeException { return eval(environment); };
}