package words.exceptions;

@SuppressWarnings("serial")
public class WordsFunctionArgsException extends WordsRuntimeException {
	
	private String expectedArgs;
	private String receivedArgs;
	private String functionName;

	public WordsFunctionArgsException(String functionName, String receivedArgs, String expectedArgs) {
		this.functionName = functionName;
		this.receivedArgs = receivedArgs;
		this.expectedArgs = expectedArgs;
	}
	
	public String getString() {
		return String.format("function %s expected %s, received \"%s\".", 
				functionName, expectedArgs, receivedArgs);
	}
}
