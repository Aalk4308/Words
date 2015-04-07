package words.exceptions;
@SuppressWarnings("serial")
public class WordsObjectNotFoundException extends WordsRuntimeException {
	
	private String objectName;
	
	public WordsObjectNotFoundException(String objectName) {
		this.objectName = objectName;
	}
	
	public String getObjectName() {
		return objectName;
	}
	
	@Override
	public String toString() {
		return String.format("object %s did not exist.", objectName);
	}
}
