package words.environment;

import words.Variable;
import words.exceptions.*;
import words.ast.*;

/**
 * An event listener as specified in the Words language.
 */
public class WordsEventListener {
	private AST predicate;
	private AST statementList;
	private boolean temporary;

	public WordsEventListener(AST predicate, AST statementList, boolean temporary) {
		this.predicate = predicate;
		this.statementList = statementList;
		this.temporary = temporary;
	}
	
	/**
	 * Execute a listener
	 * @return false if the listener should be deleted by the caller. 
	 * @throws WordsProgramException
	 */
	public boolean execute(Environment environment) throws WordsProgramException {
		if (predicate instanceof INodeBasicActionPredicate) {
			boolean predVal = false;
			try {
				predVal = predicate.eval(environment, statementList).booleanValue;
			} catch (WordsRuntimeException e) {
				throw new WordsProgramException(statementList, e);
			}
			return !temporary || predVal;
		} else {
			Variable predicateValue;
			try {
				predicateValue = predicate.eval(environment);
			} catch (WordsRuntimeException e) {
				throw new WordsProgramException(predicate, e);
			}
			
			assert predicateValue.type == Variable.Type.BOOLEAN : "Predicate has type " + predicateValue.type.toString();
	
			if (predicateValue.booleanValue == true) {
				try {
					statementList.eval(environment);
				} catch (WordsRuntimeException e) {
					throw new WordsProgramException(statementList, e);
				}
			} else {
				if (temporary) {
					return false;
				}
			}
	
			return true;
		}
	}
}