package words.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import words.Variable;
import words.exceptions.*;

/**
 * An object as specified in the Words language.
 */
public class WordsObject {
	private String objectName;
	private WordsClass wordsClass;
	private HashMap<String, Variable> properties;
	private LinkedList<Action> actionQueue;
	private Position currentPosition;
	private String currentMessage;
	private Action lastAction;
	private boolean shouldRemove;
	private boolean createdInThisFrame;
	private Position startOfFramePosition;
	
	// While an object is expanding a custom action, actions are enqueued in a separate list
	private boolean isExpandingCustomAction;
	private LinkedList<Action> customActionExpansion;
	private HashMap<WordsObject, ArrayList<Variable>> referers;
	
	public WordsObject(String objectName, WordsClass wordsClass, Position cell) {
		this.wordsClass = wordsClass;
		this.objectName = objectName;
		this.currentPosition = cell;
		this.actionQueue = new LinkedList<Action>();
		this.properties = new HashMap<String, Variable>();
		this.customActionExpansion = new LinkedList<Action>();
		this.isExpandingCustomAction = false;
		this.referers = new HashMap<WordsObject, ArrayList<Variable>>();
		this.shouldRemove = false;
		this.createdInThisFrame = true;
	}
	
	public void clearActionQueue() {
		actionQueue.clear();
	}

	public void enqueueAction(Action action) {
		if (this.isExpandingCustomAction) {
			customActionExpansion.add(action);
		} else {
			actionQueue.add(action);
		}
	}

	public void enqueueActionAtFront(Action action) {
		if (this.isExpandingCustomAction) {
			customActionExpansion.addFirst(action);
		} else {
			actionQueue.addFirst(action);
		}
	}

	/**
	 * Prepares this object to receive queue statements that represent the expansion of a custom action.
	 */
	public void startExpandingCustomAction() {
		isExpandingCustomAction = true;
		customActionExpansion.clear();
	}
	
	/**
	 * Returns the list of actions that was expanded and reverts the object to its normal state.
	 */
	public LinkedList<Action> finishExpandingCustomAction() {
		isExpandingCustomAction = false;
		return customActionExpansion;
	}

	/**
	 * Retrieves a property of an object by looking only at the object itself, ignoring its class chain.
	 * A missing property returns null.
	 */
	private Variable getOwnProperty(String propertyName) {
		if (properties.containsKey(propertyName))
			return properties.get(propertyName);
		else
			return null;
	}

	/**
	 * Retrieves a property on an object by looking at the object itself and its class chain.
	 * A missing property returns a WordsProperty of type NOTHING.
	 */
	public Variable getProperty(String propertyName) {
		// Special handling of "row" "column" "name" and "class" properties
		if (propertyName.equals("row"))
			return new Variable(currentPosition.y);
		else if (propertyName.equals("column"))
			return new Variable(currentPosition.x);
		else if (propertyName.equals("name"))
			return new Variable(objectName);
		else if (propertyName.equals("class"))
			return new Variable(wordsClass.getClassName());

		Variable property = getOwnProperty(propertyName);

		if (property != null)
			return property;
		else
			return wordsClass.getProperty(propertyName);
	}

	/**
	 * Assigns a property to an object.  Assigning NOTHING removes the property, if it exists.
	 */
	public void setProperty(String propertyName, Variable property) throws WordsRuntimeException {
		// Special handling of "row" and "column" properties
		if (propertyName.equals("row") || propertyName.equals("column")) {
			if (property.type != Variable.Type.NUM) {
				throw new InvalidTypeException(Variable.Type.NUM.toString(), property.type.toString());
			}

			if (propertyName.equals("row"))
				currentPosition.y = (int) Math.round(property.numValue);
			else
				currentPosition.x = (int) Math.round(property.numValue);
			
			return;
		}
		
		// User cannot change "name" and "class"
		if (propertyName.equals("name") || propertyName.equals("class")) {
			throw new ModifyObjectPropertyException(propertyName);
		}

		if (properties.containsKey(propertyName) && property.type == Variable.Type.NOTHING) {
			properties.remove(propertyName);
		}
		if (property.type == Variable.Type.OBJ) {
			property.objValue.updateReferers(this, property);
		}
		properties.put(propertyName, property);
	}
	
	public void updateReferers(WordsObject referringObject, Variable referringProperty) {
		ArrayList<Variable> referringProperties;
		if ((referringProperties = referers.get(referringObject)) == null) {
			referringProperties = new ArrayList<Variable>();
			referers.put(referringObject, referringProperties);
		}
		
		referringProperties.add(referringProperty);
	}
	
	public void clearReferers() {
		for (Iterator<ArrayList<Variable>> it = referers.values().iterator(); it.hasNext();) {
			ArrayList<Variable> properties = it.next();
			
			for (Variable property : properties) {
				property.type = Variable.Type.NOTHING;
				property.objValue = null;
			}
			
			it.remove();
		}
	}
	
	public void flagForRemoval() {
		shouldRemove = true;
	}
	
	public boolean shouldRemove() {
		return shouldRemove;
	}
	
	public void moveUp() {
		this.currentPosition.y++;
	}

	public void moveDown() {
		this.currentPosition.y--;
	}

	public void moveLeft() {
		this.currentPosition.x--;
	}

	public void moveRight() {
		this.currentPosition.x++;
	}

	/**
	 * Executes the next action on this object's action queue, updating its lastAction
	 * property accordingly.  An object will not execute an action if it was just created
	 * in this frame.
	 */
	public void executeNextAction(Environment environment) throws WordsProgramException {
		if (createdInThisFrame) {
			createdInThisFrame = false;
		} else {
			if (!actionQueue.isEmpty()) {
				while (actionQueue.peek().isExpandable()) {
					Action action = actionQueue.pop();
					actionQueue.addAll(0, action.expand(this, environment));
					
					// If the action we just expanded was a custom action, it is possible that its expansion
					// executed some immediate statements but caused no new actions to be enqueued
					// In this case, we are done
					if (actionQueue.isEmpty()) {
						lastAction = new WaitAction(environment.getCurrentScope());
						return;
					}
				}
				
				Action action = actionQueue.pop();
				lastAction = action;
				action.execute(this, environment);
			} else {
				lastAction = new WaitAction(environment.getCurrentScope());
			}
		}
	}
	
	public boolean movedInThisFrame() {
		return lastAction == null || !startOfFramePosition.equals(currentPosition);
	}
	
	public void recordStartOfFramePosition() {
		startOfFramePosition = new Position(currentPosition);
	}

	public Position getCurrentPosition() {
		return currentPosition;
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
	
	public Action getLastAction() {
		return lastAction;
	}

	public void setMessage(String message) {
		currentMessage = message;
	}
}