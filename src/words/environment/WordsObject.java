package words.environment;
import java.util.HashMap;
import java.util.LinkedList;


public class WordsObject {
	private String objectName;
	private WordsClass wordsClass;
	private HashMap<String, WordsProperty> properties;
	private LinkedList<WordsAction> actionQueue;
	private WordsPosition cell;
	private String currentMessage;
	
	public WordsObject(String objectName, WordsClass wordsClass, WordsPosition cell) {
		this.wordsClass = wordsClass;
		this.objectName = objectName;
		this.cell = cell;
		this.actionQueue = new LinkedList<WordsAction>();
	}
	
	public void enqueueAction(WordsAction action) {
		actionQueue.add(action);
	}
	
	public void enqueueActionAtFront(WordsAction action) {
		actionQueue.addFirst(action);
	}
	
	/**
	 * Retrieves a property of an object by looking only at the object itself, ignoring its class chain.
	 * A missing property returns null.
	 */
	private WordsProperty getOwnProperty(String propertyName) {
		if (properties.containsKey(propertyName))
			return properties.get(propertyName);
		else
			return null;
	}
	
	/**
	 * Retrieves a property on an object by looking at the object itself and its class chain.
	 * A missing property returns a WordsProperty of type NOTHING.
	 */
	public WordsProperty getProperty(String propertyName) {
		// Special handling of "row" and "column" properties
		if (propertyName.equals("row"))
			return new WordsProperty(cell.y);
		else if (propertyName.equals("column"))
			return new WordsProperty(cell.x);
		
		WordsProperty property = getOwnProperty(propertyName);
		
		if (property != null)
			return property;
		else
			return wordsClass.getProperty(propertyName);
	}
	
	/**
	 * Assigns a property to an object.  Assigning NOTHING removes the property, if it exists.
	 */
	public void setProperty(String propertyName, WordsProperty property) {
		// Special handling of "row" and "column" properties
		if (propertyName.equals("row") || propertyName.equals("column")) {
			if (property.type != WordsProperty.PropertyType.NUM) {
				// throw an exception?
				return;
			}
			
			if (propertyName.equals("row"))
				cell.y = (int) Math.round(property.numProperty);
			else
				cell.x = (int) Math.round(property.numProperty);
		}
		
		if (properties.containsKey(propertyName) && property.type == WordsProperty.PropertyType.NOTHING)
			properties.remove(propertyName);
		else
			properties.put(propertyName, property);
	}
	
	public void moveUp() {
		this.cell.y--;
	}
	
	public void moveDown() {
		this.cell.y++;
	}
	
	public void moveLeft() {
		this.cell.x--;
	}
	
	public void moveRight() {
		this.cell.x++;
	}
	
	public void executeNextAction(WordsEnvironment environment) throws WordsProgramException {
		if (!actionQueue.isEmpty()) {
			while (actionQueue.peek().isExpandable()) {
				WordsAction action = actionQueue.pop();
				actionQueue.addAll(0, action.expand(this, environment));
			}
			
			WordsAction action = actionQueue.pop();
			action.execute(this, environment);
		}
	}
	
	public WordsPosition getCurrentCell() {
		return cell;
	}
	
	public String getClassName() {
		return wordsClass.getClassName();
	}
	
	public WordsClass getWordsClass() {
		return wordsClass;
	}
	
	public String getCurrentMessage() {
		return currentMessage;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setMessage(String message) {
		currentMessage = message;
	}
}