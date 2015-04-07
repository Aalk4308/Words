package words.exceptions;
@SuppressWarnings("serial")
public class WordsReferenceException extends WordsRuntimeException {
	
	public WordsReferenceException() {
	}
	
	public String toString() {
		return String.format("attempted to dereference nothing");
	}
}
