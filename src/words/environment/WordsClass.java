package words.environment;
import java.util.ArrayList;
import java.util.HashMap;

import words.Variable;
import words.exceptions.CustomActionAlreadyExistsException;
import words.exceptions.CustomActionNotFoundException;

/**
 * A class as specified in the Words language.
 */
public class WordsClass {
	private HashMap<String, Variable> properties;
	private HashMap<String, CustomActionDefinition> customActions;
	private WordsClass parent;
	private String className;
	private ArrayList<WordsClass> children;
	
	public WordsClass(String className, WordsClass parent) {
		properties = new HashMap<String, Variable>();
		customActions = new HashMap<String, CustomActionDefinition>();
		children = new ArrayList<WordsClass>();
		this.parent = parent;
		this.className = className;
		if (parent != null) {
			// everything except the base superclass should have a parent. 
			parent.registerChild(this);
		}
	}
		
	public String getClassName() {
		return className;
	}
	
	/**
	 * Register a child to its parent
	 * 
	 */
	public void registerChild(WordsClass childClass) {
		children.add(childClass);
	}
	
	/**
	 * Get all classes that inherit from this one
	 */
	public ArrayList<WordsClass> getChildren() {
		return children;
	}
	
	/**
	 * Retrieves a property of a class by looking only at the class itself, ignoring its class chain.
	 * A missing property returns null.
	 */
	private Variable getOwnProperty(String propertyName) {
		if (properties.containsKey(propertyName))
			return properties.get(propertyName);
		else
			return null;
	}
	
	/**
	 * Retrieves a property on a class by looking at the class itself and its class chain.
	 * A missing property returns a WordsProperty of type NOTHING.
	 */
	public Variable getProperty(String propertyName) {
		WordsClass lookupClass = this;
		
		while (lookupClass != null) {
			Variable property = lookupClass.getOwnProperty(propertyName);
			
			if (property != null)
				return property;
			
			lookupClass = lookupClass.parent;
		}
		
		return new Variable(Variable.Type.NOTHING);
	}
	
	/**
	 * Sets a property on this class for a given name, overwriting any existing property for that name.
	 * Does not set anything if the provided property is NOTHING.
	 */
	public void setProperty(String propertyName, Variable property) {
		if (property.type != Variable.Type.NOTHING) {
			properties.put(propertyName, property);
		}
	}
	
	/**
	 * Creates a new custom action in this class.  Throws an exception if a custom action of that
	 * name has already been defined.
	 * @throws CustomActionAlreadyExistsException 
	 */
	public void defineCustomAction(String customActionName, CustomActionDefinition customAction) throws CustomActionAlreadyExistsException {
		if (customActions.containsKey(customActionName)) {
			throw new CustomActionAlreadyExistsException(customActionName);
		} else {		
			customActions.put(customActionName, customAction);
		}
	}

	/**
	 * Retrieves a custom action definition of a class by looking only at the class itself, ignoring its class chain.
	 * A missing custom action definition returns null.
	 */
	private CustomActionDefinition getOwnCustomActionDefinition(String customActionName) {
		if (customActions.containsKey(customActionName))
			return customActions.get(customActionName);
		else
			return null;
	}
	
	/**
	 * Retrieves a custom action definition on a class by looking at the class itself and its class chain.
	 * A missing custom action definition throws an exception.
	 */
	public CustomActionDefinition getCustomActionDefinition(String customActionName) throws CustomActionNotFoundException {
		WordsClass lookupClass = this;
		
		while (lookupClass != null) {
			CustomActionDefinition customActionDefinition = lookupClass.getOwnCustomActionDefinition(customActionName);
			
			if (customActionDefinition != null)
				return customActionDefinition;
			
			lookupClass = lookupClass.parent;
		}

		throw new CustomActionNotFoundException(customActionName);
	}
}