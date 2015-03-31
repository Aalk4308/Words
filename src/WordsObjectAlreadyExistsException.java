@SuppressWarnings("serial")
public class WordsObjectAlreadyExistsException extends WordsEnvironmentException {

	private String objectName;
	
	public WordsObjectAlreadyExistsException(String objectName) {
		this.objectName = objectName;
	}
	
	@Override
	public String toString() {
		return String.format("object %s already existed. Cannot be created again.", objectName);
	}
}
