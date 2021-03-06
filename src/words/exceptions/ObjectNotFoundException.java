package words.exceptions;
@SuppressWarnings("serial")
public class ObjectNotFoundException extends WordsRuntimeException {
	
	private String objectName;
	
	public ObjectNotFoundException(String objectName) {
		this.objectName = objectName;
	}
	
	public String getObjectName() {
		return objectName;
	}
	
	@Override
	public String toString() {
		return String.format("Object %s does not exist or was not in scope.", objectName);
	}
}
