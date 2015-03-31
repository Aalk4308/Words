@SuppressWarnings("serial")
public class InvalidTypeException extends WordsEnvironmentException {
	
	private String expectedType;
	private String receivedType;
	
	public InvalidTypeException(String expected, String received) {
		this.expectedType = expected;
		this.receivedType = received;
	}

	@Override
	public String toString() {
		return String.format("expected %s, received %s.", expectedType, receivedType);
	}
}
