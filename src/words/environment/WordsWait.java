package words.environment;
import java.util.LinkedList;

import words.ast.AST;
import words.ast.ASTValue;
import words.ast.ASTValue.ValueType;
import words.exceptions.WordsFunctionArgsException;
import words.exceptions.WordsInvalidTypeException;
import words.exceptions.WordsRuntimeException;

public class WordsWait extends WordsAction {
	private AST lengthExpression;
	private double lengthValue;
	
	public WordsWait(AST lengthExpression) {
		this.lengthExpression = lengthExpression;
	}
	
	/**
	 * Create a new WordsWait action.  lengthValue must round to a positive integer.
	 */
	public WordsWait(double lengthValue) {
		this.lengthValue = lengthValue;
	}

	@Override
	public boolean isExecutable() {
		if (lengthExpression != null)
			return false;

		if (Math.round(lengthValue) != 1)
			return false;
		
		return true;
	}

	@Override
	public void doExecute(WordsObject object, WordsEnvironment environment) {
		// Nothing to do, this will simply cause the object to wait 1 frame
	}

	@Override
	public LinkedList<WordsAction> doExpand(WordsObject object, WordsEnvironment environment) throws WordsProgramException {
		if (lengthExpression != null) {
			ASTValue value;
			try {
				value = lengthExpression.eval(environment).tryCoerceTo(ASTValue.ValueType.NUM);
			} catch (WordsRuntimeException e) {
				throw new WordsProgramException(lengthExpression, e);
			}
			
			if (value.type != ASTValue.ValueType.NUM) {
				throw new WordsProgramException(lengthExpression, new WordsInvalidTypeException(value.type.toString(), ASTValue.ValueType.NUM.toString()));
			}
			
			lengthValue = Math.round(value.numValue);
			lengthExpression = null;						// Not necessary, but including for clarity since once the expression is evaluated, it is no longer needed
		}

		// Throw an appropriate WordsException if lengthValue is zero or negative
		if (lengthValue < 1) {
			throw new WordsProgramException(lengthExpression, new WordsFunctionArgsException("wait", "a positive number", String.format("%d", lengthValue)));
		}
		
		LinkedList<WordsAction> list = new LinkedList<WordsAction>();

		// Decompose into executable 1-frame waits
		for (int i = 0; i < lengthValue; i++)
			list.add(new WordsWait(1));
		
		return list;
	}
}