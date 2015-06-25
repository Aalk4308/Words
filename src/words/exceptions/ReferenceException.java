package words.exceptions;

import words.Variable;

@SuppressWarnings("serial")
public class ReferenceException extends WordsRuntimeException {
	
	private Variable.Type wordsType;
	private String wordsId;
	
	public ReferenceException(String wordsId, Variable.Type wordsType) {
		this.wordsId = wordsId;
		this.wordsType = wordsType;
	}
	
	public String toString() {
		return String.format("Attempted to dereference %s which has invalid type %s", wordsId, wordsType);
	}
}
