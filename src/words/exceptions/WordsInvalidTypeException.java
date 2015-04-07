package words.exceptions;
@SuppressWarnings("serial")
public class WordsInvalidTypeException extends WordsRuntimeException {
	
	private String expectedType;
	private String receivedType;
	
	public WordsInvalidTypeException(String expected, String received) {
		this.expectedType = expected;
		this.receivedType = received;
	}

	@Override
	public String toString() {
		return String.format("expected %s, received %s.", expectedType, receivedType);
	}
}
