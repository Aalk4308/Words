package words.environment;
import java.util.HashMap;

public class WordsClass {
	private HashMap<String, WordsProperty> properties;
	private HashMap<String, WordsCustomActionDefinition> functions;
	private WordsClass parent;
	private String className;
	
	public WordsClass(String className, WordsClass parent) {
		properties = new HashMap<String, WordsProperty>();
		functions = new HashMap<String, WordsCustomActionDefinition>();
		this.parent = parent;
		this.className = className;
	}
		
	public String getClassName() {
		return className;
	}
	
	/**
	 * Retrieves a property of a class by looking only at the class itself, ignoring its class chain.
	 * A missing property returns null.
	 */
	private WordsProperty getOwnProperty(String propertyName) {
		if (properties.containsKey(propertyName))
			return properties.get(propertyName);
		else
			return null;
	}
	
	/**
	 * Retrieves a property on a class by looking at the class itself and its class chain.
	 * A missing property returns a WordsProperty of type NOTHING
	 */
	public WordsProperty getProperty(String propertyName) {
		WordsClass lookupClass = this;
		
		while (lookupClass != null) {
			WordsProperty property = lookupClass.getOwnProperty(propertyName);
			
			if (property != null)
				return property;
			
			lookupClass = lookupClass.parent;
		}
		
		return new WordsProperty(WordsProperty.PropertyType.NOTHING);
	}
	
	public void setProperty(String propertyName, WordsProperty property) {
		// TODO
		// Handle case where property is nothing, in which case we should actually remove the key if it exists, not store it
	}
	
	public WordsCustomActionDefinition getCustomActionDefinition(String customActionName) {
		// TODO
		return null;
	}
	
	public void addCustomAction(String customActionName, WordsCustomActionDefinition customActionDefinition) {
		// TODO
	}
}