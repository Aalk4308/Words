package words.exceptions;
@SuppressWarnings("serial")
public class WordsOperatorTypeMismatchException extends WordsRuntimeException {
	
	private String lhsType;
	private String rhsType;
	
	public WordsOperatorTypeMismatchException(String lhs, String rhs) {
		this.lhsType = lhs;
		this.rhsType = rhs;
	}

	@Override
	public String toString() {
		return String.format("cannot compare %s to %s.", lhsType, rhsType);
	}
}
